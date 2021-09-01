package com.utilities;

public class UI {

    public static void listerator(String... listElements){
        int listNum = 0;
        for(String option : listElements){
            System.out.printf("%d. %s\n", ++listNum, option);
        }
    }
    public static void listerator(String prompt, String... listElements){
        System.out.println(prompt);
        int listNum = 0;
        for(String option : listElements){
            System.out.printf("%d. %s\n", ++listNum, option);
        }
    }
}
