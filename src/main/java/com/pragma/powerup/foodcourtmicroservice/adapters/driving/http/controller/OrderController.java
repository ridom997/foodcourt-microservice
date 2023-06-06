package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.controller;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewOrderRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
public class OrderController {

    private final IOrderHandler orderHandler;

    @Operation(summary = "Create a new order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Restaurant not found or dish not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "409", description = "Client already has an active order in the restaurant selected",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "400", description = "Bad Request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PostMapping
    public ResponseEntity<OrderResponseDto> createNewOrder(@Valid @RequestBody NewOrderRequestDto newOrderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderHandler.createOrder(newOrderRequestDto));
    }


}
