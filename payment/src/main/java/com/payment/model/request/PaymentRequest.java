package com.payment.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long orderId;
    private Long cardNumber;
    private Long cvv;
    private Long cardExpiryMonth;
    private Long cardExpiryYear;
}