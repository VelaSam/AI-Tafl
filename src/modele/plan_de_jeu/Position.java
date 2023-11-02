package modele.plan_de_jeu;

public class Position {
    private Integer i;
    private Integer j;
    private String boardPosition;



    public Integer getI() { return this.i; }
    public Integer getJ() { return this.j; }
    public Integer getBoardPosition() { return this.boardPosition; }

    public Position(Integer i, Integer j){
        this.i = i;     
        this.j = j;     
        this.boardPosition = Board.getLetterValue(i) + this.j;
    }

}
