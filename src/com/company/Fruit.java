package com.company;

public class Fruit extends Product{
    public boolean inSeason;

    public Fruit(String item, int amount, boolean inSeason){
        super(item, amount);
        this.inSeason = inSeason;
    }
}
