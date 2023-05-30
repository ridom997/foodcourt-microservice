package com.pragma.powerup.foodcourtmicroservice.domain.spi;

public interface ITokenValidationPort {
    Boolean userIsInToken(Long idUser, String token);
    Long findIdUserFromToken(String token);
    void verifyRoleInToken(String token, String roleName);
}
