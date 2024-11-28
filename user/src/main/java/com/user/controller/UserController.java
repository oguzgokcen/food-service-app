package com.user.controller;

import com.user.dto.*;
import com.user.exception.CardNotFoundException;
import com.user.exception.InsufficientBalanceException;
import com.user.exception.MissMatchException;
import com.user.exception.UserNotFoundException;
import com.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public MessageResponse addNewUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return service.saveUser(registerRequest);
    }

    @PostMapping("/login")
    public String getToken(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        return service.generateToken(loginRequest.getEmail());
    }

    @GetMapping("/validate")
    public String validateToken(@RequestHeader("Authorization") String authHeader) {
        return service.validateToken(authHeader);
    }

    @GetMapping("/email/{email}")
    public UserResponse getUser(@PathVariable String email) {
        return service.getUser(email);
    }

    @PostMapping("/users/cards")
    public ResponseEntity<String> addCardToUser(@RequestBody CardRequest cardRequest, @RequestHeader("Authorization") String authHeader) throws UserNotFoundException { //, @RequestHeader("Authorization") String authHeader
        return service.addCardToUser(cardRequest, authHeader);
    }

    @PostMapping("/users/cards/deduct")
    public Long deductMoneyFromCardAndGetUserId(@RequestBody DeductRequest deductRequest, @RequestHeader String userEmail) throws MissMatchException, InsufficientBalanceException, UserNotFoundException, CardNotFoundException {
        return service.deductMoneyFromCardAndGetUserId(deductRequest, userEmail);
    }
}