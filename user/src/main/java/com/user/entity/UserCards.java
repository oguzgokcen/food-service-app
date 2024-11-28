package com.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_CARDS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    private String cvv;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private Long balance;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private UserCredential userCredential;
}