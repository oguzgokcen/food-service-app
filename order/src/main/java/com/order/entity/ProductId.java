package com.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCT_ID")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "productIdList")
    private Set<Order> orders = new HashSet<>();

    public ProductId(Long productId) {
        this.productId = productId;
    }
}
