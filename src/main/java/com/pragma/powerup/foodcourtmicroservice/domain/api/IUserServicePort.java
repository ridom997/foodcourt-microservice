package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.model.User;

public interface IUserServicePort {
    void saveUser(User user);
}
