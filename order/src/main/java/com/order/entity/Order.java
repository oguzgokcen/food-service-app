package com.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long restaurantId;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    @JsonIgnore
    private List<Product> productList;

    private Long totalPrice;
    private Long userId;

}
