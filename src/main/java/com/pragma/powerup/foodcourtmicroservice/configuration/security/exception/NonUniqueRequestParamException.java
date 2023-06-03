package com.pragma.powerup.foodcourtmicroservice.configuration.security.exception;

public class NonUniqueRequestParamException extends RuntimeException{
    public NonUniqueRequestParamException(String message) {
        super(message);
    }
}
