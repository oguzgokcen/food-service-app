package com.review.client;

import com.review.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "users", url = "http://localhost:8076/auth")
public interface UserClient {
    @GetMapping("/email/{email}")
    UserResponse getUser(@RequestParam String email);
}
