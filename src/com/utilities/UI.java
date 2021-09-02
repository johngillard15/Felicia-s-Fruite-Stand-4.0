package com.utilities;

public class UI { // TODO: make different listing methods (bullet points, letters, numerals, etc.)

    public static void listerator(String... listElements){
        listerator(1, 0, listElements);
    }

    /**
     * <p></p>
     *
     * @param listType the kind of list to create (1 - number, 2 - letter)
     * @param subLevel level of indentation to apply to the list
     * @param listElements the information to listerate
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

            default -> throw new IllegalStateException("Invalid list type: " + listType);
        }
    }
}
