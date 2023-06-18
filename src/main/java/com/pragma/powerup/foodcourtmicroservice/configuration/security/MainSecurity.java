package com.pragma.powerup.foodcourtmicroservice.configuration.security;

import com.pragma.powerup.foodcourtmicroservice.configuration.security.jwt.JwtEntryPoint;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.CLIENT_VALUE;

@Configuration
@EnableWebSecurity
public class MainSecurity {
    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            //To delete generated password.
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, @Autowired JwtEntryPoint jwtEntryPoint) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers( "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.POST,"/restaurants").hasRole("ADMIN")
                        .requestMatchers("/dishes" , "/dishes/{id}", "/restaurants/{id}/validateOwner", "/dishes/{id}/status","/restaurants/{idRestaurant}/completed-orders-duration", "/restaurants/{idRestaurant}/ranking-employees").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET,"/restaurants","/restaurants/{idRestaurant}/dishes", "/orders/{idOrder}/history").hasRole(CLIENT_VALUE)
                        .requestMatchers(HttpMethod.POST,"/orders").hasRole(CLIENT_VALUE)
                        .requestMatchers(HttpMethod.PATCH,"/orders/{idOrder}/cancel").hasRole(CLIENT_VALUE)
                        .requestMatchers("/restaurants/{idRestaurant}/orders", "/orders/{idOrder}/ready", "/orders/{idOrder}/deliver").hasRole("EMPLOYEE")
                        .anyRequest().authenticated()
                )
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint);
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
