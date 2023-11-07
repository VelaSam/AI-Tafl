package modele.plan_de_jeu;

import modele.helpers.Helpers;

public class Position {
    private final int i;
    private final int j;
    private final String boardPosition;


    public Position(int i, int j) {
        this.i = i;
        this.j = j;
        this.boardPosition = Helpers.getLetterValue(j) + this.i;
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
