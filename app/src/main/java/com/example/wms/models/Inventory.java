package com.example.wms.models;

public class Inventory {
    private int id;
    private int id_product;
    private int inv_type;
    private String inv_date;
    private int qty;
    private float price;
    private int qty_oh;
    private String notes;
    private int modify_by;
    private int id_ic_reason;
    private String modify_date;


    public Inventory(int id, int id_product, int inv_type, String inv_date, int qty, float price, int qty_oh, String notes, int modify_by, int id_ic_reason, String modify_date) {
        this.id = id;
        this.id_product = id_product;
        this.inv_type = inv_type;
        this.inv_date = inv_date;
        this.qty = qty;
        this.price = price;
        this.qty_oh = qty_oh;
        this.notes = notes;
        this.modify_by = modify_by;
        this.id_ic_reason = id_ic_reason;
        this.modify_date = modify_date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getInv_type() {
        return inv_type;
    }

    public void setInv_type(int inv_type) {
        this.inv_type = inv_type;
    }

    public String getInv_date() {
        return inv_date;
    }

    public void setInv_date(String inv_date) {
        this.inv_date = inv_date;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQty_oh() {
        return qty_oh;
    }

    public void setQty_oh(int qty_oh) {
        this.qty_oh = qty_oh;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getModify_by() {
        return modify_by;
    }

    public void setModify_by(int modify_by) {
        this.modify_by = modify_by;
    }

    public String getModify_date() {
        return modify_date;
    }

    public void setModify_date(String modify_date) {
        this.modify_date = modify_date;
    }

    public int getId_ic_reason() {
        return id_ic_reason;
    }

    public void setId_ic_reason(int id_ic_reason) {
        this.id_ic_reason = id_ic_reason;
    }
}
