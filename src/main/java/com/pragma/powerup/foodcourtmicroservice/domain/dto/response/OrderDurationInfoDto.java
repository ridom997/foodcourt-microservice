package com.pragma.powerup.foodcourtmicroservice.domain.dto.response;

import java.time.Duration;
import java.time.LocalDateTime;

public class OrderDurationInfoDto {
    private Long idOrder;
    private LocalDateTime creationTime;
    private LocalDateTime endTime;
    private Long durationInSeconds;

    private Integer finalStatus;

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(Long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public Integer getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(Integer finalStatus) {
        this.finalStatus = finalStatus;
    }

    public OrderDurationInfoDto() {
    }

    public OrderDurationInfoDto(Long idOrder, LocalDateTime creationTime, LocalDateTime endTime, Integer finalStatus) {
        this.idOrder = idOrder;
        this.creationTime = creationTime;
        this.endTime = endTime;
        this.finalStatus = finalStatus;
        this.durationInSeconds = Duration.between(creationTime, endTime).getSeconds();
    }
}
