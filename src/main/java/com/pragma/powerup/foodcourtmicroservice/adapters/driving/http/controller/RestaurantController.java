package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.controller;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.OrderWithDetailResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.RestaurantResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IRestaurantHandler;
import com.pragma.powerup.foodcourtmicroservice.configuration.Constants;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.RequestParamValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
public class RestaurantController {

    private final IRestaurantHandler restaurantHandler;
    private final IDishHandler dishHandler;
    private final IOrderHandler orderHandler;

    @Operation(summary = "Add a new restaurant",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Restaurant created",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Map"))),
                    @ApiResponse(responseCode = "404", description = "Owner doesn't exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User provided does not have permission check message",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "400", description = "Required variable is missing",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "500", description = "Fail communication with user-microservice",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PostMapping
    public ResponseEntity<Map<String, String>> createNewRestaurant(@Valid @RequestBody  RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurant(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.RESTAURANT_CREATED_MESSAGE));
    }

    @Operation(summary = "Validate if the user from token is the owner of the restaurant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Validation done",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"isTheRestaurantOwner\": true}"))),
                    @ApiResponse(responseCode = "404", description = "Restaurant not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request or idUser doesn't exists in token",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "400", description = "Required variable is missing or Restaurant doesn't have owner",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @GetMapping(value = "/{id}/validateOwner")
    public ResponseEntity<Map<String, Boolean>> userIsTheRestaurantOwner(@PathVariable @NotNull Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_IS_THE_RESTAURANT_OWNER_KEY,
                        restaurantHandler.userIsTheRestaurantOwner(id)));
    }

    @Operation(summary = "Get paginated restaurants",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of Restaurants found (returns a list of objects similar to the example object)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "No restaurants found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "400", description = "Bad Request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> findAllRestaurantsPaged(HttpServletRequest httpServletRequest, @RequestParam("page") int page, @RequestParam("size") int size) {
        RequestParamValidator.validate(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(restaurantHandler.findAllPaged(page,size));
    }

    @Operation(summary = "List Dishes of restaurant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of dishes found (returns a list of objects similar to the example object)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "No data found (restaurant or category or list of dishes)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @GetMapping(value = "/{idRestaurant}/dishes")
    public ResponseEntity<List<DishResponseDto>> findAllDishesByRestaurantAndCategory(
            HttpServletRequest httpServletRequest,
            @PathVariable Long idRestaurant,
            @RequestParam(required = false) Long idCategory,
            @RequestParam int page,
            @RequestParam int size
    ) {
        RequestParamValidator.validate(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(dishHandler.getPagedDishesByRestaurantAndOptionalCategory(page,size,idRestaurant,idCategory));
    }

    @Operation(summary = "Find all orders of restaurants by status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of orders found (returns a list of objects similar to the example object)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderWithDetailResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (check response message)",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User who made the request is not an employee of the given restaurant",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Employee doesnt have idRestaurant associated or no orders found or no detail found in an order",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "500", description = "Error in communication with user-microservice",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @GetMapping(value = "/{idRestaurant}/orders")
    public ResponseEntity<List<OrderWithDetailResponseDto>> findAllOrdersByStatus(
            HttpServletRequest httpServletRequest,
            @PathVariable Long idRestaurant,
            @RequestParam Integer idStatus,
            @RequestParam int page,
            @RequestParam int size
    ) {
        RequestParamValidator.validate(httpServletRequest); //validate request params
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderHandler.findAllPagedOrdersByIdStatus(idRestaurant,idStatus,page,size));
    }

}
