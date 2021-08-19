package com.company;

public class Meat extends Product{
    public boolean isFrozen;

    public Meat(String item, int amount, boolean isFrozen){
        super(item, amount);
        this.isFrozen = isFrozen;
    }
}
