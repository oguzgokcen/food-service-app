package com.order.client;

import com.order.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",url = "${application.config.users-url}")
public interface UserClient {
    @GetMapping("/{email}")
    public UserResponse getUser(@PathVariable String email);
}
