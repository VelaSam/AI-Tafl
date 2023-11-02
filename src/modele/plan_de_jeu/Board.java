package modele.plan_de_jeu;

public class Board {

    private int[][] tiles;

    private static Map<Integer, String> letters;
    {
        letters = new HashMap<>();
        letter.put(1, "A");
        letter.put(2, "B");
        letter.put(3, "C");
        letter.put(4, "D");
        letter.put(5, "E");
        letter.put(6, "F");
        letter.put(7, "G");
        letter.put(8, "H");
        letter.put(9, "I");
        letter.put(10, "J");
        letter.put(11, "K");
        letter.put(12, "L");
        letter.put(13, "M");
    }

    public static String getLetterValue(Integer key) { return this.letters.get(key); }
}
