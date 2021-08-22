package com.store;

import com.product.Fruit;
import com.product.Meat;
import com.product.Product;
import com.utilities.ANSI;
import com.utilities.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

// TODO: finish getting selling large quantities
// TODO: justify list output based on length of name
public class Store {
    private static final int INIT_BALANCE = 1_000_00;
    public final String name;
    private final List<Product> produce = new ArrayList<>();
    private int balance = 1_000_00; // balance / 100 = true balance
    private static final double MARKUP = 0.3; // markup percentage for profits

    public Store(){
        name = "Felicia's Fruit Stand";
    }

    public Store(String name){
        this.name = name;
    }

    public void readInvFile(){
        try{
            File file = new File("inventory.txt");
            Scanner scan = new Scanner(file);

            int savedBalance;
            try{
                savedBalance = Integer.parseInt(scan.nextLine());
            }
            catch(NoSuchElementException e){
                savedBalance = INIT_BALANCE;
            }

            while(scan.hasNext()){
                String type = scan.nextLine();
                String name = scan.nextLine();
                int price = Integer.parseInt(scan.nextLine());
                String useBy = scan.nextLine();
                int quantity = Integer.parseInt(scan.nextLine());
                boolean typeSpecific = Boolean.parseBoolean(scan.nextLine());

                if(type.equals("Fruit"))
                    addProduct(new Fruit(name, price, useBy, quantity, typeSpecific));
                else
                    addProduct(new Meat(name, price, useBy, quantity, typeSpecific));
            }

            balance = savedBalance;
        } catch (FileNotFoundException e){
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    public void writeInvFile(){
        try{
            File file = new File("inventory.txt");
            if(file.delete())
                file.createNewFile();
        }
        catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try{
            FileWriter fileWriter = new FileWriter("inventory.txt");

            fileWriter.write(balance + "\n");

            for(Product product : produce){
                String type = product.getClass().getSimpleName();
                boolean typeSpecific;

                if(product instanceof Fruit)
                    typeSpecific = ((Fruit) product).inSeason;
                else
                    typeSpecific = ((Meat) product).isFrozen;

                fileWriter.write(String.format("""
                                %s
                                %s
                                %s
                                %s
                                %s
                                %s
                                """,
                        type,
                        product.name,
                        product.price,
                        product.useBy,
                        product.getQuantity(),
                        typeSpecific)
                );
            }

            fileWriter.flush();
            fileWriter.close();
        }
        catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
        withdraw(product.price * product.getQuantity());
    }

    public void sellProduct(Product product){
        sellProduct(product, 1);
    }

    public void sellProduct(Product product, int saleQuantity){
        int salePrice = (int) ((double)product.price * MARKUP) + product.price;
        deposit(salePrice * saleQuantity);

        if(product.getQuantity() == 1)
            produce.remove(product);
        else
            decreaseStock(product, saleQuantity);
    }

    public double getMarkupPrice(Product product){
        int salePrice = (int) ((double)product.price * MARKUP) + product.price;

        return (double) salePrice / 100;
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

            double wholesale = (double)product.getQuantity() * ((double)product.price/100 + ((double)product.price/100) * MARKUP);
            System.out.printf("\t%d. %s (%s), use by: %s - $%,.2f ($%,.2f x%s)\n",
                    ++listNum, product.name, typeSpecific, useBy, wholesale, getMarkupPrice(product),
                    product.getQuantity());
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

        System.out.println("\nEnter quantity to add:");
        System.out.print("quantity: ");
        int quantity = Input.getInt();

        System.out.println("\nPlease enter thr product's expiration date");
        System.out.print("Year (yyyy): ");
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
                addProduct(new Fruit(name, (int) (price * 100), useBy, quantity, inSeason));
            }

            case 2 -> {
                System.out.printf("Is %s frozen?\n", name);
                System.out.println("\t1. Frozen");
                System.out.println("\t2. Not Frozen");
                System.out.print("choice: ");
                boolean isFrozen = Input.getInt(2) == 1;
                addProduct(new Meat(name, (int) (price * 100), useBy, quantity, isFrozen));
            }
        }

        Product product = produce.get(produce.size() - 1);

        System.out.printf("\n%s successfully added for $%s.\n", product.name, product.getFormattedPrice());
    }

    private void decreaseStock(Product product){
        decreaseStock(product, 1);
    }
    private void decreaseStock(Product product, int amount){
        product.setQuantity(product.getQuantity() - amount);
    }

    public boolean isExpired(Product product){
        long epochDays = LocalDate.now().toEpochDay();
        long productEpoch = LocalDate.parse(product.useBy).toEpochDay();

        return productEpoch < epochDays;
    }
}
