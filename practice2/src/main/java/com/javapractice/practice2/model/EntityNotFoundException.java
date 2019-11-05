package com.javapractice.practice2.model;

public class EntityNotFoundException extends RuntimeException {
    private final String message;


    public EntityNotFoundException(String message) {
        super(message);
        this.message = message;
    }



}
