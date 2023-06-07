package com.pragma.powerup.foodcourtmicroservice.domain.model;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;

public class Restaurant {
    private Long id;
    private String name;
    private String address;
    private Long idOwner;
    private String phone;
    private String urlLogo;
    private String nit;

    public Restaurant() {
    }

    public Restaurant(Long id) {
        this.id = id;
    }

    public Restaurant(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Restaurant(Long id, String name, String address, Long idOwner, String phone, String urlLogo, String nit) {
        this.id = id;
        setName(name);
        setAddress(address);
        setIdOwner(idOwner);
        setPhone(phone);
        setUrlLogo(urlLogo);
        setNit(nit);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new FailValidatingRequiredVariableException("Name is not present");
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.isEmpty()) throw new FailValidatingRequiredVariableException("Address is not present");
        this.address = address;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Long idOwner) {
        if (idOwner == null) throw new FailValidatingRequiredVariableException("idOwner is not present");
        this.idOwner = idOwner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.isEmpty()) throw new FailValidatingRequiredVariableException("Phone is not present");
        this.phone = phone;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        if (urlLogo == null || urlLogo.isEmpty()) throw new FailValidatingRequiredVariableException("Url logo is not present");
        this.urlLogo = urlLogo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        if (nit == null || nit.isEmpty()) throw new FailValidatingRequiredVariableException("Nit is not present");
        this.nit = nit;
    }
}
