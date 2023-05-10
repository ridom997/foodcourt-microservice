package com.pragma.powerup.foodcourtmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FoodCourtMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodCourtMicroserviceApplication.class, args);
	}

}
