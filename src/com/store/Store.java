package com.store;

import com.product.Product;
import com.utilities.ANSI;
import com.utilities.FileHandling;
import com.utilities.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

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

        decreaseStock(product, saleQuantity);

        if(product.getQuantity() <= 0)
            produce.remove(product);
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
