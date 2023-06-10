package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import java.time.LocalDateTime;

public class OrderLogDto {
    private Long idOrder;
    private Long idClient;
    private String mailClient;
    private Integer previousStatus;
    private Integer newStatus;
    private Long idEmployee;
    private String mailEmployee;
    private LocalDateTime date;

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getMailClient() {
        return mailClient;
    }

    public void setMailClient(String mailClient) {
        this.mailClient = mailClient;
    }

    public Integer getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(Integer previousStatus) {
        this.previousStatus = previousStatus;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getMailEmployee() {
        return mailEmployee;
    }

    public void setMailEmployee(String mailEmployee) {
        this.mailEmployee = mailEmployee;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
