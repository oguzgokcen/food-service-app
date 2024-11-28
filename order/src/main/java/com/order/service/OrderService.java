package com.order.service;

import com.order.client.ProductClient;
import com.order.client.RestaurantClient;
import com.order.client.UserClient;
import com.order.constants.OrderConstants;
import com.order.dto.OrderDto;
import com.order.entity.Order;
import com.order.entity.Product;
import com.order.exception.OrderNotCreatedException;
import com.order.exception.OrderNotFoundException;
import com.order.exception.RestaurantNotExistException;
import com.order.model.CustomResponseMessages;
import com.order.model.request.OrderRequest;
import com.order.model.request.ProductRequest;
import com.order.model.response.OrderResponse;
import com.order.model.response.ProductResponse;
import com.order.model.response.RestaurantResponse;
import com.order.model.response.UserResponse;
import com.order.populator.OrderDtoPopulator;
import com.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
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
    private final ProductService productService;
    private final OrderDtoPopulator orderDtoPopulator;

    public OrderDto createOrder(OrderRequest orderRequest, String userEmail) throws RestaurantNotExistException, OrderNotCreatedException {
        if (restaurantClient.existById(orderRequest.getRestaurantId()))
            throw new RestaurantNotExistException(CustomResponseMessages.RESTAURANT_DOES_NOT_EXISTS);
        List<ProductResponse> productResponse = productClient.getAllById(orderRequest.getProductList().stream().map(ProductRequest::getProductId).toList());
        UserResponse userResponse = userClient.getUser(userEmail);
        if (!checkOrderPrice(productResponse, orderRequest.getTotalPrice(), orderRequest.getProductList()))
            throw new OrderNotCreatedException(CustomResponseMessages.ORDER_NOT_CREATED);
        else if(!checkStock(productResponse,orderRequest.getProductList())){
            throw new OrderNotCreatedException(CustomResponseMessages.STOCK_IS_NOT_ENOUGH);
        }
        Order order = Order.builder()
                .restaurantId(orderRequest.getRestaurantId())
                .productList(orderRequest.getProductList().stream().map(e -> Product.builder().productId(e.getProductId()).count(e.getCount()).build()).toList())
                .userId(userResponse.getId())
                .totalPrice(orderRequest.getTotalPrice())
                .build();
        order.setProductList(orderRequest.getProductList().stream().map(e -> Product.builder().order(order).productId(e.getProductId()).count(e.getCount()).build()).toList());
        productClient.removeFromStock(orderRequest.getProductList());
        return orderDtoPopulator.populate(orderRepository.save(order));
    }

    private boolean checkStock(List<ProductResponse> productResponse, List<ProductRequest> productList) {
        return productList.stream().anyMatch(e-> productResponse.stream().anyMatch(x-> !e.getProductId().equals(x.getId()) || e.getCount() <= x.getStock()));
    }

    private boolean checkOrderPrice(List<ProductResponse> productResponse, Long totalPrice, List<ProductRequest> productList) {
        AtomicLong temp = new AtomicLong();
        productResponse.forEach(e -> temp.addAndGet(e.getProductPrice() * productList.stream().filter(x -> x.getProductId().equals(e.getId())).findFirst().orElseThrow(() -> new RuntimeException("Not Found")).getCount()));
        return (temp.get() + OrderConstants.deliveryChange) == totalPrice;
    }

    public OrderResponse getOrderByOrderId(Long orderId, String userEmail) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(CustomResponseMessages.ORDER_NOT_FOUND));
        RestaurantResponse restaurantResponse = restaurantClient.getById(order.getRestaurantId());
        List<ProductResponse> productResponseList = productClient.getAllById(order.getProductList().stream().map(Product::getProductId).toList());
        UserResponse userResponse = userClient.getUser(userEmail);

        setCount(productResponseList, order);
        return OrderResponse.builder()
                .orderId(orderId)
                .restaurantResponse(restaurantResponse)
                .productResponseList(productResponseList)
                .totalPrice(order.getTotalPrice())
                .userResponse(userResponse)
                .isPaid(order.isPaid())
                .build();
    }

    public List<OrderResponse> getOrdersOfRestaurantById(Long restaurantId) {
        List<Order> orderList = orderRepository.findOrdersByRestaurantId(restaurantId);

        return getOrderResponses(orderList);
    }

    private static void setCount(List<ProductResponse> productResponseList, Order order) {
        productResponseList.forEach(e -> {
            order.getProductList().forEach(x -> {
                if (e.getId().equals(x.getProductId())) e.setStock(x.getCount());
            });
        });
    }

    public Long getOrderCountByProductId(Long productId) {
        return productService.getOrderCountByProductId(productId);
    }

    public List<Long> getProductIdsOfOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return order.getProductList().stream().map(Product::getProductId).collect(Collectors.toList());
    }

    public void setOrderAsPaid(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(CustomResponseMessages.ORDER_NOT_FOUND));
        order.setPaid(true);
        orderRepository.save(order);
    }

    public boolean isOrderPaid(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(CustomResponseMessages.ORDER_NOT_FOUND));
        return order.isPaid();
    }

    public List<OrderResponse> getPaidOrdersOfRestaurant(Long restaurantId) {
        return getOrderResponses(orderRepository.findOrdersByRestaurantId(restaurantId).stream().filter(Order::isPaid).toList());
    }

    private List<OrderResponse> getOrderResponses(List<Order> orderList) {
        return orderList.stream().map(e -> {
                    OrderResponse orderResponse = OrderResponse.builder()
                            .orderId(e.getId())
                            .restaurantResponse(restaurantClient.getById(e.getRestaurantId()))
                            .productResponseList(productClient.getAllById(e.getProductList().stream().map(Product::getId).toList()))
                            .totalPrice(e.getTotalPrice())
                            .build();
                    setCount(orderResponse.getProductResponseList(), e);
                    return orderResponse;
                }
        ).toList();
    }

    public List<Long> getUsersOrderIds(Long userId){
        return orderRepository.findByUserId(userId).stream().map(Order::getId).collect(Collectors.toList());
    }
}
