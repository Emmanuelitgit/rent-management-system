package com.rent_management_system.Payment;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService  paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initialize")
    public PaymentResponse initializeTransaction(@Valid @RequestBody PaymentRequest paymentRequest){
        return paymentService.initializeTransaction(paymentRequest);
    }

    @GetMapping("/verify/{reference}")
    public Object verifyTransaction(@PathVariable String reference){
        return paymentService.verifyTransaction(reference);
    }
}