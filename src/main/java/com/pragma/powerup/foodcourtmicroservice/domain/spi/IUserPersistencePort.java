package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.User;

public interface IUserPersistencePort {
    void saveUser(User user);
}
