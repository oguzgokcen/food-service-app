package com.payment.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeductRequest {
    private Long cardNumber;
    private Long cvv;
    private Long cardExpiryMonth;
    private Long cardExpiryYear;
    private Long amount;
}
