package modele.plan_de_jeu;

import modele.helpers.Helpers;

public class Position {
    private final int x;
    private final int y;
    private final String boardPosition;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.boardPosition = Helpers.getLetterValue(x) + (this.y + 1);
//        System.out.println("THIS IS THE POSITION:" + "I: " + x + ", J: " + y + " POSITION: " + this.boardPosition);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getBoardPosition() {
        return boardPosition;
    }

    public String toString(){
        return boardPosition;
    }
}
