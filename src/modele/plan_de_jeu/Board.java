package modele.plan_de_jeu;

import java.util.*;

public class Board {

    public final int WIDTH = 13;
    private Tile[] tiles;

    private TileState maxPlayer;
    private TileState minPlayer;


    public Board(String[] boardValues, TileState couleur) {
        initializeNewBoard(boardValues);
        this.maxPlayer = couleur;
        this.minPlayer = this.maxPlayer == TileState.NOIR ? TileState.ROUGE : TileState.NOIR;

    }

    public void initializeNewBoard(String[] boardValues) {
        tiles = new Tile[WIDTH*WIDTH];
        int x = 0, y = 0;
        for (int i = 0; i < boardValues.length; i++) {
            tiles[i] = new Tile(new Position(x, y), Integer.parseInt(boardValues[i]));
            x++;
            if (x == WIDTH) {
                x = 0;
                y++;
            }
        }
    }

    public Map<Tile, ArrayList<Position>> getPossibleMoves(){

        // optimisation: a place de recreer la map a chaque fois, juste updater map qui existe deja
        // optimisation, envoyer la position a place de la tile
        Map<Tile, ArrayList<Position>> positions = new HashMap<>();

        ArrayList<Tile> maxPlayerPieces;

        if(this.maxPlayer == TileState.NOIR){
            // optimisation: a place de recreer la map a chaque fois, juste updater map qui existe deja
            maxPlayerPieces = new ArrayList<>(Arrays.stream(tiles).filter(tile -> tile.getState() == TileState.NOIR ||
                    tile.getState() == TileState.ROI_NOIR).toList());

        } else {
            maxPlayerPieces = new ArrayList<>(Arrays.stream(tiles).filter(tile -> tile.getState() == TileState.ROUGE).toList());
        }

        skimThroughBoard(positions, maxPlayerPieces);

        return positions;
    }

    private void skimThroughBoard(Map<Tile, ArrayList<Position>> positions, ArrayList<Tile> filteredTiles) {
        for(Tile tile: filteredTiles){

            ArrayList<Position> availablePositions = new ArrayList<>();

            //monter vers le haut
            for(int i = tile.getPosition().getI() + 1; tile.getState() != TileState.EMPTY && i < this.WIDTH; i++){
                availablePositions.add(new Position(i, tile.getPosition().getJ()));
            }

            // descendre vers le bas
            for(int i = tile.getPosition().getI() - 1; tile.getState() != TileState.EMPTY && i >= 0; i--){
                availablePositions.add(new Position(i, tile.getPosition().getJ()));
            }

            // vers la droite
            for(int j = tile.getPosition().getJ() + 1; tile.getState() != TileState.EMPTY && j < this.WIDTH; j++){
                availablePositions.add(new Position(tile.getPosition().getI(), j));

            }

            // vers la gauche
            for(int j = tile.getPosition().getJ() - 1; tile.getState() != TileState.EMPTY && j >= 0; j--){
                availablePositions.add(new Position(tile.getPosition().getI(), j));
            }

            positions.put(tile, availablePositions);
        }
    }
}
