package com.payment.controller;

import com.payment.model.request.PaymentRequest;
import com.payment.model.response.MessageResponse;
import com.payment.model.response.PaymentResponse;
import com.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentApi {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<MessageResponse> makePayment(@RequestBody PaymentRequest paymentRequest, @RequestHeader String userEmail) {
        return paymentService.makePayment(paymentRequest, userEmail);
    }

    @GetMapping("/restaurants/{restaurantId}")
    public List<PaymentResponse>  getPaymentsOfRestaurant(@PathVariable String restaurantId){
        return paymentService.getPaymentsOfRestaurant(restaurantId);
    }
}