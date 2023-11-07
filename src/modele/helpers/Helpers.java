package modele.helpers;

import java.util.HashMap;
import java.util.Map;

public class Helpers {
    public static Map<Integer, String> letterFromNum;
    public static Map<String, Integer> numFromLetter;


    static {
        letterFromNum = new HashMap<>();
        letterFromNum.put(0, "A");
        letterFromNum.put(1, "B");
        letterFromNum.put(2, "C");
        letterFromNum.put(3, "D");
        letterFromNum.put(4, "E");
        letterFromNum.put(5, "F");
        letterFromNum.put(6, "G");
        letterFromNum.put(7, "H");
        letterFromNum.put(8, "I");
        letterFromNum.put(9, "J");
        letterFromNum.put(10, "K");
        letterFromNum.put(11, "L");
        letterFromNum.put(12, "M");

        numFromLetter = new HashMap<>();
        numFromLetter.put("A", 0);
        numFromLetter.put("B", 1);
        numFromLetter.put("C", 2);
        numFromLetter.put("D", 3);
        numFromLetter.put("E", 4);
        numFromLetter.put("F", 5);
        numFromLetter.put("G", 6);
        numFromLetter.put("H", 7);
        numFromLetter.put("I", 8);
        numFromLetter.put("J", 9);
        numFromLetter.put("K", 10);
        numFromLetter.put("L", 11);
        numFromLetter.put("M", 12);
    }

    public static String getLetterValue(Integer key) {
        return letterFromNum.get(key);
    }

    public static int getNumberValue(String key){return numFromLetter.get(key);}
}
