package com.elbuensabor.api.dto;

public class UpdateOrderStateRequest {
    private Long id;
    private String newState;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }
}
