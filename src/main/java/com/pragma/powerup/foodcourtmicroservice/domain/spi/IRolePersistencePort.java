package com.pragma.powerup.foodcourtmicroservice.domain.spi;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Role;

import java.util.List;

public interface IRolePersistencePort {
    List<Role> getAllRoles();
}
