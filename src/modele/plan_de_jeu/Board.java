package modele.plan_de_jeu;

import java.util.HashMap;
import java.util.Map;

public class Board {

    private int[][] tiles;

    private Map<Integer, String> letters;
    {
        letters = new HashMap<>();
        letters.put(1, "A");
        letters.put(2, "B");
        letters.put(3, "C");
        letters.put(4, "D");
        letters.put(5, "E");
        letters.put(6, "F");
        letters.put(7, "G");
        letters.put(8, "H");
        letters.put(9, "I");
        letters.put(10, "J");
        letters.put(11, "K");
        letters.put(12, "L");
        letters.put(13, "M");
    }


    public Map<Integer, String> getLetters() {
        return letters;
    }

    public String getLetterValue(Integer key) { return this.letters.get(key); }
}
