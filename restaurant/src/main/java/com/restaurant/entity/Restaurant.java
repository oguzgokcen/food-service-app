package com.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RESTAURANTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;


    //todo image eklenecek

}
