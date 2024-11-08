package com.order.service;

import com.order.client.ProductClient;
import com.order.client.RestaurantClient;
import com.order.client.UserClient;
import com.order.constants.OrderConstants;
import com.order.entity.Order;
import com.order.entity.ProductId;
import com.order.model.CustomResponseMessages;
import com.order.model.ResponseMessage;
import com.order.model.request.OrderRequest;
import com.order.model.response.CategoryResponse;
import com.order.model.response.ProductResponse;
import com.order.model.response.RestaurantResponse;
import com.order.model.response.UserResponse;
import com.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantClient restaurantClient;
    private final ProductClient productClient;
    private final UserClient userClient;
    public ResponseEntity<ResponseMessage> createOrder(OrderRequest orderRequest) {
        RestaurantResponse restaurantResponse = restaurantClient.getById(orderRequest.getRestaurantId());
        List<ProductResponse> productResponse = productClient.getAllById(orderRequest.getProductIdList());
//        UserResponse userResponse = userClient.getUser("");
        if(!checkOrderPrice(productResponse,orderRequest.getTotalPrice())) return ResponseEntity.internalServerError().body(new ResponseMessage(CustomResponseMessages.ORDER_NOT_CREATED, HttpStatus.INTERNAL_SERVER_ERROR));
        Order order = Order.builder()
                .restaurantId(orderRequest.getRestaurantId())
                .productIdList(orderRequest.getProductIdList().stream().map(ProductId::new).collect(Collectors.toSet()))
                .userId(1L)//userResponse.getId())
                .totalPrice(orderRequest.getTotalPrice())
                .build();
        orderRepository.save(order);

        return ResponseEntity.ok(new ResponseMessage(CustomResponseMessages.ORDER_CREATED, HttpStatus.OK));
    }

    private boolean checkOrderPrice(List<ProductResponse> productResponse,Long totalPrice) {
        AtomicLong temp = new AtomicLong();
        productResponse.forEach(e-> temp.addAndGet(e.getProductPrice()));
        return (temp.get() + OrderConstants.deliveryChange) == totalPrice;
    }
}
