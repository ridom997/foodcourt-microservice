package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.controller;

import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.EditDishRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.request.NewDishInfoRequestDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.handlers.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/dishes")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
public class DishController {

    private final IDishHandler dishHandler;

    @Operation(summary = "Add a new dish",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dish created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Malformed request body",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User provided does not have permission check message",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Category or restaurant not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PostMapping
    public ResponseEntity<DishResponseDto> createNewDish(@Valid @RequestBody NewDishInfoRequestDto dishInfoRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dishHandler.saveDish(dishInfoRequestDto));
    }

    @Operation(summary = "Edit a dish",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dish edited",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Malformed request body",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User provided does not have permission check message",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Dish not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PatchMapping(value = "/{id}")
    public ResponseEntity<DishResponseDto> editDish(@PathVariable("id") @NotNull Long idDish , @Valid @RequestBody EditDishRequestDto editDishRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(dishHandler.editDish(idDish, editDishRequestDto));
    }

    @Operation(summary = "Edit status of a dish",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dish status updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Malformed request body",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "403", description = "User provided does not have permission check message",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error"))),
                    @ApiResponse(responseCode = "404", description = "Dish not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))
            })
    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<DishResponseDto> editStatusDish(@PathVariable("id") @NotNull Long idDish , @RequestParam("active") @NotNull boolean active) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(dishHandler.changeStatusDish(idDish, active));
    }
}
