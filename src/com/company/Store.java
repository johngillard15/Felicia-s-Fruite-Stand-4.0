package com.company;

import com.company.Product;
import com.utilities.Input;

import java.util.ArrayList;
import java.util.List;

public class Store {
    public final String name;
    public final List<Product> produce = new ArrayList<>();
    private int balance = 1000000;
    private static final double MARKUP = 0.3;

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
        for(Product product : produce)
            System.out.printf("\t%d. %s - $%s\n", ++listNum, product.name, getMarkupPrice(product));
    }

    public void getProductInfo(){
        System.out.println("\nWhat type of produce are you adding?: ");
        System.out.println("Type: ");
        System.out.println("\t1. Fruit");
        System.out.println("\t2. Meat");
        int type = Input.getInt();

        System.out.println("Enter product name: ");
        String name = Input.getString();
        System.out.println("Enter product price (no decimals): ");
        int price = Input.getInt();

        switch(type){
            case 1 -> addProduct(new Fruit(name, price));
            case 2 -> addProduct(new Meat(name, price));
        }

        Product product = produce.get(produce.size() - 1);

        System.out.printf("\n%s successfully added for $%s.\n", product.name, product.getFormattedPrice());
    }
}
