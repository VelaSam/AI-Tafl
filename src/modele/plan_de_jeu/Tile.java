package modele.plan_de_jeu;

public class Tile {

    private TileState state;
    private boolean isX;

    Position position;

    public Tile(Position position){
        if(position.getI() == 12 || position.getI() == 0){
            if(position.getJ() == 0){
                isX = true;
            }
            if(position.getJ() == 12){
                isX = true;
            }
        } else if(position.getI() == 6 && position.getJ() == 6){
            isX = true;
        } else {
            isX = false;
        }


    }

}
