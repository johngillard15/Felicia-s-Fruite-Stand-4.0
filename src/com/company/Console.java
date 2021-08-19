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
            System.out.printf("\t%d. %s SELL PRODUCT\n",
                    ++listNum, store.produce.size() == 0 ? ANSI.RED + "-NO INVENTORY-" + ANSI.RESET : "");
            System.out.printf("\t%d. CLEAN INVENTORY\n", ++listNum);
            System.out.printf("\t%d. EXIT\n", ++listNum);

            switch(Input.getInt(listNum)){
                case 1 -> viewProduce();
                case 2 -> showStoreBalance();
                case 3 -> addProduct();
                case 4 -> {
                    if(store.produce.size() == 0)
                        System.out.println("\nThere are no products to sell.");
                    else
                        sellProduct();
                }
                case 5 -> cleanInventory();
                case 6 -> exit = true;
            }

            CLI.pause();
        }while(!exit);

        System.out.println("\nExiting cashier interface...");
    }

    public void viewProduce(){
        if(store.produce.size() == 0)
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

        int choice = Input.getInt(store.getProduceQuantity());

        Product product = store.produce.get(choice - 1);
        store.sellProduct(product);

        System.out.printf("\n%s successfully sold for $%s.\n", product.name, store.getMarkupPrice(product));
        showStoreBalance();
    }

    // (maybe?) iterate through produce and discard expired products
    public void cleanInventory(){
        System.out.println("Clean?");
    }

    public void showStoreBalance(){
        System.out.printf("\nCurrent store balance: $%s\n", store.getFormattedBalance());
    }
}
