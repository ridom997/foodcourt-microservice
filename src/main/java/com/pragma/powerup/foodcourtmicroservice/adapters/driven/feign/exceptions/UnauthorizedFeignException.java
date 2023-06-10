package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions;

public class UnauthorizedFeignException extends RuntimeException{
    public UnauthorizedFeignException(String message) {
        super(message);
    }
}
