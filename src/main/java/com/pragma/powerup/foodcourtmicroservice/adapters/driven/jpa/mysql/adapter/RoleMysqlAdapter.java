package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IRoleRepository;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.RoleEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IRoleEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Role;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IRolePersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RoleMysqlAdapter implements IRolePersistencePort {
    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;
    @Override
    public List<Role> getAllRoles() {
        List<RoleEntity> roleEntityList = roleRepository.findAll();
        if (roleEntityList.isEmpty()) {
            throw new NoDataFoundException();
        }
        return roleEntityMapper.toRoleList(roleEntityList);
    }
}
