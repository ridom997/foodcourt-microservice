package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Role;

import java.util.List;

public interface IRoleServicePort {
    List<Role> getAllRoles();
}
