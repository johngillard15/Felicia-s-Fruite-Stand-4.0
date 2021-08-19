package com.company;

import com.utilities.ANSI;
import com.utilities.CLI;
import com.utilities.Input;

public class Console {
    Store store;

    public Console(){
        store = new Store("Ye Olde Felicia's Fruite Stande");
    }

    public Console(String storeName){
        store = new Store(storeName);
    }

    public void menu(){
        System.out.println("Loading cashier interface...\n");

        boolean exit = false;
        do{
            System.out.printf("\n\n~~~ %s ~~~\n", store.name);

            int listNum = 0;
            System.out.printf("\t%d. CHECK INVENTORY\n", ++listNum);
            System.out.printf("\t%d. SHOW STORE BALANCE\n", ++listNum);
            System.out.printf("\t%d. ADD PRODUCT\n", ++listNum);
            System.out.printf("\t%d. %sSELL PRODUCT\n",
                    ++listNum, store.getInvSize() == 0 ? ANSI.RED + "-NO INVENTORY- " + ANSI.RESET : "");
            System.out.printf("\t%d. CLEAN INVENTORY\n", ++listNum);
            System.out.printf("\t%d. EXIT\n", ++listNum);

            System.out.print("choice: ");
            switch(Input.getInt(listNum)){
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
                        cleanInventory();
                }
                case 6 -> exit = true;
            }

            if(!exit) CLI.pause();
        }while(!exit);

        System.out.println("\nExiting cashier interface...");
    }

    public void viewProduce(){
        if(store.getInvSize() == 0)
            System.out.println("\nThere is no inventory to show.");
        else{
            System.out.println("\nShowing current inventory...");
            store.printProduce();
        }
    }

    public void addProduct(){
        store.getProductInfo();
    }

    public void sellProduct(){
        viewProduce();

        System.out.println("Select Product:");
        System.out.print("choice: ");
        int choice = Input.getInt(store.getInvSize());

        Product product = store.getProduct(choice - 1);
        store.sellProduct(product);

        System.out.printf("\n%s successfully sold for $%s.\n", product.getName(), store.getMarkupPrice(product));
        showStoreBalance();
    }

    // (maybe?) iterate through produce and discard expired products
    public void cleanInventory(){
        viewProduce();

        System.out.println("Select Product:");
        System.out.print("choice: ");
        int choice = Input.getInt(store.getInvSize());

        Product product = store.getProduct(choice - 1);

        System.out.printf("\n%s has been discarded.\n", product.getName());
        store.removeProduct(product);
    }

    public void showStoreBalance(){
        System.out.printf("\nCurrent store balance: $%s\n", store.getFormattedBalance());
    }
}
