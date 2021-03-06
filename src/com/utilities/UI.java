package com.utilities;

public class UI {

    /**
     * <p>Creates a list out of the provided String arguments. Prints a numbered list by default.</p>
     *
     * @param listElements the information to listerate
     */
    public static void listerator(String... listElements){
        listerator(1, 0, listElements);
    }
    /**
     * <p>Creates a list out of the provided String arguments. Supports multiple list types.</p>
     *
     * @param listType the kind of list to create (1 - number, 2 - letter, 3 - numeral, 4 - bullet)
     * @param listElements the information to listerate
     */
    public static void listerator(int listType, String... listElements){
        listerator(listType, 0, listElements);
    }
    /**
     * <p>Creates a list out of the provided String arguments. Supports multiple list types and indentation levels.</p>
     *
     * @param listType the kind of list to create (1 - number, 2 - letter, 3 - numeral, 4 - bullet)
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
                for(int i = 0; i < listElements.length; i++){
                    System.out.printf("%s%s. %s\n", subLevelIndent, getLetterValue(i), listElements[i]);
                }
            }
            case 3 -> { // numeral

            }
            case 4 -> { // bullet
                for(String option : listElements){
                    System.out.printf("%s? %s\n", subLevelIndent, option);
                }
            }

            default -> throw new IllegalStateException("Invalid list type: " + listType);
        }
    }

    /**
     * Converts a number to a String of letters.
     *
     * @param listNum the number to convert
     * @return a String of letters representing the passed number value
     */
    public static String getLetterValue(int listNum){
        int nextLetter = listNum / 26;
        int letterNum = listNum % 26;
        char letter = (char)((int)'A' + letterNum);

        return (nextLetter == 0 ? "" : getLetterValue(nextLetter - 1)) + letter;
    }
}
