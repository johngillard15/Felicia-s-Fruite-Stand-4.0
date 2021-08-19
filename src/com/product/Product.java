package com.product;

public abstract class Product {
    private final String name;
    private final int price;
    //public final int quantity;
    //protected String useBy;

    public Product(String name, int price){
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getFormattedPrice(){
        return String.format("%,.2f", (double) price / 100);
    }
}
