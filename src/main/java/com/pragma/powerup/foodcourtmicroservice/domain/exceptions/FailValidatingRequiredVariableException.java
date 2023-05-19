package com.pragma.powerup.foodcourtmicroservice.domain.exceptions;

public class FailValidatingRequiredVariableException extends RuntimeException {
    public FailValidatingRequiredVariableException(String message) {
        super(message);
    }
}
