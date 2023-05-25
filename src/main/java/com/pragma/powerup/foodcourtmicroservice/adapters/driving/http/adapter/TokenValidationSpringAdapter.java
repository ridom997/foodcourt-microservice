package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.adapter;

import com.pragma.powerup.foodcourtmicroservice.configuration.security.jwt.JwtProvider;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TokenValidationSpringAdapter implements ITokenValidationPort {


    private final JwtProvider jwtProvider;

    @Override
    public Boolean userIsInToken(Long idUser, String token) {
        if (token == null)
            return false;
        Claims claimsFromToken = jwtProvider.getClaimsFromToken(token);
        Long idUserFromToken = ((Integer) claimsFromToken.get("idUser")).longValue();
        if (idUserFromToken == null)
            return false;
        return idUser.equals(idUserFromToken);
    }
}
