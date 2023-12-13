package tacos.authorization;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class OAuth2CallbackController {

    private static final Logger log = LoggerFactory.getLogger(OAuth2CallbackController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String clientId = "taco-admin-client";
    private final String clientSecret = "secret";
    private final String tokenEndpoint = "http://localhost:9000/oauth2/token";

    @GetMapping("/callback")
    public ResponseEntity<?> handleOAuth2Callback(@RequestParam("code") String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", authorizationCode);
        map.add("redirect_uri", "http://127.0.0.1:9000/callback");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            // Check if the response body is not empty before attempting to parse
            if (!e.getResponseBodyAsString().isEmpty()) {
                try {
                    Map<String, Object> errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
                    String errorDetail = (String) errorResponse.getOrDefault("error_description", "No error description provided.");
                    log.error("Detailed error: " + errorDetail);
                } catch (Exception jsonException) {
                    log.error("Error parsing JSON response: " + jsonException.getMessage());
                }
            } else {
                log.error("Error response body is empty.");
            }
            return ResponseEntity.status(e.getStatusCode()).body("Error during token exchange: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("RestClientException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during token exchange: " + e.getMessage());
        }
    }
}
