package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.service.Auth0TokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class Auth0TokenServiceImpl implements Auth0TokenService {

    @Value("${AUTH0_DOMAIN}")
    private String domain;

    @Value("${AUTH0_API_CLIENT_ID}")
    private String apiClientID;

    @Value("${AUTH0_API_CLIENT_SECRET}")
    private String apiClientSecret;

    @Value("${AUTH0_API_AUDIENCE}")
    private String apiAudience;

    public String getAuth0Token() {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String requestBody = "grant_type=client_credentials&client_id=" + apiClientID +
                    "&client_secret=" + apiClientSecret +
                    "&audience=" + apiAudience;

            RequestBody body = RequestBody.create(mediaType, requestBody);

            Request request = new Request.Builder()
                    .url("https://" + domain + "/oauth/token")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            String token = jsonNode.get("access_token").asText();

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
