package com.example.wms.models;

import androidx.annotation.NonNull;

public class Supplier {

    private Integer id;
    private String supplier_name;
    private String code;
    private String email;
    private String phone;


    public Supplier(String supplier_name, String code, String phone, String email) {
        this.supplier_name = supplier_name;
        this.code = code;
        this.email = email;
        this.phone = phone;
    }

    public Supplier(Integer id, String supplier_name, String code, String phone, String email) {
        this.id = id;
        this.supplier_name = supplier_name;
        this.code = code;
        this.email = email;
        this.phone = phone;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @NonNull
    @Override
    public String toString() {
        return supplier_name;
    }
}
