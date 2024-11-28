package com.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeductRequest {
    private String cardNumber;
    private String cvv;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private Long amount;
}
