package com.pragma.powerup.foodcourtmicroservice.domain.exceptions;

public class NoRoleFoundInTokenException extends RuntimeException{
    public NoRoleFoundInTokenException(String message) {
        super(message);
    }
}
