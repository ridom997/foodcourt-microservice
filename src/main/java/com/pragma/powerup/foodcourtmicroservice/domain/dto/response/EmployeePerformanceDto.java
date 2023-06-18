package com.pragma.powerup.foodcourtmicroservice.domain.dto.response;

public class EmployeePerformanceDto {
    private Long idEmployee;
    private Integer averageTimeInSeconds;
    private Long numberOfOrders;

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Long getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(Long numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public EmployeePerformanceDto() {
    }

    public Integer getAverageTimeInSeconds() {
        return averageTimeInSeconds;
    }

    public void setAverageTimeInSeconds(Integer averageTimeInSeconds) {
        this.averageTimeInSeconds = averageTimeInSeconds;
    }

    public EmployeePerformanceDto(Long idEmployee, Integer averageTimeInSeconds, Long numberOfOrders) {
        this.idEmployee = idEmployee;
        this.averageTimeInSeconds = averageTimeInSeconds;
        this.numberOfOrders = numberOfOrders;
    }
}
