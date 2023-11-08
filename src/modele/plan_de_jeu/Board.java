package modele.plan_de_jeu;

import modele.helpers.Helpers;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    public final int WIDTH = 13;
    private Tile[][] tiles;

    private TileState maxPlayer;
    private TileState minPlayer;

    public static int iterations = 0;

    public Board(String[] boardValues, TileState couleur) {
        initializeNewBoard(boardValues);
        this.maxPlayer = couleur;
        this.minPlayer = this.maxPlayer == TileState.NOIR ? TileState.ROUGE : TileState.NOIR;
    }

    public Board(Tile[][] tiles, TileState playerColor){
        this.tiles = new Tile[WIDTH][WIDTH];
        for (int j = WIDTH-1; j >= 0; j--) {
            for(int i = 0 ; i < WIDTH; i++){
                tiles[i][j] = new Tile(new Position(i, j), tiles[i][j].getState());
            }
        }
        this.maxPlayer = playerColor;
        this.minPlayer = this.maxPlayer == TileState.NOIR ? TileState.ROUGE : TileState.NOIR;
    }
    public TileState getMaxPlayer() {
        return maxPlayer;
    }
    public TileState getMinPlayer() {
        return minPlayer;
    }

    public Board checkMove(Position move, TileState color) {
        Board clonedBoard = this.clone(this.tiles, color);

        System.out.println(clonedBoard);

        clonedBoard.tiles[move.getX()][move.getY()].setState(color == TileState.ROUGE ? TileState.NOIR : TileState.ROUGE);
        // clonedBoard.nextMarkToPlay = nextMarkToPlay == Mark.X ? Mark.O : Mark.X; huh
        return clonedBoard;
    }

    public Board clone(Tile[][] tiles, TileState playerColor){
        Board clonedBoard = new Board(tiles, playerColor);
        return clonedBoard;
    }

    public void initializeNewBoard(String[] boardValues) {
        tiles = new Tile[WIDTH][WIDTH];
        int z = 0;
        for (int j = WIDTH-1; j >= 0; j--) {
            for(int i = 0 ; i < WIDTH; i++){
                tiles[i][j] = new Tile(new Position(i, j), Integer.parseInt(boardValues[z]));
                z++;
            }
        }
    }

    public Map<Tile, List<Position>> getPossibleMoves(){

        // optimisation: a place de recreer la map a chaque fois, juste updater map qui existe deja
        // optimisation, envoyer la position a place de la tile
        Map<Tile, List<Position>> positions = new HashMap<>();

        List<Tile> maxPlayerPieces;

        if(this.maxPlayer == TileState.NOIR){
            maxPlayerPieces = Arrays.stream(tiles)
                    .flatMap(Arrays::stream)
                    .filter(tile -> tile.getState() == TileState.NOIR || tile.getState() == TileState.ROI_NOIR)
                    .collect(Collectors.toList());
        } else {
            maxPlayerPieces = Arrays.stream(tiles)
                    .flatMap(Arrays::stream)
                    .filter(tile -> tile.getState() == TileState.ROUGE)
                    .collect(Collectors.toList());
        }

        skimThroughBoard(positions, maxPlayerPieces);

        return positions;
    }


    //Va recevoir un move tel que "A5 - B5@" et va ensuite modifier le board en consequence
    public void playMoveOnBoard(String move) {

        iterations++;
        String beginningSpace = move.split("-")[0].trim();
        String destinationSpace = move.split("-")[1].trim();

        Tile beginningTile = tiles
                [Helpers.getNumberValue(String.valueOf(beginningSpace.charAt(0)))]
                [Integer.parseInt(beginningSpace.substring(1))-1];

        Tile destinationTile = tiles
                [Helpers.getNumberValue(String.valueOf(destinationSpace.charAt(0)))]
                [Integer.parseInt(destinationSpace.substring(1))-1];

        if (beginningTile.getX() != destinationTile.getX() && beginningTile.getY() != destinationTile.getY()) {
            throw new IllegalArgumentException("The piece at the tile " +
                    Helpers.getLetterValue(beginningTile.getX()) + (beginningTile.getY() + 1) +
                    " may not go to " + Helpers.getLetterValue(destinationTile.getX()) + (destinationTile.getY() + 1));
        }


        TileState moverState = beginningTile.getState();

        if (beginningTile.getState() == TileState.EMPTY) {
            throw new IllegalArgumentException("Beginning tile: tiles[" + beginningTile.getX() +
                    "][" + beginningTile.getY() + "] " + Helpers.getLetterValue(beginningTile.getX())+
                    (beginningTile.getY()+1) + " is empty. Cannot make a move.");
        }

        if (destinationTile.getState() != TileState.EMPTY) {
            throw new IllegalArgumentException("Cannot move on a tile: tiles[" + destinationTile.getX() +
                    "][" + destinationTile.getY() + "] " + Helpers.getLetterValue(destinationTile.getX())+
                    (destinationTile.getY()+1) + " already containing a piece");
        }

        if(destinationTile.isMarkedX() && beginningTile.getState() != TileState.ROI_NOIR){
            throw new IllegalArgumentException("A non king piece cannot move on a X tile: tiles[" +
                    beginningTile.getX() + "][" + beginningTile.getY() + "]  ");
        }

        beginningTile.setState(TileState.EMPTY);
        destinationTile.setState(moverState);
        checkForPieceKill(destinationTile);

        // removeable
        System.out.println(this);
    }


    public String toString(){
        StringBuilder boardStr = new StringBuilder("");
        if(iterations == 0){
            boardStr.append("Game start \n");
        }else{
            boardStr.append("Move number: ").append(iterations).append(" \n");
        }
        for(int i = WIDTH-1; i >= 0; i--){
            for(int j = 0; j < WIDTH; j++){
                boardStr.append(tiles[j][i].singleCharacterStateString()).append(" ");
            }
        boardStr.append("\n");
        }
        return boardStr.toString();
    }

