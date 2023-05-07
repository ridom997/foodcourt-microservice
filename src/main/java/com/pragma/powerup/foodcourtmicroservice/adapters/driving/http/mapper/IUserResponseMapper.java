package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.mapper;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.UserResponseDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {
    UserResponseDto userToPersonResponse(User user);
    List<UserResponseDto> userListToPersonResponseList(List<User> userList);
}
