package com.pragma.powerup.foodcourtmicroservice.domain.exceptions;

public class NoDataFoundException extends RuntimeException{
    public NoDataFoundException(String message) {
        super(message);
    }
}
