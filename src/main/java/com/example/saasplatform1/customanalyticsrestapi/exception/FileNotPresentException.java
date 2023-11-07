package com.example.saasplatform1.customanalyticsrestapi.exception;

public class FileNotPresentException extends RuntimeException{
    public FileNotPresentException(String message){
        super(message);
    }
}
