package com.console;

import com.product.Fruit;
import com.product.Meat;
import com.product.Product;
import com.store.Store;
import com.utilities.ANSI;
import com.utilities.CLI;
import com.utilities.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {
    private final Store store;
    public static final LocalDate todaysDate = LocalDate.now();

    public Console(){
        store = new Store("Ye Olde Felicia's Fruite Stande");
    }

    public Console(String storeName){
        store = new Store(storeName);
    }

    public void menu(){
        System.out.println("Loading cashier interface...\n");

        readInvFile();

        boolean exit = false;
        do{
            System.out.printf("\n\n~~~ %s ~~~\n", store.name);
            String formattedDate = todaysDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
            System.out.printf("Today's date is %s\n", formattedDate);

            int listNum = 0;
            System.out.printf("\t%d. CHECK INVENTORY\n", ++listNum);
            System.out.printf("\t%d. SHOW STORE BALANCE\n", ++listNum);
            System.out.printf("\t%d. ADD PRODUCT\n", ++listNum);
            System.out.printf("\t%d. %sSELL PRODUCT\n",
                    ++listNum, store.getInvSize() == 0 ? ANSI.RED + "-NO INVENTORY- " + ANSI.RESET : "");
            System.out.printf("\t%d. SHRINK INVENTORY\n", ++listNum);
            System.out.printf("\t%d. CLEAN INVENTORY\n", ++listNum);
            System.out.printf("\t%d. EXIT\n", ++listNum);

            System.out.print("choice: ");
            switch(Input.getInt(1, listNum)){
                case 1 -> viewProduce();
                case 2 -> showStoreBalance();
                case 3 -> addProduct();
                case 4 -> {
                    if(store.getInvSize() == 0)
                        System.out.println("\nThere are no products to sell.");
                    else
                        sellProduct();
                }
                case 5 -> {
                    if(store.getInvSize() == 0)
                        System.out.println("\nThere are no products to discard.");
                    else
                        shrinkInventory();
                }
                case 6 -> cleanInventory();
                case 7 -> exit = true;
            }

            if(!exit) CLI.pause();
        }while(!exit);

        System.out.println("\nExiting cashier interface...");

        writeInvFile();
    }

    public void readInvFile(){
        final int INIT_BALANCE = 1_000_00;

        try{
            File file = new File("inventory.txt");
            Scanner scan = new Scanner(file);

            int savedBalance;
            try{
                savedBalance = Integer.parseInt(scan.nextLine());
            }
            catch(NoSuchElementException e){
                scan.nextLine();
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
                    store.addProduct(new Fruit(name, price, useBy, quantity, typeSpecific));
                else
                    store.addProduct(new Meat(name, price, useBy, quantity, typeSpecific));
            }

            store.setBalance(savedBalance);
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

            fileWriter.write(store.getBalance() + "\n");

            for(int i = 0; i < store.getInvSize(); i++){
                Product product = store.getProduct(i);

                String type = product.getClass().getSimpleName();
                boolean typeSpecific;

                if(product instanceof Fruit)
                    typeSpecific = ((Fruit) product).inSeason;
                else
                    typeSpecific = ((Meat) product).isFrozen;

                fileWriter.write(String.format("%s\n%s\n%s\n%s\n%s\n%s\n",
                        type, product.name, product.price, product.useBy, product.getQuantity(), typeSpecific));
            }

            fileWriter.flush();
            fileWriter.close();
        }
        catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void viewProduce(){
        if(store.getInvSize() == 0)
            System.out.println("\nThere is no inventory to show.");
        else{
            System.out.println("\nShowing current inventory...");
            store.printProduce();
        }
    }

    private void addProduct(){
        store.addNewItem();
    }

    private void sellProduct(){
        viewProduce();

        System.out.println("Select Product:");
        System.out.print("choice: ");
        int choice = Input.getInt(1, store.getInvSize());

        Product product = store.getProduct(choice - 1);

        System.out.printf("\nHow many \"%s\" would you like to sell?\n", product.name);
        System.out.print("quantity: ");
        int saleQuantity = Input.getInt(product.getQuantity());

        store.sellProduct(product, saleQuantity);

        System.out.printf("\n%d %s successfully sold for $%,.2f.\n",
                saleQuantity, product.name, store.getMarkupPrice(product) * product.getQuantity());

        showStoreBalance();
    }

    // (maybe?) iterate through produce and discard expired products
    private void shrinkInventory(){
        viewProduce();

        System.out.println("Select Product:");
        System.out.print("choice: ");
        int choice = Input.getInt(1, store.getInvSize());

        Product product = store.getProduct(choice - 1);

        System.out.printf("\n%s has been discarded.\n", product.name);
        store.removeProduct(product);
    }

    private void cleanInventory(){
        System.out.println("\nCleaning inventory...\n");

        int itemsThrownOut = 0;

        for(int i = 0; i < store.getInvSize(); i++){
            Product product = store.getProduct(i);

            if(store.isExpired(product)){
                System.out.printf("%s expired on %s\n", product.name, product.useBy);

                store.removeProduct(product);
                ++itemsThrownOut;
            }
        }

        System.out.printf("\n%s %s discarded.\n",
                itemsThrownOut, itemsThrownOut == 1 ? "item was" : "items were");
    }

    public void showStoreBalance(){
        System.out.printf("\nCurrent store balance: $%s\n", store.getFormattedBalance());
    }
}
