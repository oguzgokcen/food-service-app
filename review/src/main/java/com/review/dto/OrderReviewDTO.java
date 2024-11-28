package com.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class OrderReviewDTO {
    private Long orderId;
    private String reviewBody;
    private int star;
}
