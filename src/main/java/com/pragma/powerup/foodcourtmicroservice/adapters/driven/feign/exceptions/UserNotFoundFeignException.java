package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions;

public class UserNotFoundFeignException extends RuntimeException {
    public UserNotFoundFeignException(Throwable cause) {
        super(cause);
    }
}
