package modele.plan_de_jeu;

public class Tile {

    private TileState state;
    private final boolean isMarkedX;
    private Position position;

    public Tile(Position position, int tileState) {
        this.position = position;
        this.state = TileState.getTileState(tileState);

        isMarkedX = isTileMarkedX(position);
    }

    private boolean isTileMarkedX(Position position) {
        if (position.getX() == 12 || position.getX() == 0) {
            if (position.getY() == 0) {
                return true;
            }
            return position.getY() == 12;
        } else
            return position.getX() == 6 && position.getY() == 6;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public boolean isMarkedX() {
        return isMarkedX;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String toString(){

        if(this.state == TileState.ROI_NOIR){
            return "ROI";
        }else{
            return this.position.toString();
        }

    }

}
