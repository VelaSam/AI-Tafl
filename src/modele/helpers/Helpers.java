package modele.helpers;

import java.util.HashMap;
import java.util.Map;

public class Helpers {
    public static Map<Integer, String> letters;

    static {
        letters = new HashMap<>();
        letters.put(0, "A");
        letters.put(1, "B");
        letters.put(2, "C");
        letters.put(3, "D");
        letters.put(4, "E");
        letters.put(5, "F");
        letters.put(6, "G");
        letters.put(7, "H");
        letters.put(8, "I");
        letters.put(9, "J");
        letters.put(10, "K");
        letters.put(11, "L");
        letters.put(12, "M");
    }

    public static String getLetterValue(Integer key) {
        return letters.get(key);
    }
}
