package com.pragma.powerup.foodcourtmicroservice.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class HistoryOrderDto {
    private Long idOrder;
    private List<TraceabilityOrderDto> statusChanges;
    private LocalDateTime creationTime;
    private LocalDateTime endTime;
    private Long idChef;
    private Integer actualStatus;

    public List<TraceabilityOrderDto> getStatusChanges() {
        return statusChanges;
    }

    public void setStatusChanges(List<TraceabilityOrderDto> statusChanges) {
        this.statusChanges = statusChanges;
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

    public Long getIdChef() {
        return idChef;
    }

    public void setIdChef(Long idChef) {
        this.idChef = idChef;
    }

    public Integer getActualStatus() {
        return actualStatus;
    }

    public void setActualStatus(Integer actualStatus) {
        this.actualStatus = actualStatus;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public HistoryOrderDto(Long idOrder,List<TraceabilityOrderDto> statusChanges, LocalDateTime creationTime, LocalDateTime endTime, Long idChef, Integer actualStatus) {
        this.idOrder = idOrder;
        this.statusChanges = statusChanges;
        this.creationTime = creationTime;
        this.endTime = endTime;
        this.idChef = idChef;
        this.actualStatus = actualStatus;
    }

    public HistoryOrderDto() {
    }
}
