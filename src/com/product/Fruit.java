package com.product;

public class Fruit extends Product{
    public final boolean inSeason;

    public Fruit(String item, int amount, String useBy, boolean inSeason){
        super(item, amount, useBy);
        this.inSeason = inSeason;
    }

    public Fruit(String item, int amount, String useBy, int quantity, boolean inSeason){
        super(item, amount, useBy, quantity);
        this.inSeason = inSeason;
    }
}
