package com.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String reviewBody;
    private int star;

    public OrderReview(Long orderId, String reviewBody, int star) {
        this.orderId = orderId;
        this.reviewBody = reviewBody;
        this.star = star;
    }
}
