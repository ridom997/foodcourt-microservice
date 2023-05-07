package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.UserEntity;
import com.pragma.powerup.foodcourtmicroservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserEntityMapper {
    UserEntity toEntity(User user);
}
