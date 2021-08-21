package com.product;

public class Fruit extends Product{
    public final boolean inSeason;

    public Fruit(String item, int amount, String useBy, boolean inSeason){
        super(item, amount, useBy);
        this.inSeason = inSeason;
    }
}
