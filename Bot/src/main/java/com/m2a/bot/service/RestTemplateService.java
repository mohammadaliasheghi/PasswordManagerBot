package com.m2a.bot.service;

import com.m2a.bot.model.ApiResponse;
import com.m2a.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class RestTemplateService {

    @Value("${app.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ApiResponse callApiWithToken(String endPoint, String token, Object requestData, HttpMethod method) {
        try {
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(Constant.AUTHORIZATION, token);

            // Create request entity with headers and body
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestData, headers);

            // Make the API call
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + endPoint,
                    method, // POST, PUT, DELETE as needed
                    requestEntity,
                    String.class
            );

            // Return the response body
            return ApiResponse.builder()
                    .status(response.getStatusCode().value())
                    .message(Objects.requireNonNull(response.getBody()))
                    .data(response.getBody())
                    .build();
        } catch (HttpClientErrorException e) {
            return ApiResponse.builder()
                    .status(400)
                    .message(e.getMessage())
                    .data(HttpStatus.BAD_REQUEST.toString())
                    .build();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .status(500)
                    .message(e.getMessage())
                    .data(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .build();
        }
    }

    public ApiResponse getDataWithToken(String endPoint, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(Constant.AUTHORIZATION, token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + endPoint,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            // Return the response body
            return ApiResponse.builder()
                    .status(response.getStatusCode().value())
                    .message(Objects.requireNonNull(response.getBody()))
                    .data(response.getBody())
                    .build();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .status(500)
                    .message(e.getMessage())
                    .data(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .build();
        }
    }
}