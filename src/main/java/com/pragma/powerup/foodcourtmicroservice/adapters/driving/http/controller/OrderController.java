package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.controller;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewOrderRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.PinRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.HistoryOrderResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderAndStatusMessagingResponseDto;
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
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Assign to an order (make the order to status in progress)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Assigned to the order",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User who made the request is not an employee of the given restaurant or order is already assigned",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Employee does not have idRestaurant associated or no order found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "500", description = "Error in communication with user-microservice or traceability microservice",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PatchMapping("/{idOrder}/assignEmployee")
    public ResponseEntity<OrderResponseDto> assignOrder(@PathVariable("idOrder") @Valid Long idOrder) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderHandler.assignOrder(idOrder));
    }

    @Operation(summary = "Set order to status: Ready",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order was changed to status ready.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderAndStatusMessagingResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User who made the request is not an employee of the given restaurant or isn´t the chef of the order or order isn't in progress",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Employee does not have idRestaurant associated or no order found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "500", description = "Error in communication with user-microservice or traceability microservice",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PatchMapping("/{idOrder}/ready")
    public ResponseEntity<OrderAndStatusMessagingResponseDto> changeOrderToReady(@PathVariable("idOrder") @Valid Long idOrder) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderHandler.orderReady(idOrder));
    }

    @Operation(summary = "Set order to status: Delivered",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order was changed to status delivered.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User who made the request is not an employee of the given restaurant or isn´t the chef of the order or order isn't ready",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Employee does not have idRestaurant associated or no order found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "500", description = "Error in communication with user-microservice or traceability microservice",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PatchMapping("/{idOrder}/deliver")
    public ResponseEntity<OrderResponseDto> changeOrderToDelivered(@PathVariable("idOrder") @Valid Long idOrder, @Valid @RequestBody PinRequestDto pinRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderHandler.orderDelivered(idOrder, pinRequestDto.getPin()));
    }

    @Operation(summary = "Set order to status: Cancelled",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order was changed to status cancelled.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User who made the request is not the client of the given order or order isn't pending",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Order not found (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "500", description = "Error in communication with user-microservice or traceability microservice",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PatchMapping("/{idOrder}/cancel")
    public ResponseEntity<OrderResponseDto> changeOrderToCancelled(@PathVariable("idOrder") @Valid Long idOrder) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderHandler.orderCancelled(idOrder));
    }

    @Operation(summary = "Get historic of order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order history found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HistoryOrderResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User who made the request is not the client of the given order",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Order not found or no logs found when order is not in a pending status",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "500", description = "Error in communication with traceability microservice",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @GetMapping("/{idOrder}/history")
    public ResponseEntity<HistoryOrderResponseDto> getHistoryOfOrder(@PathVariable("idOrder") @Valid Long idOrder) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderHandler.getHistoryOfOrder(idOrder));
    }
}
