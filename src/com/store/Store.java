package com.store;

import com.product.Fruit;
import com.product.Meat;
import com.product.Product;
import com.utilities.ANSI;
import com.utilities.FileHandling;
import com.utilities.Input;
import com.utilities.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

// TODO: finish getting selling large quantities
// TODO: justify list output based on length of name
public class Store {
    public final String name;
    private List<Product> produce = new ArrayList<>();
    private int balance = 1_000_00; // balance / 100 = true balance
    private static final double MARKUP = 0.3D; // markup percentage for profits
    public static final String[] productTypes = {
            "Fruit",
            "Meat"
    };

    public Store(String name){
        this.name = name;
    }

    public void readInvFile(){
        final int INIT_BALANCE = 1_000_00;

        try{
            File file = new File("object.txt");
            if(file.createNewFile())
                FileHandling.write(produce);
        }
        catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try{
            File file = new File("inventory.txt");
            Scanner scan = new Scanner(file);

            int savedBalance;
            try{
                savedBalance = Integer.parseInt(scan.nextLine());
            }
            catch(NoSuchElementException | NumberFormatException e){
                savedBalance = INIT_BALANCE;
            }

            produce = (ArrayList<Product>) FileHandling.read();

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
            File file = new File("object.txt");
            if(file.delete())
                file.createNewFile();
        }
        catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter("inventory.txt")){
            pw.print(balance);
            pw.flush();
            pw.close();

            FileHandling.write(produce);
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

    public void setBalance(int newBalance){
        balance = newBalance;
    }

    public String getFormattedBalance(){
        return String.format("%,.2f", (double) getBalance() / 100);
    }

    private void deposit(int deposit){
        setBalance(balance + deposit);
    }

    private void withdraw(int cost){
        setBalance(balance - cost);
    }

    public void printProduce(){
        String[] produceInfo = new String[produce.size()];

        for(int i = 0; i < produce.size(); i++){
            Product product = produce.get(i);

            String ANSI_COLOR = isExpired(product) ? ANSI.RED : "";
            String useBy = ANSI_COLOR + product.useBy + ANSI.RESET;

            double wholesale = (double)product.getQuantity() * ((double)product.price/100 + ((double)product.price/100) * MARKUP);

            produceInfo[i] = String.format("%s, use by: %s\n" +
                            "\twholesale: $%,.2f ($%,.2f x%s)",
                    product, useBy,
                    wholesale, getMarkupPrice(product), product.getQuantity());
        }

        System.out.println("\n~ List of Produce ~");
        UI.listerator(produceInfo);
    }

    public void addNewItem(){
        System.out.println("\nWhat type of produce are you adding? ");
        UI.listerator(productTypes);
        System.out.print("type ");
        int type = Input.getInt(1, productTypes.length);

        System.out.println("\nEnter product name:");
        System.out.print("name ");
        String name = Input.getString();

        System.out.println("Enter product price:");
        System.out.print("price ");
        double price = Input.getDouble(0);

        System.out.println("\nEnter quantity to add:");
        System.out.print("quantity ");
        int quantity = Input.getInt(1);

        System.out.println("\nPlease enter thr product's expiration date");
        System.out.print("Year (yyyy) ");
        String year = Input.getString();
        System.out.print("Month (MM) ");
        String month = Input.getString();
        System.out.print("Day (dd) ");
        String day = Input.getString();

        String useBy = String.format("%s-%s-%s", year, month, day);

        switch(type){
            case 1 -> {
                System.out.printf("\nIs \"%s\" in season?\n", name);
                UI.listerator("In season", "Not in season");
                System.out.print("choice ");
                boolean inSeason = Input.getInt(1,2) == 1;
                addProduct(new Fruit(name, (int) (price * 100), useBy, quantity, inSeason));
            }

            case 2 -> {
                System.out.printf("Is %s frozen?\n", name);
                UI.listerator("Frozen", "Fresh");
                System.out.print("choice ");
                boolean isFrozen = Input.getInt(1, 2) == 1;
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
