package com.product;

public abstract class Product {
    public final String name;
    public final int price;
    //public final int quantity; // TODO: remember to get wholesale
    public final String useBy;

    public Product(String name, int price, String useBy){
        this.name = name;
        this.price = price;
        this.useBy = useBy;
    }

    public String getFormattedPrice(){
        return String.format("%,.2f", (double) price / 100);
    }
}
