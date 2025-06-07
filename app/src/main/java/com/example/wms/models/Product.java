package com.example.wms.models;

public class Product {

    private int id;
    private String product_name;
    private String sku;
    private String description;
    private byte[] image;
    private int qty_on_hand;
    private int id_category;
    private int id_supplier;


    public Product(int id, String product_name, String sku, String description, int id_category, byte[] image, Integer id_supplier, int qty_on_hand) {
        this.id = id;
        this.product_name = product_name;
        this.sku = sku;
        this.description = description;
        this.id_category = id_category;
        this.image = image;
        this.id_supplier = id_supplier;
        this.qty_on_hand = qty_on_hand;
    }




    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public int getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(Integer id_supplier) {
        this.id_supplier = id_supplier;
    }

    public int getQty_on_hand() {
        return qty_on_hand;
    }

    public void setQty_on_hand(int qty_on_hand) {
        this.qty_on_hand = qty_on_hand;
    }
}
