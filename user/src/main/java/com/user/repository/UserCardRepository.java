package com.user.repository;

import com.user.entity.UserCards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCardRepository extends JpaRepository<UserCards,Long> {
    Optional<UserCards> findUserCardsByCardNumberAndCvvAndCardExpiryMonthAndCardExpiryYear(String cardNumber, String cvv, String cardExpiryMonth, String cardExpiryYear);
}
