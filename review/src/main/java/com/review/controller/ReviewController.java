package com.review.controller;

import com.review.dto.CreateReviewRequest;
import com.review.dto.OrderReviewDTO;
import com.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public void saveReview(@RequestBody CreateReviewRequest reviewRequest) {
        reviewService.saveReview(reviewRequest);
    }

    @GetMapping("/rating")
    public double getProductRatingById(@RequestParam Long productId) {
        return reviewService.getProductRatingById(productId);
    }

    @GetMapping("/user")
    public List<OrderReviewDTO> getUsersReviews(@RequestHeader String userEmail){
        return reviewService.getUsersReviews(userEmail);
    }
}