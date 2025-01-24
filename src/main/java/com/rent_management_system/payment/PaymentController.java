package com.rent_management_system.payment;

import com.rent_management_system.response.ResponseHandler;
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
    @PostMapping("/initialize/{rentInfoId}")
    public PaymentResponse initializeTransaction(@Valid @RequestBody PaymentRequest paymentRequest, @PathVariable Long rentInfoId){
        return paymentService.initializeTransaction(paymentRequest, rentInfoId);
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
    public ResponseEntity<HttpStatus> handleWebhook(@RequestBody Map<String, Object> payload) {
        log.info("DATA RECEIVED IN CONTROLLER:{}", payload);

        String event = (String) payload.get("event");

        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        if ("charge.success".equals(event)) {
            paymentService.processSuccessfulPayment(data);
        }

        return new ResponseEntity<>(HttpStatus.OK);
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