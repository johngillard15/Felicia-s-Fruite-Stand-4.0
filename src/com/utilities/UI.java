package com.utilities;

public class UI { // TODO: make different listing methods (bullet points, letters, numerals, etc.)

    public static void listerator(String... listElements){
        listerator(1, 0, listElements);
    }

    /**
     * <p>1 = number, 2, letter</p>
     * <p>subLevel refers to the level of indentation before each list element</p>
     * @param listType
     * @param subLevel
     * @param listElements
     */
    public static void listerator(int listType, int subLevel, String... listElements){
        String subLevelIndent = "";
        for(int i = 0; i < subLevel; i++)
            subLevelIndent += "\t";

        switch(listType){
            case 1 -> { // number
                int listNum = 0;
                for(String option : listElements){
                    System.out.printf("%s%d. %s\n", subLevelIndent, ++listNum, option);
                }
            }
            case 2 -> { // letter
                char c = 'a';
                for(String option : listElements){
                    System.out.printf("%s%c. %s\n", subLevelIndent, ++c, option);
                }
            }

            default ->{
                throw new IllegalStateException("Invalid list type: " + listType);
            }
        }
    }
}
