package modele.plan_de_jeu;

import modele.helpers.Helpers;

public class Position {
    private int i;
    private int j;
    private String boardPosition;

    public Integer getI() { return this.i; }
    public Integer getJ() { return this.j; }
    public String getBoardPosition() { return this.boardPosition; }

    public Position(Integer i, Integer j){
        this.i = i;     
        this.j = j;     
        this.boardPosition = Helpers.getLetterValue(j) + this.i;
    }

}