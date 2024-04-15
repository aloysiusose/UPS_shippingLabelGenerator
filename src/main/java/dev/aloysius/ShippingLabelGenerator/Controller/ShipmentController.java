package dev.aloysius.ShippingLabelGenerator.Controller;

import dev.aloysius.ShippingLabelGenerator.Domain.RequestObject;
import dev.aloysius.ShippingLabelGenerator.Domain.ResponseObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
@RestController
@RequestMapping("/api/v1/ship")
public class ShipmentController {

    private final RestClient restClient;
    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    public ShipmentController(RestClient restClient, OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        this.restClient = restClient;
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    }

    @PostMapping("/")
    public void getShipment() throws IOException, InterruptedException {
        testApi();

//        ResponseEntity<ResponseObject> entity = restClient.post()
//                .uri("https://wwwcie.ups.com/api/shipments/v2403/ship")
//                .headers(c -> c.setBearerAuth(this.getToken()))
//                .contentType(APPLICATION_JSON)
//                .body(HttpRequest.BodyPublishers.ofString(object.toString()))
//                .retrieve()
//                .toEntity(ResponseObject.class);
//        return entity.getBody();
    }

    private String getToken(){
        OAuth2AuthorizeRequest re = OAuth2AuthorizeRequest.withClientRegistrationId("ups")
                .principal("fAsQ0NN0uvBB8kk5VhC1fo4aXApXAZYIsV1TvfkcVxGFBPAq")
                .build();

        OAuth2AuthorizedClient authorize = oAuth2AuthorizedClientManager.authorize(re);
        assert authorize != null;
        String tokenValue = authorize.getAccessToken().getTokenValue();
        System.out.println(tokenValue);
        return tokenValue;
    }
    public void testApi() throws IOException, InterruptedException {
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
                , "    \"ShipperNumber\": \" \","
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
                , "      \"AccountNumber\": \" \""
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

        System.out.println(response.body());
    }
}
