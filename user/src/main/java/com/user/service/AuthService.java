package com.user.service;

import com.user.dto.*;
import com.user.entity.UserCredential;
import com.user.exception.*;
import com.user.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserCardService userCardService;

    public MessageResponse saveUser(RegisterRequest registerRequest) {
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        try {
            userCredentialRepository.save(new UserCredential(registerRequest.getFullName(), registerRequest.getEmail(), registerRequest.getPassword()));
        } catch (DataIntegrityViolationException ex) {
            throw new NotUniqueEmailException();
        }
        return new MessageResponse("user added to the system");
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public String validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public UserResponse getUser(String email) {
        UserCredential user = userCredentialRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail());
    }

    public ResponseEntity<String> addCardToUser(CardRequest cardRequest, String authHeader) throws UserNotFoundException {
        UserCredential user = userCredentialRepository.findByEmail(validateToken(authHeader)).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userCardService.addCardToUser(cardRequest, user);
    }

    public Long deductMoneyFromCardAndGetUserId(DeductRequest deductRequest, String userEmail) throws MissMatchException, InsufficientBalanceException, UserNotFoundException, CardNotFoundException {
        UserCredential user = userCredentialRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User not found"));
        userCardService.deductMoneyFromCard(deductRequest, userEmail);
        return (long) user.getId();
    }
}
