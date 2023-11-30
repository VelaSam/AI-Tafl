package modele.plan_de_jeu;

public class BeforeMoveState {

    private TileState fromTileState;
    private TileState toTileState;
    private String fromBoardPosition;
    private String toBoardPosition;
    private boolean isKill;

    /**
     * Creates a memento of the state of the board before a move is made.
     * @param fromTileState The state of the tile starting the move.
     * @param toTileState The state of the tile of the arriving position (EMPTY or X-Tile).
     * @param fromBoardPosition The board position of the starting tile.
     * @param toBoardPosition The board position of the arriving tile.
     */
    public BeforeMoveState(TileState fromTileState, TileState toTileState, String fromBoardPosition,
            String toBoardPosition) {
        this.fromTileState = fromTileState;
        this.toTileState = toTileState;
        this.fromBoardPosition = fromBoardPosition;
        this.toBoardPosition = toBoardPosition;
        this.isKill = false;
    }

    /**
     * Creates a memento of the state of the board to save a kill move.
     * @param fromTileState The state of the tile before it was killed.
     * @param fromBoardPosition The position of the killed tile.
     */
    public BeforeMoveState(TileState fromTileState, String fromBoardPosition) {
        this.fromTileState = fromTileState;
        this.fromBoardPosition = fromBoardPosition;
        this.isKill = true;
    }
    
    public TileState getFromTileState() {
        return this.fromTileState;
    }

    public void setFromTileState(TileState fromTileState) {
        this.fromTileState = fromTileState;
    }

    public TileState getToTileState() {
        return this.toTileState;
    }

    public void setToTileState(TileState toTileState) {
        this.toTileState = toTileState;
    }

    public String getFromBoardPosition() {
        return this.fromBoardPosition;
    }

    public void setFromBoardPosition(String fromBoardPosition) {
        this.fromBoardPosition = fromBoardPosition;
    }

    public String getToBoardPosition() {
        return this.toBoardPosition;
    }

    public void setToBoardPosition(String toBoardPosition) {
        this.toBoardPosition = toBoardPosition;
    }

    public boolean isKill() {
        return this.isKill;
    }

    public void setIsKill(boolean isKill) {
        this.isKill = isKill;
    }
}