package com.product;

public class Meat extends Product{
    public final boolean isFrozen;

    public Meat(String name, int price, String useBy, boolean isFrozen){
        super(name, price, useBy);
        this.isFrozen = isFrozen;
    }

    public Meat(String name, int price, String useBy, int quantity, boolean isFrozen){
        super(name, price, useBy, quantity);
        this.isFrozen = isFrozen;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", %s", isFrozen ? "frozen" : "fresh");
    }
}
