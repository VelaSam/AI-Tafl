package modele.plan_de_jeu;

public class Tile {

    private TileState state;
    private final boolean isMarkedX;
    private Position position;

    private final int WIDTH = 13;

    public Tile(Position position, int tileState) {
        this.position = position;
        this.state = TileState.getTileState(tileState);

        isMarkedX = isTileMarkedX(position);
    }

    public Tile(Position position, TileState tileState){
        this.position = position;
        this.state = tileState;

        isMarkedX = isTileMarkedX(position);
    }

    private boolean isTileMarkedX(Position position) {
        if (position.getX() == 12 || position.getX() == 0) {
            if (position.getY() == 0) {
                return true;
            }
            return position.getY() == WIDTH - 1;
        } else
            return position.getX() == WIDTH/2 && position.getY() == WIDTH/2;
    }

    public boolean isOppositeColorOf(Tile otherTile){

        if(this.state == TileState.NOIR || this.state == TileState.ROI_NOIR){
            return otherTile.state == TileState.ROUGE;
        }
        if(this.state == TileState.ROUGE){
            return otherTile.state == TileState.ROI_NOIR || otherTile.state == TileState.NOIR;
        } else {
            return false;
        }
        return false;
    }

    //risque de perte de vitesse si passe tiles par copie, correct si passe par reference
    public Tile getNextRight(Tile[][] tiles){
        //null si on sort du tableau
        //is > or >= ????
        if(this.getX() + 1 >= WIDTH){
            return null;
        }
        return tiles[this.getX() + 1][this.getY()];
    }

    public Tile getNextLeft(Tile[][] tiles){
        //retournera null si on sort du tableau
        if(this.getX() - 1 < 0){
            return null;
        }
        return tiles[this.getX()-1][this.getY()];
    }
    public Tile getNextUp(Tile[][] tiles){
        //retournera null si on sort du tableau
        if(this.getY() + 1 >= WIDTH){
            return null;
        }
        return tiles[this.getX()][this.getY() + 1];
    }
    public Tile getNextDown(Tile[][] tiles){
        //retournera null si on sort du tableau
        if(this.getY() - 1 < 0){
            return null;
        }
        return tiles[this.getX()][this.getY() - 1];
    }


    public int getX(){
        return this.position.getX();
    }

    public int getY(){
        return this.position.getY();
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


    public String singleCharacterStateString(){
        if(this.isMarkedX && this.state == TileState.EMPTY){
            return "X";
        }
        else
            return this.state.toString();
    }

}