//    To parallelize the code, you can use Java's ExecutorService and the submit method
//    to create and run tasks concurrently. You can divide the work into separate tasks for
//    each Tile in the filteredTiles list. Here's a parallelized version of your code:
    private void skimThroughBoard(Map<Tile, List<Position>> positions, List<Tile> filteredTiles) {
        for(Tile tile: filteredTiles){

            List<Position> availablePositions = new ArrayList<>();

            //aller vers la droite
            for(int i = tile.getX() + 1; i < WIDTH && !isBlockedTile(tiles[i][tile.getY()]); i++){
                availablePositions.add(new Position(i, tile.getY()));
            }
            //aller vers la gauche
            for(int i = tile.getX() - 1; i >= 0 && !isBlockedTile(tiles[i][tile.getY()]); i--){
                availablePositions.add(new Position(i, tile.getY()));
            }
            //aller vers le haut
            for(int j = tile.getY() + 1; j < WIDTH && !isBlockedTile(tiles[tile.getX()][j]); j++){
                availablePositions.add(new Position(tile.getX(), j));
            }
            // aller vers le bas
            for(int j = tile.getY() - 1; j >= 0 && !isBlockedTile(tiles[tile.getX()][j]); j--){
                availablePositions.add(new Position(tile.getX(), j));
            }
            positions.put(tile, availablePositions);
        }
    }
    private boolean isBlockedTile(Tile tile){
        //if the tile has a piece on it OR has an X marked on it
        return tile.getState() != TileState.EMPTY || tile.isMarkedX();
    }

    private void checkForPieceKill(Tile movedPiece){
        //will kill a piece if the enemy piece is between two pieces

        Tile nextUp = null, nextDown = null, nextRight = null, nextLeft = null;

        nextUp = movedPiece.getNextUp(tiles);
        nextDown = movedPiece.getNextDown(tiles);
        nextRight = movedPiece.getNextRight(tiles);
        nextLeft = movedPiece.getNextLeft(tiles);

        //Cette section check si la case a coter est de couleur opposee ET que la case APRES celle la est de la meme couleur que movedPiece
        //si oui, KILL LA PIECE AONDOAWIDAOWIDN
        if(nextUp != null && movedPiece.isOppositeColorOf(nextUp)){
            Tile nextNextUp = nextUp.getNextUp(tiles);
            if(nextNextUp != null && (nextUp.isOppositeColorOf(nextNextUp) || nextNextUp.isMarkedX())){
                //kill
                nextUp.setState(TileState.EMPTY);
            }
        }
        if(nextDown != null && movedPiece.isOppositeColorOf(nextDown)){
            Tile nextNextDown = nextDown.getNextDown(tiles);
            if(nextNextDown != null && (nextDown.isOppositeColorOf(nextNextDown) || nextNextDown.isMarkedX())){
                nextDown.setState(TileState.EMPTY);
            }
        }
        if(nextRight != null && movedPiece.isOppositeColorOf(nextRight)){
            Tile nextNextRight = nextRight.getNextRight(tiles);
            if(nextNextRight != null && (nextRight.isOppositeColorOf(nextNextRight) || nextNextRight.isMarkedX())){
                nextRight.setState(TileState.EMPTY);
            }

        }
        if(nextLeft != null && movedPiece.isOppositeColorOf(nextLeft)){
            Tile nextNextLeft = nextLeft.getNextLeft(tiles);
            if(nextNextLeft != null && (nextLeft.isOppositeColorOf(nextNextLeft) || nextNextLeft.isMarkedX())){
                nextLeft.setState(TileState.EMPTY);
            }
        }


    }
}
