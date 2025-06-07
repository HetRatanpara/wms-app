package com.example.wms.models;

import androidx.annotation.NonNull;

public class ICReason {

    Integer id;
    String reason;
    Integer is_active;

    public ICReason(Integer id, String reason, Integer is_active) {
        this.id = id;
        this.reason = reason;
        this.is_active = is_active;
    }

    public ICReason(Integer id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @NonNull
    @Override
    public String toString() {
        return reason;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }
}
