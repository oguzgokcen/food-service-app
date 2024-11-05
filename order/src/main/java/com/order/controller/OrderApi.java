package com.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApi {
    @GetMapping
    public String getOrder(){
        return "ORDER RETRIEVED";
    }


}
