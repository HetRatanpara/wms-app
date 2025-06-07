package com.example.wms.models;

import androidx.annotation.NonNull;

public class Category {

    Integer id;
    String category_name;

    public Category(Integer id, String category_name) {
        this.id = id;
        this.category_name = category_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    @NonNull
    @Override
    public String toString() {
        return category_name;
    }
}
