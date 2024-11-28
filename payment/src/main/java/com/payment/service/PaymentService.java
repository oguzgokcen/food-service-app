package com.payment.service;

import com.payment.client.OrderClient;
import com.payment.client.UserClient;
import com.payment.entity.Payment;
import com.payment.model.request.DeductRequest;
import com.payment.model.request.PaymentRequest;
import com.payment.model.response.MessageResponse;
import com.payment.model.response.OrderResponse;
import com.payment.model.response.PaymentResponse;
import com.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final UserClient userClient;

    public ResponseEntity<MessageResponse> makePayment(PaymentRequest paymentRequest, String userEmail) {
        OrderResponse orderResponse = orderClient.getOrderByOrderId(paymentRequest.getOrderId(), userEmail);
        if (orderResponse.isPaid()) return ResponseEntity.internalServerError().body(new MessageResponse("Order already paid"));
        try {
            Long userId = userClient.deductMoneyFromCardAndGetUserId(DeductRequest.
                    builder()
                    .cardNumber(paymentRequest.getCardNumber())
                    .cvv(paymentRequest.getCvv())
                    .cardExpiryMonth(paymentRequest.getCardExpiryMonth())
                    .cardExpiryYear(paymentRequest.getCardExpiryYear())
                    .amount(orderResponse.getTotalPrice())
                    .build(), userEmail);

            Payment payment = Payment.builder().paymentDate(new Date()).userId(userId).orderId(orderResponse.getOrderId()).build();
            orderClient.setOrderAsPaid(paymentRequest.getOrderId());
            paymentRepository.save(payment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.ok(new MessageResponse("Payment done successfully"));
    }

    public List<PaymentResponse> getPaymentsOfRestaurant(String restaurantId) {
        List<OrderResponse> orderResponseList = orderClient.getPaidOrdersOfRestaurant(restaurantId);
        return orderResponseList.stream().map(e -> {
            Payment payment = paymentRepository.findPaymentByOrderId(e.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
            return PaymentResponse.builder()
                    .userResponse(e.getUserResponse())
                    .totalPrice(e.getTotalPrice())
                    .productResponseList(e.getProductResponseList())
                    .orderId(payment.getOrderId())
                    .paymentDate(payment.getPaymentDate()).build();
        }).toList();
    }
}
