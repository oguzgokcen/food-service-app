package com.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.service.spi.InjectService;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {
    @Id
    long productId;
    double productAverageStar;
    int reviewTimesOfProduct;
}