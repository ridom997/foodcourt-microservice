package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryOrderResponseDto {
    @Schema(example = "100")
    private Long idOrder;
    @JsonInclude(JsonInclude.Include.NON_NULL) //do not show this variable in the response if it's null.
    private List<TraceabilityOrderResponseDto> statusChanges;

    private LocalDateTime creationTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime endTime;
    @Schema(example = "20")
    private Long idChef;
    @Schema(example = "IN PROGRESS")
    private String actualStatus;
}
