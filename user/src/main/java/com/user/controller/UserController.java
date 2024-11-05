package com.user.controller;

import com.user.dto.LoginRequest;
import com.user.dto.RegisterRequest;
import com.user.entity.UserCredential;
import com.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/hello")
    public String hello(){
        return "hello from user";
    }

    @PostMapping("/register")
    public String addNewUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return service.saveUser(registerRequest);
    }

    @PostMapping("/login")
    public String getToken(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        return service.generateToken(loginRequest.getEmail());
    }

    @GetMapping("/validate")
    public void validateToken(@RequestHeader("Authorization") String authHeader) {
        service.validateToken(authHeader);
    }
}
