package com.order.exception;

public class RestaurantNotExistException extends Exception {
    public RestaurantNotExistException(String message) {
        super(message);
    }
}
