package modele.plan_de_jeu;

public class Board {

    public final int WIDTH = 13;
    private Tile[] tiles;

    public Board(String[] boardValues) {
        initializeNewBoard(boardValues);
    }

    public void initializeNewBoard(String[] boardValues) {
        int x=0,y=0;
        for(int i=0; i<boardValues.length;i++){
            tiles[i] = new Tile(new Position(x, y), Integer.parseInt(boardValues[i]));
            x++;
            if(x == WIDTH){
                x = 0;
                y++;
            }
        }
    }
}
