package com.user.service;

import com.user.dto.CardRequest;
import com.user.dto.DeductRequest;
import com.user.entity.UserCards;
import com.user.entity.UserCredential;
import com.user.exception.CardNotFoundException;
import com.user.exception.InsufficientBalanceException;
import com.user.exception.MissMatchException;
import com.user.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCardService {
    private final UserCardRepository userCardRepository;

    public ResponseEntity<String> addCardToUser(CardRequest cardRequest, UserCredential user) {
        UserCards userCards = UserCards.builder()
                .cardNumber(cardRequest.getCardNumber())
                .cvv(cardRequest.getCvv())
                .cardExpiryMonth(cardRequest.getCardExpiryMonth())
                .cardExpiryYear(cardRequest.getCardExpiryYear())
                .userCredential(user)
                .balance(cardRequest.getBalance())
                .build();
        userCardRepository.save(userCards);
        return ResponseEntity.ok("Card saved successfully");
    }

    public void deductMoneyFromCard(DeductRequest deductRequest, String userEmail) throws MissMatchException, InsufficientBalanceException, CardNotFoundException {
        UserCards userCards = userCardRepository.findUserCardsByCardNumberAndCvvAndCardExpiryMonthAndCardExpiryYear(
                deductRequest.getCardNumber(),
                deductRequest.getCvv(),
                deductRequest.getCardExpiryMonth(),
                deductRequest.getCardExpiryYear()
        ).orElseThrow(() -> new CardNotFoundException("Card not found"));
        if (!userCards.getUserCredential().getEmail().equalsIgnoreCase(userEmail))
            throw new MissMatchException("Card does not belong this user");
        else if (userCards.getBalance() < deductRequest.getAmount())
            throw new InsufficientBalanceException("Insufficient balance!");
        userCards.setBalance(userCards.getBalance() - deductRequest.getAmount());
        userCardRepository.save(userCards);
    }
}
