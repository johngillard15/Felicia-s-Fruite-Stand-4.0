package com.product;

public class Fruit extends Product{
    public final boolean inSeason;

    public Fruit(String item, int amount, boolean inSeason){
        super(item, amount);
        this.inSeason = inSeason;
    }
}
