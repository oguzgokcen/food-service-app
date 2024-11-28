package com.review.service;

import com.review.client.OrderClient;
import com.review.client.UserClient;
import com.review.dto.CreateReviewRequest;
import com.review.dto.OrderReviewDTO;
import com.review.dto.UserResponse;
import com.review.entity.OrderReview;
import com.review.entity.ProductReview;
import com.review.repository.OrderReviewRepository;
import com.review.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final OrderReviewRepository repository;
    private final ProductReviewRepository productReviewRepository;
    private final OrderClient orderClient;
    private final UserClient userClient;

    public void saveReview(CreateReviewRequest reviewRequest) {
        if (!controlOrderIsPaid(reviewRequest.orderId()))
            throw new RuntimeException("order is not paid");

        Optional<OrderReview> byOrderId = repository.findByOrderId(reviewRequest.orderId());
        if(byOrderId.isPresent())
            throw new RuntimeException("order already rated");

        List<Long> productIds = orderClient.getProductIds(reviewRequest.orderId());
        for (int i = 0; i < productIds.size(); i++) {
            Optional<ProductReview> productReview = productReviewRepository.findById(productIds.get(i));
            if (productReview.isEmpty())
                productReviewRepository.save(new ProductReview(productIds.get(i), reviewRequest.star(), 1));
            else {
                int reviewTimes = productReview.get().getReviewTimesOfProduct();
                double averageStar = productReview.get().getProductAverageStar();
                double totalStar = reviewTimes * averageStar + reviewRequest.star();
                reviewTimes += 1;
                double lastAverageStar = totalStar / reviewTimes;
                productReview.get().setReviewTimesOfProduct(reviewTimes);
                productReview.get().setProductAverageStar(lastAverageStar);
                productReviewRepository.save(productReview.get());
            }
        }
        repository.save(new OrderReview(reviewRequest.orderId(), reviewRequest.reviewBody(), reviewRequest.star()));
    }


    public double getProductRatingById(Long productId) {
        Optional<ProductReview> productReview = productReviewRepository.findById(productId);
        return productReview.map(ProductReview::getProductAverageStar).orElse(0.0);
    }

    private boolean controlOrderIsPaid(long orderId) {
        return orderClient.isOrderPaid(orderId);
    }

    public List<OrderReviewDTO> getUsersReviews(String email){
        List<OrderReviewDTO> orderReviewDTOList = new ArrayList<>();
       UserResponse userResponse=  userClient.getUser(email);
       long userId = userResponse.getId();
       List<Long> orderIds =  orderClient.getUsersOrderIds(userId);
       if(orderIds.isEmpty()){
           return null;
       }

       for(int i =0; i< orderIds.size() ; i++){
          Optional<OrderReview> optionalOrderReview =  repository.findByOrderId(orderIds.get(i));
          OrderReview orderReview = null;
          if(optionalOrderReview.isPresent()){
              orderReview = optionalOrderReview.get();
              orderReviewDTOList.add(new OrderReviewDTO(orderReview.getOrderId(),orderReview.getReviewBody(),orderReview.getStar()));
          }
       }
        return orderReviewDTOList;
    }
}
