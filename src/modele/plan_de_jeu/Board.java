package modele.plan_de_jeu;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    public final int WIDTH = 13;
    private Tile[][] tiles;

    private TileState maxPlayer;
    private TileState minPlayer;


    public Board(String[] boardValues, TileState couleur) {
        initializeNewBoard(boardValues);
        this.maxPlayer = couleur;
        this.minPlayer = this.maxPlayer == TileState.NOIR ? TileState.ROUGE : TileState.NOIR;

    }

    public void initializeNewBoard(String[] boardValues) {
        tiles = new Tile[WIDTH][WIDTH];
        int x = 0, y = 0, z = 0;
        for (int j = 0; j < WIDTH; j++) {
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
}
