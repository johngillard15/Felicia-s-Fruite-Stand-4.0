package com.product;

import java.io.Serializable;

public abstract class Product implements Serializable {
    public final String name;
    public final int price;
    public final String useBy;
    private int quantity; // TODO: remember to get wholesale

    public Product(String name, int price, String useBy){
        this.name = name;
        this.price = price;
        this.useBy = useBy;
        quantity = 1;
    }

    public Product(String name, int price, String useBy, int quantity){
        this.name = name;
        this.price = price;
        this.useBy = useBy;
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getFormattedPrice(){
        return String.format("%,.2f", (double) price / 100);
    }
}
