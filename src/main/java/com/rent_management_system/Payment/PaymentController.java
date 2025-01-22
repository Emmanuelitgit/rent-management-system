package com.rent_management_system.Payment;

import com.rent_management_system.Response.ResponseHandler;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService  paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for initializing payments
     * @param: paymentRequest object
     * @date 21-01-2025
     * @return PaymentResponse
     */
    @PostMapping("/initialize")
    public PaymentResponse initializeTransaction(@Valid @RequestBody PaymentRequest paymentRequest){
        return paymentService.initializeTransaction(paymentRequest);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for verifying transaction status through payStack verify api
     * @param: reference
     * @date 21-01-2025
     * @return Object
     */
    @GetMapping("/verify/{reference}")
    public Object verifyTransaction(@PathVariable String reference){
        return paymentService.verifyTransaction(reference);
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for verifying transaction status through payStack webhook
     * @param: payload Object coming from payStack
     * @date 21-01-2025
     * @return responseEntity object
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String event = (String) payload.get("event");

            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            log.info("DATA RECEIVED IN CONTROLLER:{}", data);
            if ("charge.success".equals(event)) {
                paymentService.processSuccessfulPayment(data);
            }

            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to fetch all payment list
     * @date 22-01-2025
     * @return responseEntity object
     */
    @GetMapping("/payment-list")
    public ResponseEntity<Object> getPaymentList(){
        List<PaymentDTO> payments = paymentService.getPaymentList();
        return ResponseHandler.responseBuilder("All payment list", payments, HttpStatus.OK);
    }

}