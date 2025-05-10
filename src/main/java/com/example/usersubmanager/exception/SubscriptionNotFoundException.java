package com.example.usersubmanager.exception;

public class SubscriptionNotFoundException extends RuntimeException{
    private String message;
    public SubscriptionNotFoundException(String message) {super(message);}
}
