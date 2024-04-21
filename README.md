This is a demo for interacting with the UPS Shipment API. UPS recently transitioned from an access code to a client credentials OAuth2 flow for Authorization when interacting with its API.

This Application has been built with SpringBoot 3.2.4 with key libraries as follows:
1. Spring web for configuring RESTful services
2. Spring OAuth2 Client for security and configuring OAuth2 work flows

## How to use this Program
This code base can be used as an embedded code or a standalone web server. Since the Shipment API requires a JSON payload that describes the request, a payload has been constructed so that it can accept details of the shipment which are not limited to payment, packaging, description, address etc. some of this data can be made constant and others variable so as to represent the correct detail submitted when a package is scanned for shipment.

Once this is done, a request is sent to the UPS shipment api adding the authorization token as a header and a response gotten. Then the shipping label is extracted from the response and then sent to a thermal printer for printing
### Must Haves
1. A code Editor or an IDE
2. latest JDK or version 11 at least

The source code for this project has been divided into two packages viz: Controller and Security

### Security Package
This package contains a security configuration class has that various Beans for configuring security.
The bean below is a method to configure the client. This provides an in memory representation of the client. The client in this case is this application that wants to obtain an Oauth2 authrozation code from UPS using its CLIENT-ID, CLIENT_SECRET and TOKEN_URL.

    @Bean

    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration c1 = ClientRegistration.withRegistrationId("ups")
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .tokenUri(TOKEN_URL)
                .build();

        InMemoryClientRegistrationRepository clientRegistrationRepository =
                new InMemoryClientRegistrationRepository(c1);

        return clientRegistrationRepository;
    }

The bean below is a method to configure a manager for the client defined above. The client manager is what will manage the client with respect to storing tokens, checking for token expiry before send sending a token request, and managing the refresh token that has been configured

    @Bean
    public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository
    ) {
        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .refreshToken()
                        .clientCredentials()
                        .build();

        DefaultOAuth2AuthorizedClientManager defaultOAuth2AuthorizedClientManager
                = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, auth2AuthorizedClientRepository);
        defaultOAuth2AuthorizedClientManager.setAuthorizedClientProvider(provider);

        return defaultOAuth2AuthorizedClientManager;
    }
### Controller Package
This package has the Shipment controller class, this class is used for performing the shipment request to the UPS Shipment API

First of it has the getToken method which retrieves the token from the UPS token endpoint by making use of the client manager discussed above.
The code below details the getToken method

    private String getToken(){
        OAuth2AuthorizeRequest re = OAuth2AuthorizeRequest.withClientRegistrationId("ups")
                .principal(CLIENT_ID)
                .build();

        OAuth2AuthorizedClient authorize = oAuth2AuthorizedClientManager.authorize(re);
        assert authorize != null;
        String tokenValue = authorize.getAccessToken().getTokenValue();
        System.out.println(tokenValue);
        return tokenValue;
    }

*** When this method is called, it first check the token repository to see if the current token is still valid. if yes no need to call the UPS endpoint.

Next, in the controller is this method "public void standardShippingApi()" it sends the payload using http and the token obtained in the previous section as part of the authorization header.

The method returns a response that is a JSON data. This JSON data has as part of the response a base64 encoded shipping label image. This image is what is of interest here.
The following code sample is used for extracting the encoded image from the Shipment Response

    public String extractImageValue(String str) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(str);
        JsonNode packageResults = rootNode.path("ShipmentResponse").path("ShipmentResults").get("PackageResults");
        String val = null;
        for (JsonNode pack : packageResults){
            val = pack.path("ShippingLabel").get("GraphicImage").asText();
        }
        return val;

    }
The following code sample illustrates sending the payload, getting a response, and extracting the image from the response.

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String s1 = extractImageValue(response.body());
        //I need to so a base64 decoding of this string
        try{
            System.out.println(s1);
            byte[] imageBytes = Base64.getDecoder().decode(s1);
            FileOutputStream fos = new FileOutputStream("Shipping_label.jpg");
            fos.write(imageBytes);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        //System.out.println(response.body());

The FileOutpitStream configuration can be replace with a thermal printer configuration inorder to get the decoded image into a printer.