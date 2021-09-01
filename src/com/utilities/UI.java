package com.utilities;

public class UI { // TODO: make different listing methods (bullet points, letters, numerals, etc.)

    public static void listerator(String... listElements){
        listerator(1, listElements);
    }
    public static void listerator(int listType, String... listElements){
        switch(listType){
            case 1 -> { // number
                int listNum = 0;
                for(String option : listElements){
                    System.out.printf("%d. %s\n", ++listNum, option);
                }
            }
            case 2 -> { // letter
                char c = 'a';
                for(String option : listElements){
                    System.out.printf("%c. %s\n", ++c, option);
                }
            }

            default ->{
                throw new IllegalStateException("Invalid list type: " + listType);
            }
        }
    }
}
