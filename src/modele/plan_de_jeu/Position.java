package modele.plan_de_jeu;

import modele.helpers.Helpers;

public class Position {
    private final int x;
    private final int y;
    private final String boardPosition;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.boardPosition = Helpers.getBoardPositionFromCoordinates(this.x, this.y);
        // System.out.println("THIS IS THE POSITION:" + "I: " + x + ", J: " + y + " POSITION: " +
        // this.boardPosition);
    }

    public Position(String boardPosition) {
        this.boardPosition = boardPosition;
        this.x = Helpers.getNumberValue(String.valueOf(boardPosition.charAt(0)));
        this.y = Integer.parseInt(boardPosition.substring(1)) - 1;
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

    public String toString() {
        return boardPosition;
    }
}
