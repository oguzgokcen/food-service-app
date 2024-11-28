package com.review.dto;

public record CreateReviewRequest(long orderId, String reviewBody, int star) {
}
