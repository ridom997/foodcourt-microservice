package com.pragma.powerup.foodcourtmicroservice.domain.dto;

public class OrderActorsDto {
    private UserBasicInfoDto client;
    private UserBasicInfoDto employee;

    public UserBasicInfoDto getClient() {
        return client;
    }

    public void setClient(UserBasicInfoDto client) {
        this.client = client;
    }

    public UserBasicInfoDto getEmployee() {
        return employee;
    }

    public void setEmployee(UserBasicInfoDto employee) {
        this.employee = employee;
    }

    public OrderActorsDto(UserBasicInfoDto client, UserBasicInfoDto employee) {
        this.client = client;
        this.employee = employee;
    }

    public OrderActorsDto() {
    }
}
