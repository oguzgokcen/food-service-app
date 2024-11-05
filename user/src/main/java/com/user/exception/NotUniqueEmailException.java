package com.user.exception;

public class NotUniqueEmailException extends RuntimeException{
    public NotUniqueEmailException() {
        super("Bu email zaten kullanımda");
    }
}
