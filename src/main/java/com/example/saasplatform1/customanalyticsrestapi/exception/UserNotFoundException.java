package com.example.saasplatform1.customanalyticsrestapi.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
