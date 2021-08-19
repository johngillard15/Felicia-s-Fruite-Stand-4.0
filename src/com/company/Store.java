package com.company;

import com.utilities.Input;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Store {
    public final String name;
    public final List<Product> produce = new ArrayList<>();
    private int balance = 100000; // balance / 100 = true balance
    private static final double MARKUP = 0.3; // markup percentage for profits
    //private static final String todaysDate = LocalDate.now().toString();

    public Store(){
        name = "Felicia's Fruit Stand";
    }

    public Store(String name){
        this.name = name;
    }

    public int getProduceQuantity(){
        return produce.size();
    }

    public void addProduct(Product product){
        produce.add(product);
        withdraw(product.getPrice());
    }

    public void sellProduct(Product product){
        int salePrice = (int) ((double)product.getPrice() * MARKUP) + product.getPrice();
        deposit(salePrice);

        produce.remove(product);
    }

    public String getMarkupPrice(Product product){
        int salePrice = (int) ((double)product.getPrice() * MARKUP) + product.getPrice();

        return String.format("%,.2f", (double) salePrice / 100);
    }

    public int getBalance(){
        return balance;
    }

    private void setBalance(int value){
        balance += value;
    }

    public String getFormattedBalance(){
        return String.format("%,.2f", (double) getBalance() / 100);
    }

    private void deposit(int deposit){
        setBalance(deposit);
    }

    private void withdraw(int cost){
        setBalance(-cost);
    }

    public void printProduce(){
        System.out.println("\n~ List of Produce ~");
        int listNum = 0;
        for(Product product : produce){
            String typeSpecific = product instanceof Fruit ? "in season" : "frozen";

            if(product instanceof Fruit)
                typeSpecific = (((Fruit) product).inSeason ? "" : "not ") + typeSpecific;
            else
                typeSpecific = (((Meat) product).isFrozen ? typeSpecific : "fresh");

            System.out.printf("\t%d. %s (%s) - $%s\n", ++listNum, product.getName(), typeSpecific, getMarkupPrice(product));
        }
    }

    public void getProductInfo(){
        System.out.println("\nWhat type of produce are you adding? ");
        System.out.println("Type: ");
        System.out.println("\t1. Fruit");
        System.out.println("\t2. Meat");
        System.out.print("choice: ");
        int type = Input.getInt(2);

        System.out.println("\nEnter product name ");
        System.out.print("product: ");
        String name = Input.getString();
        System.out.println("Enter product price (no decimals): ");
        System.out.print("price: ");
        int price = Input.getInt();

        switch(type){
            case 1 -> {
                System.out.printf("\nIs \"%s\" in season?\n", name);
                System.out.println("\t1. In season");
                System.out.println("\t2. Not in season");
                System.out.print("choice: ");
                boolean inSeason = Input.getInt(2) == 1;
                addProduct(new Fruit(name, price, inSeason));
            }

            case 2 -> {
                System.out.printf("Is %s frozen?\n", name);
                System.out.println("\t1. Frozen");
                System.out.println("\t2. Not Frozen");
                System.out.print("choice: ");
                boolean isFrozen = Input.getInt(2) == 1;
                addProduct(new Meat(name, price, isFrozen));
            }
        }

        Product product = produce.get(produce.size() - 1);

        System.out.printf("\n%s successfully added for $%s.\n", product.getName(), product.getFormattedPrice());
    }
}
