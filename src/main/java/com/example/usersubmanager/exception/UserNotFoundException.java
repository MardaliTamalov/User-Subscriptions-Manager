package com.example.usersubmanager.exception;

public class UserNotFoundException extends RuntimeException {

    private String message;

    public UserNotFoundException(String message) {super(message);}

}
