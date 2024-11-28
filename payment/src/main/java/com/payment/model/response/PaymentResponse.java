package com.payment.model.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Date paymentDate;
    private Long orderId;
    private List<ProductResponse> productResponseList;
    private Long totalPrice;
    private UserResponse userResponse;
}
