package com.product;

public class Meat extends Product{
    public final boolean isFrozen;

    public Meat(String item, int amount, String useBy, boolean isFrozen){
        super(item, amount, useBy);
        this.isFrozen = isFrozen;
    }
}
