package com.product;

public class Fruit extends Product{
    public final boolean inSeason;

    public Fruit(String name, int price, String useBy, boolean inSeason){
        super(name, price, useBy);
        this.inSeason = inSeason;
    }

    public Fruit(String name, int price, String useBy, int quantity, boolean inSeason){
        super(name, price, useBy, quantity);
        this.inSeason = inSeason;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", %s", (inSeason ? "" : "not in ") + "season");
    }
}
