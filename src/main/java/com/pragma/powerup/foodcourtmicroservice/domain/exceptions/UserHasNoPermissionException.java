package com.pragma.powerup.foodcourtmicroservice.domain.exceptions;

public class UserHasNoPermissionException extends RuntimeException{
    public UserHasNoPermissionException(String message) {
        super(message);
    }
}
