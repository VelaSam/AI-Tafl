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
            //time to change this shti wtf
            tiles[i] = new Tile(new Position(x, y), Integer.parseInt(boardValues[i]));
            x++;
            if (x == WIDTH) {
                x = 0;
                y++;
            }
        }
    }

    public Map<Tile, List<Position>> getPossibleMoves(){

        // optimisation: a place de recreer la map a chaque fois, juste updater map qui existe deja
        // optimisation, envoyer la position a place de la tile
        Map<Tile, List<Position>> positions = new HashMap<>();

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

    private void skimThroughBoard(Map<Tile, List<Position>> positions, List<Tile> filteredTiles) {
        for(Tile tile: filteredTiles){

            List<Position> availablePositions = new ArrayList<>();

            //aller vers la droite
            for(int i = tile.getPosition().getX() + 1; i < this.WIDTH && tiles[i + tile.getPosition().getY()].getState() == TileState.EMPTY; i++){
                availablePositions.add(new Position(i, tile.getPosition().getY()));
            }

            // aller vers la gauche
            for(int i = tile.getPosition().getX() - 1; i >= 0 && tiles[i + tile.getPosition().getY()].getState() == TileState.EMPTY; i--){
                availablePositions.add(new Position(i, tile.getPosition().getY()));
            }

            // vers le haut
            for(int j = tile.getPosition().getY() + 1; j < this.WIDTH && tiles[j + tile.getPosition().getX()].getState() == TileState.EMPTY; j++){
                System.out.println(tiles[j + tile.getPosition().getX()].getState() + " " + tiles[j + tile.getPosition().getX()].getPosition());

                availablePositions.add(new Position(tile.getPosition().getX(), j));

            }

            // vers le bas
            for(int j = tile.getPosition().getY() - 1; j >= 0 && tiles[j + tile.getPosition().getX()].getState() == TileState.EMPTY; j--){
                availablePositions.add(new Position(tile.getPosition().getX(), j));
            }

            positions.put(tile, availablePositions);
        }
    }
}
