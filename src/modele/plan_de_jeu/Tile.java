package modele.plan_de_jeu;

public class Tile {

    private TileState state;
    private final boolean isX;
    private Position position;

    public Tile(Position position, int tileState) {
        this.position = position;
        this.state = TileState.getTileState(tileState);

        isX = isXTile(position);
    }

    private boolean isXTile(Position position) {
        if (position.getI() == 12 || position.getI() == 0) {
            if (position.getJ() == 0) {
                return true;
            }
            return position.getJ() == 12;
        } else
            return position.getI() == 6 && position.getJ() == 6;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public boolean isX() {
        return isX;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
