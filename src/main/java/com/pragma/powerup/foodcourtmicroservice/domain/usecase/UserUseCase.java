package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.IUserServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.model.User;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {
    private final IUserPersistencePort userPersistencePort;

    public UserUseCase(IUserPersistencePort personPersistencePort) {
        this.userPersistencePort = personPersistencePort;
    }

    @Override
    public void saveUser(User user) {
        userPersistencePort.saveUser(user);
    }
}
