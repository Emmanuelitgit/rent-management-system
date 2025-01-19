package com.rent_management_system.Payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Value("${paystack.secret-key}")
    private String secretKey;

    @Value("${paystack.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public PaymentResponse initializeTransaction(PaymentRequest request) {
        String url = baseUrl + "/transaction/initialize";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(url, entity, PaymentResponse.class);

        return response.getBody();
    }

    public Object verifyTransaction(String reference) {
        String url = baseUrl + "/transaction/verify/" + reference;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        return response.getBody();
    }

}
