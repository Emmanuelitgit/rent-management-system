package com.rent_management_system.Payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class WebHookController {

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody Map<String, Object> payload) {
        // Log or process the webhook event here
        System.out.println("Received webhook: " + payload);
        log.info("Received webhook{}", payload);
    }
}