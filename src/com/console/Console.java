package com.console;

import com.product.Fruit;
import com.product.Meat;
import com.product.Product;
import com.store.Store;
import com.utilities.CLI;
import com.utilities.Input;
import com.utilities.UI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Console {
    private final Store store;
    public static final LocalDate todaysDate = LocalDate.now();
    private static final String[] CONSOLE_OPTIONS = {
            "CHECK INVENTORY",
            "SHOW STORE BALANCE",
            "ADD PRODUCT",
            "SELL PRODUCT",
            "SHRINK INVENTORY",
            "CLEAN INVENTORY",
            "EXIT"
    };

    public Console(){
        store = new Store("Ye Olde Felicia's Fruite Stande");
    }

    public Console(String storeName){
        store = new Store(storeName);
    }

    public void menu(){
        store.readInvFile();

        System.out.println("Loading cashier interface...\n");

        boolean exit = false;
        do{
            System.out.printf("\n\n~~~ %s ~~~\n", store.name);
            String formattedDate = todaysDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
            System.out.printf("Today's date is %s\n", formattedDate);

            UI.listerator(CONSOLE_OPTIONS);

            System.out.print("choice: ");
            switch(Input.getInt(1, CONSOLE_OPTIONS.length)){
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
                case 7 -> {
                    System.out.println("\nAre you sure you want to exit? (y/n)");
                    if(Input.getString(false, "y", "n").equalsIgnoreCase("y"))
                        exit = true;
                }
            }

            if(!exit) CLI.pause();
        }while(!exit);

        System.out.println("\nExiting cashier interface...");

        store.writeInvFile();
    }

    private void viewProduce(){
        if(store.getInvSize() == 0)
            System.out.println("\nThere is no inventory to show.");
        else{
            System.out.println("\nShowing current inventory...");
            store.printProduce();
        }
    }

    public void addProduct(){
        System.out.println("\nWhat type of produce are you adding? ");
        UI.listerator(Store.productTypes);
        System.out.print("type ");
        int type = Input.getInt(1, Store.productTypes.length);

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

        Product product = switch(type){
            case 1 -> {
                System.out.printf("\nIs \"%s\" in season?\n", name);
                UI.listerator("In season", "Not in season");
                System.out.print("choice ");
                boolean inSeason = Input.getBoolean("1", "2");

                yield new Fruit(name, (int) (price * 100), useBy, quantity, inSeason);
            }

            case 2 -> {
                System.out.printf("Is %s frozen?\n", name);
                UI.listerator("Frozen", "Fresh");
                System.out.print("choice ");
                boolean isFrozen = Input.getBoolean("1", "2");

                yield new Meat(name, (int) (price * 100), useBy, quantity, isFrozen);
            }

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        store.addProduct(product);

        System.out.printf("\n%s successfully added for $%s.\n", product.name, product.getFormattedPrice());
    }

    private void sellProduct(){
        viewProduce();

        System.out.println("Select Product:");
        System.out.print("choice: ");
        int choice = Input.getInt(1, store.getInvSize());

        Product product = store.getProduct(choice - 1);

        System.out.printf("\nHow many \"%s\" would you like to sell?\n", product.name);
        System.out.print("quantity: ");
        int saleQuantity = Input.getInt(1, product.getQuantity());

        store.sellProduct(product, saleQuantity);

        System.out.printf("\n%d %s successfully sold for $%,.2f.\n",
                saleQuantity, product.name, store.getMarkupPrice(product) * saleQuantity);

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
