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
            case 2 -> { // letter TODO: maybe have array that will store chars, then iterate through it backwards to print list
                /*
                char[] charArr = new char[listElements.length];
                charArr[0] = 'a';
                String listValue = charArr[0];

                String getListValue(){
                    String newValue = "";
                    for(int i = 0; i < charArr.length; i++){
                        if(charArr[i] >= 'z' && (i + 1 != charArr.length)){
                            charArr[i] = 'a';
                            ++charArr[i + 1];
                        }
                        else
                            ++charArr[i];

                        newValue += charArr[i];
                    }

                    return newValue;
                }

                for(String option : listElements){
                    listValue = getListValue();
                    System.out.printf("%c. %s\n", listValue, option);
                }
                */
                char c = 'a';
                for(String option : listElements){
                    System.out.printf("%c. %s\n", ++c, option);
                }
            }
            case 3 -> { // numeral

            }
            case 4 -> { // bullet

            }

            default ->{
                throw new IllegalStateException("Invalid list type: " + listType);
            }
        }
    }
}
