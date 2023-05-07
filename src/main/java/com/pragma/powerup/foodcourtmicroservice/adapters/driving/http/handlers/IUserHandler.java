package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.UserRequestDto;

public interface IUserHandler {
    void saveUser(UserRequestDto personRequestDto);
}
