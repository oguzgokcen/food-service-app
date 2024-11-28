package com.payment.client;

import com.payment.model.request.DeductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users-service",url = "${application.config.users-url}")
public interface UserClient {
    @PostMapping("/users/cards/deduct")
    Long deductMoneyFromCardAndGetUserId(@RequestBody DeductRequest deductRequest, @RequestHeader("userEmail") String userEmail);
}