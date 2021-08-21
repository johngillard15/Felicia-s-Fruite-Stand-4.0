package com.store;

import com.product.Fruit;
import com.product.Meat;
import com.product.Product;
import com.utilities.ANSI;
import com.utilities.Input;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Store {
    public final String name;
    private final List<Product> produce = new ArrayList<>();
    private int balance = 1_000_00; // balance / 100 = true balance
    private static final double MARKUP = 0.3; // markup percentage for profits
    //private static final double MARKUP_FRUIT = 0.3;
    //private static final double MARKUP_MEAT = 0.3;

    public Store(){
        name = "Felicia's Fruit Stand";
    }

    public Store(String name){
        this.name = name;
    }

    public Product getProduct(int index){
        return produce.get(index);
    }

    public void removeProduct(Product product){
        produce.remove(product);
    }

    public int getInvSize(){
        return produce.size();
    }

    public void addProduct(Product product){
        produce.add(product);
        withdraw(product.price);
    }

    public void sellProduct(Product product){
        int salePrice = (int) ((double)product.price * MARKUP) + product.price;
        deposit(salePrice);

        produce.remove(product);
    }

    public String getMarkupPrice(Product product){
        int salePrice = (int) ((double)product.price * MARKUP) + product.price;

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

            String ANSI_COLOR = isExpired(product) ? ANSI.RED : "";
            String useBy = ANSI_COLOR + product.useBy + ANSI.RESET;

            System.out.printf("\t%d. %s (%s), use by: %s - $%s\n",
                    ++listNum, product.name, typeSpecific, useBy, getMarkupPrice(product));
        }
    }

    public void addNewItem(){
        System.out.println("\nWhat type of produce are you adding? ");
        System.out.println("Type: ");
        System.out.println("\t1. Fruit");
        System.out.println("\t2. Meat");
        System.out.print("choice: ");
        int type = Input.getInt(2);

        System.out.println("\nEnter product name");
        System.out.print("product: ");
        String name = Input.getString();
        System.out.println("Enter product price");
        System.out.print("price: ");
        double price = Input.getDouble();

        System.out.println("\nPlease enter thr product's expiration date");
        System.out.print("Year (yyyy): ");
        String thisYear = LocalDate.now().toString().split("-")[0];
        String thisMonth = LocalDate.now().toString().split("-")[1];
        String thisDay = LocalDate.now().toString().split("-")[2];
        String year = Input.getString();
        System.out.print("Month (MM): ");
        String month = Input.getString();
        System.out.print("Day (dd): ");
        String day = Input.getString();

        String useBy = String.format("%s-%s-%s", year, month, day);

        switch(type){
            case 1 -> {
                System.out.printf("\nIs \"%s\" in season?\n", name);
                System.out.println("\t1. In season");
                System.out.println("\t2. Not in season");
                System.out.print("choice: ");
                boolean inSeason = Input.getInt(2) == 1;
                addProduct(new Fruit(name, (int) (price * 100), useBy, inSeason));
            }

            case 2 -> {
                System.out.printf("Is %s frozen?\n", name);
                System.out.println("\t1. Frozen");
                System.out.println("\t2. Not Frozen");
                System.out.print("choice: ");
                boolean isFrozen = Input.getInt(2) == 1;
                addProduct(new Meat(name, (int) (price * 100), useBy, isFrozen));
            }
        }

        Product product = produce.get(produce.size() - 1);

        System.out.printf("\n%s successfully added for $%s.\n", product.name, product.getFormattedPrice());
    }

    public boolean isExpired(Product product){
        long epochDays = LocalDate.now().toEpochDay();
        long productEpoch = LocalDate.parse(product.useBy).toEpochDay();

        return productEpoch < epochDays;
    }
}
