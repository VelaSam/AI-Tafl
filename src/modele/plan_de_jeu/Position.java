package modele.plan_de_jeu;

import modele.helpers.Helpers;

public class Position {
    private final int i;
    private final int j;
    private final String boardPosition;


    public Position(int i, int j) {
        this.i = i;
        this.j = j;
        this.boardPosition = Helpers.getLetterValue(i) + (this.j + 1);
//        System.out.println("THIS IS THE POSITION:" + "I: " + i + ", J: " + j + " POSITION: " + this.boardPosition);
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public String getBoardPosition() {
        return boardPosition;
    }

    public String toString(){
        return boardPosition;
    }
}
