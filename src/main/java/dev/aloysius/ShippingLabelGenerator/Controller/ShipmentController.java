package dev.aloysius.ShippingLabelGenerator.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/v1/ship")
public class ShipmentController {
    public static final String CLIENT_ID = System.getenv("CLIENT_ID");

    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    public ShipmentController( OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    }

    @PostMapping("/standard")
    public void getShipment() throws IOException, InterruptedException {
        standardShippingApi();

    }
    @PostMapping("/third-party")
    public void getShipmentThirdParty() throws IOException, InterruptedException {
        thirdPartyBilling();

    }
    @GetMapping("/")
    public void getMultiPieceShipping() throws IOException, InterruptedException {
        multiPieceShipping();
    }
    public String printAsJson(String str) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Object jsonObject = mapper.readValue(str, Object.class);
        return mapper.writeValueAsString(jsonObject);
    }
    public static List<String> extractImageValues(String str) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(str);
        JsonNode packageResults = rootNode.path("ShipmentResponse").path("ShipmentResults").get("PackageResults");
        List<String> val = new ArrayList<>();
        for (JsonNode pack : packageResults){
            val.add(pack.path("ShippingLabel").get("GraphicImage").asText());
        }
        return val;

    }
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
    public String extractTrackingNumber(String str) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(str);
        JsonNode packageResults = rootNode.path("ShipmentResponse").path("ShipmentResults").get("PackageResults");
        String val = null;
        for (JsonNode pack : packageResults){
            val = pack.get("TrackingNumber").asText();
        }
        return val;
    }
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
    public void standardShippingApi() throws IOException, InterruptedException {
        PrintWriter writer = new PrintWriter(new FileWriter("Shipment_label.js"));
        var httpClient = HttpClient.newBuilder().build();

        var payload = String.join("\n"
                , "{"
                , " \"ShipmentRequest\": {"
                , "  \"Request\": {"
                , "   \"SubVersion\": \"1801\","
                , "   \"RequestOption\": \"nonvalidate\","
                , "   \"TransactionReference\": {"
                , "    \"CustomerContext\": \"\""
                , "   }"
                , "  },"
                , "  \"Shipment\": {"
                , "   \"Description\": \"Ship WS test\","
                , "   \"Shipper\": {"
                , "    \"Name\": \"ShipperName\","
                , "    \"AttentionName\": \"ShipperZs Attn Name\","
                , "    \"TaxIdentificationNumber\": \"123456\","
                , "    \"Phone\": {"
                , "     \"Number\": \"1115554758\","
                , "     \"Extension\": \" \""
                , "    },"
                , "    \"ShipperNumber\": \"AV6010 \","
                , "    \"FaxNumber\": \"8002222222\","
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"2311 York Rd\""
                , "     ],"
                , "     \"City\": \"Timonium\","
                , "     \"StateProvinceCode\": \"MD\","
                , "     \"PostalCode\": \"21093\","
                , "     \"CountryCode\": \"US\""
                , "    }"
                , "   },"
                , "   \"ShipTo\": {"
                , "    \"Name\": \"Happy Dog Pet Supply\","
                , "    \"AttentionName\": \"1160b_74\","
                , "    \"Phone\": {"
                , "     \"Number\": \"9225377171\""
                , "    },"
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"123 Main St\""
                , "     ],"
                , "     \"City\": \"timonium\","
                , "     \"StateProvinceCode\": \"MD\","
                , "     \"PostalCode\": \"21030\","
                , "     \"CountryCode\": \"US\""
                , "    },"
                , "    \"Residential\": \" \""
                , "   },"
                , "   \"ShipFrom\": {"
                , "    \"Name\": \"T and T Designs\","
                , "    \"AttentionName\": \"1160b_74\","
                , "    \"Phone\": {"
                , "     \"Number\": \"1234567890\""
                , "    },"
                , "    \"FaxNumber\": \"1234567890\","
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"2311 York Rd\""
                , "     ],"
                , "     \"City\": \"Alpharetta\","
                , "     \"StateProvinceCode\": \"GA\","
                , "     \"PostalCode\": \"30005\","
                , "     \"CountryCode\": \"US\""
                , "    }"
                , "   },"
                , "   \"PaymentInformation\": {"
                , "    \"ShipmentCharge\": {"
                , "     \"Type\": \"01\","
                , "     \"BillShipper\": {"
                , "      \"AccountNumber\": \" AV6010\""
                , "     }"
                , "    }"
                , "   },"
                , "   \"Service\": {"
                , "    \"Code\": \"03\","
                , "    \"Description\": \"Express\""
                , "   },"
                , "   \"Package\": {"
                , "    \"Description\": \" \","
                , "    \"Packaging\": {"
                , "     \"Code\": \"02\","
                , "     \"Description\": \"Nails\""
                , "    },"
                , "    \"Dimensions\": {"
                , "     \"UnitOfMeasurement\": {"
                , "      \"Code\": \"IN\","
                , "      \"Description\": \"Inches\""
                , "     },"
                , "     \"Length\": \"10\","
                , "     \"Width\": \"30\","
                , "     \"Height\": \"45\""
                , "    },"
                , "    \"PackageWeight\": {"
                , "     \"UnitOfMeasurement\": {"
                , "      \"Code\": \"LBS\","
                , "      \"Description\": \"Pounds\""
                , "     },"
                , "     \"Weight\": \"5\""
                , "    }"
                , "   }"
                , "  },"
                , "  \"LabelSpecification\": {"
                , "   \"LabelImageFormat\": {"
                , "    \"Code\": \"GIF\","
                , "    \"Description\": \"GIF\""
                , "   },"
                , "   \"HTTPUserAgent\": \"Mozilla/4.5\""
                , "  }"
                , " }"
                , "}"
        );

        HashMap<String, String> params = new HashMap<>();
        params.put("additionaladdressvalidation", "string");

        var query = params.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        var host = "https://wwwcie.ups.com";
        var version = "2403";
        var pathname = "/api/shipments/" + version + "/ship";
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .uri(URI.create(host + pathname + '?' + query))
                .header("Content-Type", "application/json")
                .header("transId", "string")
                .header("transactionSrc", "testing")
                .header("Authorization", "Bearer "+getToken())
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String s = printAsJson(response.body());
        writer.write(request.headers().toString());
        writer.println();
        writer.write("The Response to this api");
        writer.write(s);

        writer.close();
        String s1 = extractImageValue(response.body());
        String s2 = extractTrackingNumber(response.body());
        System.out.println(s2);
        //i need to so a base64 decoding of this string
        try{
            System.out.println(s1);
            byte[] imageBytes = Base64.getDecoder().decode(s1);
             FileOutputStream fos = new FileOutputStream("Shipping_label.jpg");
             fos.write(imageBytes);
             fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(response.body());
    }
    public void thirdPartyBilling() throws IOException, InterruptedException {
        PrintWriter writer = new PrintWriter(new FileWriter("Shipment_label.js"));
        var httpClient = HttpClient.newBuilder().build();

        var payload = String.join("\n"
                , "{"
                , " \"ShipmentRequest\": {"
                , "  \"Request\": {"
                , "   \"RequestOption\": \"nonvalidate\","
                , "   \"SubVersion\": \"1901\","
                , "   \"TransactionReference\": {"
                , "    \"CustomerContext\": \"\""
                , "   }"
                , "  },"
                , "  \"Shipment\": {"
                , "   \"Description\": \"Payments\","
                , "   \"Shipper\": {"
                , "    \"Name\": \"Shipper_name\","
                , "    \"AttentionName\": \"Shipper_name\","
                , "    \"CompanyDisplayableName\": \"Shipper_name\","
                , "    \"Phone\": {"
                , "     \"Number\": \"1234567890\","
                , "     \"Extension\": \"1234\""
                , "    },"
                , "    \"ShipperNumber\": \" \","
                , "    \"FaxNumber\": \"1234\","
                , "    \"EMailAddress\": \" \","
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"ShipperAddress\","
                , "      \"ShipperAddress\","
                , "      \"ShipperAddress\""
                , "     ],"
                , "     \"City\": \"01-222 Warszawa\","
                , "     \"StateProvinceCode\": \"GA\","
                , "     \"PostalCode\": \"1222\","
                , "     \"CountryCode\": \"PL\""
                , "    }"
                , "   },"
                , "   \"ShipTo\": {"
                , "    \"Name\": \"ShipToName\","
                , "    \"AttentionName\": \"ShipToName\","
                , "    \"CompanyDisplayableName\": \"ShipToName\","
                , "    \"Phone\": {"
                , "     \"Number\": \"1234567890\","
                , "     \"Extension\": \"1234\""
                , "    },"
                , "    \"FaxNumber\": \"1234\","
                , "    \"EMailAddress\": \" \","
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"ShipToAddress\","
                , "      \"ShipToAddress\","
                , "      \"ShipToAddress\""
                , "     ],"
                , "     \"City\": \"01-222 Warszawa\","
                , "     \"StateProvinceCode\": \"GA\","
                , "     \"PostalCode\": \"1222\","
                , "     \"CountryCode\": \"PL\""
                , "    }"
                , "   },"
                , "   \"ShipFrom\": {"
                , "    \"Name\": \"ShipFromName\","
                , "    \"AttentionName\": \"ShipFromName\","
                , "    \"CompanyDisplayableName\": \"ShipFromName\","
                , "    \"TaxIdentificationNumber\": \"1234\","
                , "    \"TaxIDType\": {"
                , "     \"Code\": \"EIN\","
                , "     \"Description\": \"EIN\""
                , "    },"
                , "    \"Phone\": {"
                , "     \"Number\": \"1234567890\","
                , "     \"Extension\": \"1234\""
                , "    },"
                , "    \"ShipFromAccountNumber\": \" \","
                , "    \"FaxNumber\": \"1234\","
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"ShipFromAddress\","
                , "      \"ShipFromAddress\","
                , "      \"ShipFromAddress\""
                , "     ],"
                , "     \"City\": \"01-222 Warszawa\","
                , "     \"StateProvinceCode\": \"GA\","
                , "     \"PostalCode\": \"1222\","
                , "     \"CountryCode\": \"PL\""
                , "    },"
                , "    \"EMailAddress\": \" \""
                , "   },"
                , "   \"PaymentInformation\": {"
                , "    \"ShipmentCharge\": {"
                , "     \"Type\": \"01\","
                , "     \"BillThirdParty\": {"
                , "      \"AccountNumber\": \" \","
                , "      \"Name\": \" \","
                , "      \"AttentionName\": \" \","
                , "      \"VatTaxID\": \"1234AB\","
                , "      \"TaxIDType\": \"01\","
                , "      \"CertifiedElectronicMail\": \"abc@123.123\","
                , "      \"InterchangeSystemCode\": \"SDI\","
                , "      \"SuppressPrintInvoiceIndicator\": \" \","
                , "      \"Address\": {"
                , "       \"PostalCode\": \"30005\","
                , "       \"CountryCode\": \"US\""
                , "      }"
                , "     }"
                , "    }"
                , "   },"
                , "   \"Service\": {"
                , "    \"Code\": \"011\","
                , "    \"Description\": \"Standard\""
                , "   },"
                , "   \"InvoiceLineTotal\": {"
                , "    \"CurrencyCode\": \"USD\","
                , "    \"MonetaryValue\": \"10\""
                , "   },"
                , "   \"NumOfPiecesInShipment\": \"1\","
                , "   \"USPSEndorsement\": \"5\","
                , "   \"CostCenter\": \"123\","
                , "   \"PackageID\": \"1\","
                , "   \"InformationSourceCode\": \"A3\","
                , "   \"ShipmentServiceOptions\": \"            \","
                , "   \"Package\": {"
                , "    \"Description\": \"IF\","
                , "    \"NumOfPieces\": \"10\","
                , "    \"Packaging\": {"
                , "     \"Code\": \"02\","
                , "     \"Description\": \"desc\""
                , "    },"
                , "    \"Dimensions\": {"
                , "     \"UnitOfMeasurement\": {"
                , "      \"Code\": \"CM\","
                , "      \"Description\": \"CM\""
                , "     },"
                , "     \"Length\": \"2\","
                , "     \"Width\": \"2\","
                , "     \"Height\": \"3\""
                , "    },"
                , "    \"PackageWeight\": {"
                , "     \"UnitOfMeasurement\": {"
                , "      \"Code\": \"KGS\","
                , "      \"Description\": \"LBS\""
                , "     },"
                , "     \"Weight\": \"50\""
                , "    }"
                , "   }"
                , "  },"
                , "  \"LabelSpecification\": {"
                , "   \"LabelImageFormat\": {"
                , "    \"Code\": \"GIF\","
                , "    \"Description\": \"GIF\""
                , "   }"
                , "  }"
                , " }"
                , "}"
        );

        HashMap<String, String> params = new HashMap<>();
        params.put("additionaladdressvalidation", "string");

        var query = params.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        var host = "https://wwwcie.ups.com";
        var version = "YOUR_version_PARAMETER";
        var pathname = "/api/shipments/" + version + "/ship";
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .uri(URI.create(host + pathname + '?' + query))
                .header("Content-Type", "application/json")
                .header("transId", "string")
                .header("transactionSrc", "testing")
                .header("Authorization", "Bearer "+getToken())
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        String s = printAsJson(response.body());
        writer.write(request.headers().toString());
        writer.println();
        writer.write("The Response to this api");
        writer.write(s);

        writer.close();
        String s1 = extractImageValue(response.body());
        //i need to so a base64 decoding of this string
        try{
            System.out.println(s1);
            byte[] imageBytes = Base64.getDecoder().decode(s1);
            FileOutputStream fos = new FileOutputStream("Shipping_label.jpg");
            fos.write(imageBytes);
            fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(response.body());
    }
    public void multiPieceShipping() throws IOException, InterruptedException {
        PrintWriter writer = new PrintWriter(new FileWriter("multi-Shipment_label.js"));
        var httpClient = HttpClient.newBuilder().build();

        var payload = String.join("\n"
                , "{"
                , " \"ShipmentRequest\": {"
                , "  \"Request\": {"
                , "   \"RequestOption\": \"nonvalidate\","
                , "   \"SubVersion\": \"1701\","
                , "   \"TransactionReference\": {"
                , "    \"CustomerContext\": \"\""
                , "   }"
                , "  },"
                , "  \"Shipment\": {"
                , "   \"Package\": ["
                , "    {"
                , "     \"PackageWeight\": {"
                , "      \"Weight\": \"50\","
                , "      \"UnitOfMeasurement\": {"
                , "       \"Description\": \"desc\","
                , "       \"Code\": \"LBS\""
                , "      }"
                , "     },"
                , "     \"Dimensions\": {"
                , "      \"Height\": \"2\","
                , "      \"Width\": \"2\","
                , "      \"Length\": \"02\","
                , "      \"UnitOfMeasurement\": {"
                , "       \"Description\": \"desc\","
                , "       \"Code\": \"IN\""
                , "      }"
                , "     },"
                , "     \"Packaging\": {"
                , "      \"Description\": \"desc\","
                , "      \"Code\": \"02\""
                , "     },"
                , "     \"Description\": \"desc\""
                , "    },"
                , "    {"
                , "     \"PackageWeight\": {"
                , "      \"Weight\": \"50\","
                , "      \"UnitOfMeasurement\": {"
                , "       \"Description\": \"desc\","
                , "       \"Code\": \"LBS\""
                , "      }"
                , "     },"
                , "     \"Dimensions\": {"
                , "      \"Height\": \"2\","
                , "      \"Width\": \"2\","
                , "      \"Length\": \"02\","
                , "      \"UnitOfMeasurement\": {"
                , "       \"Description\": \"desc\","
                , "       \"Code\": \"IN\""
                , "      }"
                , "     },"
                , "     \"Packaging\": {"
                , "      \"Description\": \"desc\","
                , "      \"Code\": \"02\""
                , "     },"
                , "     \"Description\": \"desc\""
                , "    },"
                , "    {"
                , "     \"Description\": \"desc\","
                , "     \"Packaging\": {"
                , "      \"Code\": \"02\","
                , "      \"Description\": \"desc\""
                , "     },"
                , "     \"Dimensions\": {"
                , "      \"UnitOfMeasurement\": {"
                , "       \"Code\": \"IN\","
                , "       \"Description\": \"desc\""
                , "      },"
                , "      \"Length\": \"02\","
                , "      \"Width\": \"2\","
                , "      \"Height\": \"2\""
                , "     },"
                , "     \"PackageWeight\": {"
                , "      \"UnitOfMeasurement\": {"
                , "       \"Code\": \"LBS\","
                , "       \"Description\": \"desc\""
                , "      },"
                , "      \"Weight\": \"50\""
                , "     }"
                , "    }"
                , "   ],"
                , "   \"Description\": \"UPS Premier\","
                , "   \"Shipper\": {"
                , "    \"Name\": \"ShipperName\","
                , "    \"AttentionName\": \"GA\","
                , "    \"CompanyDisplayableName\": \"GA\","
                , "    \"TaxIdentificationNumber\": \"12345\","
                , "    \"Phone\": {"
                , "     \"Number\": \"1234567890\","
                , "     \"Extension\": \"12\""
                , "    },"
                , "    \"ShipperNumber\": \"AV6010\","
                , "    \"FaxNumber\": \"2134\","
                , "    \"EMailAddress\": \" \","
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"address\""
                , "     ],"
                , "     \"City\": \"Alpharetta\","
                , "     \"StateProvinceCode\": \"GA\","
                , "     \"PostalCode\": \"30005\","
                , "     \"CountryCode\": \"US\""
                , "    }"
                , "   },"
                , "   \"ShipTo\": {"
                , "    \"Name\": \"ship\","
                , "    \"AttentionName\": \"GA\","
                , "    \"CompanyDisplayableName\": \"GA\","
                , "    \"TaxIdentificationNumber\": \"1234\","
                , "    \"Phone\": {"
                , "     \"Number\": \"1234567890\","
                , "     \"Extension\": \"12\""
                , "    },"
                , "    \"FaxNumber\": \"1234\","
                , "    \"EMailAddress\": \" \","
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"AddressLine\""
                , "     ],"
                , "     \"City\": \"Alpharetta\","
                , "     \"StateProvinceCode\": \"GA\","
                , "     \"PostalCode\": \"30005\","
                , "     \"CountryCode\": \"US\","
                , "     \"ResidentialAddressIndicator\": \"Y\""
                , "    }"
                , "   },"
                , "   \"ShipFrom\": {"
                , "    \"Name\": \"ship\","
                , "    \"AttentionName\": \"GA\","
                , "    \"CompanyDisplayableName\": \"ShipFrom_CompanyDisplayableName\","
                , "    \"TaxIdentificationNumber\": \"5555555555\","
                , "    \"Phone\": {"
                , "     \"Number\": \"1234567890\","
                , "     \"Extension\": \"12\""
                , "    },"
                , "    \"FaxNumber\": \"5555555555\","
                , "    \"Address\": {"
                , "     \"AddressLine\": ["
                , "      \"AddressLine\""
                , "     ],"
                , "     \"City\": \"Alpharetta\","
                , "     \"StateProvinceCode\": \"GA\","
                , "     \"PostalCode\": \"30005\","
                , "     \"CountryCode\": \"US\""
                , "    },"
                , "    \"EMailAddress\": \" \""
                , "   },"
                , "   \"PaymentInformation\": {"
                , "    \"ShipmentCharge\": {"
                , "     \"Type\": \"01\","
                , "     \"BillShipper\": {"
                , "      \"AccountNumber\": \"AV6010\""
                , "     }"
                , "    }"
                , "   },"
                , "   \"Service\": {"
                , "    \"Code\": \"01\","
                , "    \"Description\": \"desc\""
                , "   }"
                , "  },"
                , "  \"LabelSpecification\": {"
                , "   \"LabelImageFormat\": {"
                , "    \"Code\": \"ZPL\","
                , "    \"Description\": \"desc\""
                , "   },"
                , "   \"HTTPUserAgent\": \"Mozilla/4.5\","
                , "   \"LabelStockSize\": {"
                , "    \"Height\": \"6\","
                , "    \"Width\": \"4\""
                , "   }"
                , "  }"
                , " }"
                , "}"
        );

        HashMap<String, String> params = new HashMap<>();
        params.put("additionaladdressvalidation", "string");

        var query = params.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        var host = "https://wwwcie.ups.com";
        var version = "v2403";
        var pathname = "/api/shipments/" + version + "/ship";
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .uri(URI.create(host + pathname + '?' + query))
                .header("Content-Type", "application/json")
                .header("transId", "string")
                .header("transactionSrc", "testing")
                .header("Authorization", "Bearer "+getToken())
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String s = printAsJson(response.body());
        writer.write(request.headers().toString());
        writer.println();
        writer.write("The Response to this api");
        writer.println();
        writer.write(s);

        writer.close();
        var images = extractImageValues(response.body());
        images.forEach(System.out::println);
    }
}


