package com.rent_management_system.Payment;//package com.rent_management_system.Payment;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/payments")
//public class WebHookController {
//
//    @PostMapping("/webhook")
//    public void handleWebhook(@RequestBody Map<String, Object> payload) {
//        log.info("Status{}", payload);
//    }
//}


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class WebHookController {

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            // Extract event type
            String event = (String) payload.get("event");

            // Extract data object
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            if ("charge.success".equals(event)) {
                processSuccessfulPayment(data);
            }

            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

    private void processSuccessfulPayment(Map<String, Object> data) {
        String reference = (String) data.get("reference");
        Integer amount = (Integer) data.get("amount");
        String currency = (String) data.get("currency");
        String status = (String) data.get("status");
        String paidAt = (String) data.get("paid_at");

        // Extract customer details
        Map<String, Object> customer = (Map<String, Object>) data.get("customer");
        String email = (String) customer.get("email");

        // Extract payment authorization details
        Map<String, Object> authorization = (Map<String, Object>) data.get("authorization");
        String authorizationCode = (String) authorization.get("authorization_code");

        // Process the payment (e.g., update database, send confirmation email, etc.)
        System.out.println("Payment successful for reference: " + reference);
        System.out.println("Amount: " + amount + " " + currency);
        System.out.println("Paid at: " + paidAt);
        System.out.println("Customer Email: " + email);
        System.out.println("Authorization Code: " + authorizationCode);

        // TODO: Save transaction details in database
    }
}