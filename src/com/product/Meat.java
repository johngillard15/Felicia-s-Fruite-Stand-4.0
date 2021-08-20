package com.product;

public class Meat extends Product{
    public final boolean isFrozen;

    public Meat(String item, int amount, boolean isFrozen){
        super(item, amount);
        this.isFrozen = isFrozen;
    }
}
