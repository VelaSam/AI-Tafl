package modele.plan_de_jeu;

import modele.helpers.Helpers;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private static final int RED_PLAYER_PIECES = 24;

    private static final int BLACK_PLAYER_PIECES = 12;

    public final int WIDTH = 13;
    private Tile[][] tiles;

    private TileState maxPlayer;
    private TileState minPlayer;

    private Stack<BeforeMoveState> movesStack = new Stack<>();

    private int blackPieces;
    private int redPieces;

    public static int iterations = 0;

    /**
     * Constructor used to initialize the board from the string given by the server
     * 
     * @param boardValues The values of the board, given by row
     * @param playerColor The color that our AI plays
     */
    public Board(String[] boardValues, TileState playerColor) {
        initializeNewBoard(boardValues);
        this.maxPlayer = playerColor;
        this.minPlayer = Helpers.getOppositeTileState(playerColor);
        this.countCurrentPiecesOnBoard();
    }

    public TileState getMaxPlayer() {
        return maxPlayer;
    }

    public TileState getMinPlayer() {
        return minPlayer;
    }

    public int getPlayerPiecesCounter(TileState tileState) {
        if (tileState == TileState.ROUGE) {
            return redPieces;
        } else if (tileState == TileState.ROI_NOIR || tileState == TileState.NOIR) {
            return blackPieces;
        }
        return -1;
    }

    /**
     * The logic to create the tiles and populate the 2D tile array
     * 
     * @param boardValues An array of the starting tile states : empty, red, black
     *                    or king
     */
    public void initializeNewBoard(String[] boardValues) {
        this.tiles = new Tile[WIDTH][WIDTH];
        int z = 0;
        for (int j = WIDTH - 1; j >= 0; j--) {
            for (int i = 0; i < WIDTH; i++) {
                tiles[i][j] = new Tile(new Position(i, j), Integer.parseInt(boardValues[z]));
                z++;
            }
        }
    }

    /**
     * Sert à trouver les positions possibles pour chaque pièce du joueur qui a le
     * tour.
     * 
     * @return Une map qui contient les positions possibles pour chaque pièce du
     *         joueur.
     */
    public Map<String, List<String>> getPossibleMoves(TileState color) {

        // optimisation: a place de recreer la map a chaque fois, juste updater map qui
        // existe deja
        Map<String, List<String>> positions = new HashMap<>();

        List<Tile> playerPieces;

        if (color == TileState.NOIR) {
            playerPieces = Arrays.stream(tiles).flatMap(Arrays::stream)
                    .filter(tile -> tile.getState() == TileState.NOIR
                            || tile.getState() == TileState.ROI_NOIR)
                    .collect(Collectors.toList());
        } else {
            playerPieces = Arrays.stream(tiles).flatMap(Arrays::stream)
                    .filter(tile -> tile.getState() == TileState.ROUGE)
                    .collect(Collectors.toList());
        }

        skimThroughBoard(positions, playerPieces);

        return positions;
    }

    /**
     * Pour chaque pièce fournie en paramètre, explorer le plateau pour trouver les
     * emplacements vides et les ajoutés à la map de positions.
     * 
     * @param positions     La map qui contiendra les positions possibles pour les
     *                      pièces fournies.
     * @param filteredTiles Les pièces pour lesquelles on veut trouver les positions
     *                      possibles.
     */
    private void skimThroughBoard(Map<String, List<String>> positions, List<Tile> filteredTiles) {
        // To parallelize the code, you can use Java's ExecutorService and the submit
        // method
        // to create and run tasks concurrently. You can divide the work into separate
        // tasks for
        // each Tile in the filteredTiles list. Here's a parallelized version of your
        // code:
        for (Tile tile : filteredTiles) {
            String currentTileBoardPosition = tile.getPosition().getBoardPosition();

            boolean currentTileIsKing = tile.getState() == TileState.ROI_NOIR;

            List<String> availablePositions = new ArrayList<>();

            // aller vers la droite
            for (int i = tile.getX() + 1; i < WIDTH
                    && !isBlockedTile(tiles[i][tile.getY()], currentTileIsKing); i++) {
                availablePositions.add(Helpers.getBoardPositionFromCoordinates(i, tile.getY()));
            }
            // aller vers la gauche
            for (int i = tile.getX() - 1; i >= 0
                    && !isBlockedTile(tiles[i][tile.getY()], currentTileIsKing); i--) {
                availablePositions.add(Helpers.getBoardPositionFromCoordinates(i, tile.getY()));
            }
            // aller vers le haut
            for (int j = tile.getY() + 1; j < WIDTH
                    && !isBlockedTile(tiles[tile.getX()][j], currentTileIsKing); j++) {
                availablePositions.add(Helpers.getBoardPositionFromCoordinates(tile.getX(), j));
            }
            // aller vers le bas
            for (int j = tile.getY() - 1; j >= 0
                    && !isBlockedTile(tiles[tile.getX()][j], currentTileIsKing); j--) {
                availablePositions.add(Helpers.getBoardPositionFromCoordinates(tile.getX(), j));
            }

            positions.put(currentTileBoardPosition, availablePositions);
        }
    }

    private boolean isBlockedTile(Tile tile, boolean kingIsMoving) {
        // if the tile has a piece on it OR has an X marked on it
        return tile.getState() != TileState.EMPTY || tile.isMarkedX() && !kingIsMoving;
    }

    /**
     * Sert à jouer un coup sur le plateau.
     * 
     * @param move Le coup à jouer.
     */
    public void playMoveOnBoard(String move) {

        iterations++;

        // System.out.println(move);
        String[] splitMove = move.split("-");

        String beginningSpace = splitMove[0].trim();
        String destinationSpace = splitMove[1].trim();

        Tile beginningTile = tiles[Helpers.getNumberValue(String.valueOf(beginningSpace.charAt(0)))][Integer
                .parseInt(beginningSpace.substring(1)) - 1];

        Tile destinationTile = tiles[Helpers.getNumberValue(String.valueOf(destinationSpace.charAt(0)))][Integer
                .parseInt(destinationSpace.substring(1)) - 1];

        // If both have different X and Y, the move is diagonal, which is illegal.
        if (beginningTile.getX() != destinationTile.getX()
                && beginningTile.getY() != destinationTile.getY()) {
            throw new IllegalArgumentException("The piece at the tile "
                    + Helpers.getLetterValue(beginningTile.getX()) + (beginningTile.getY() + 1)
                    + " may not go to " + Helpers.getLetterValue(destinationTile.getX())
                    + (destinationTile.getY() + 1));
        }

        TileState moverState = beginningTile.getState();

        if (moverState == TileState.EMPTY) {
            throw new IllegalArgumentException(
                    "Beginning tile: tiles[" + beginningTile.getX() + "][" + beginningTile.getY()
                            + "] " + Helpers.getLetterValue(beginningTile.getX())
                            + (beginningTile.getY() + 1) + " is empty. Cannot make a move.");
        }

        if (destinationTile.getState() != TileState.EMPTY) {
            throw new IllegalArgumentException("Cannot move on a tile: tiles["
                    + destinationTile.getX() + "][" + destinationTile.getY() + "] "
                    + Helpers.getLetterValue(destinationTile.getX()) + (destinationTile.getY() + 1)
                    + " already containing a piece");
        }

        if (destinationTile.isMarkedX() && beginningTile.getState() != TileState.ROI_NOIR) {
            throw new IllegalArgumentException("A non king piece cannot move on a X tile: tiles["
                    + beginningTile.getX() + "][" + beginningTile.getY() + "]  ");
        }

        movesStack.add(new BeforeMoveState(beginningTile.getState(), destinationTile.getState(),
                beginningTile.getPosition().getBoardPosition(), destinationTile.getPosition().getBoardPosition()));

        beginningTile.setState(TileState.EMPTY);
        destinationTile.setState(moverState);
        checkForPieceKill(destinationTile);
    }

    /**
     * Permet de revenir en arriere d'un coup, incluant les éléminations de pions
     * causées par le dernier coup.
     */
    public void undoLastMove() {
        if (movesStack.size() == 0) {
            throw new IllegalArgumentException("There are no moves to undo");
        }

        BeforeMoveState lastMovePlayed = movesStack.pop();
        while (lastMovePlayed.isKill()) {
            // reverse kill
            Tile killedTile = tiles[Helpers
                    .getXCoordinateFromBoardPosition(lastMovePlayed.getFromBoardPosition())][Helpers
                            .getYCoordinateFromBoardPosition(lastMovePlayed.getFromBoardPosition())];
            killedTile.setState(lastMovePlayed.getFromTileState());
            incrementColorPiece(lastMovePlayed.getFromTileState());

            lastMovePlayed = movesStack.pop();
        }

        Tile beginningTile = tiles[Helpers
                .getXCoordinateFromBoardPosition(lastMovePlayed.getFromBoardPosition())][Helpers
                        .getYCoordinateFromBoardPosition(lastMovePlayed.getFromBoardPosition())];

        Tile destinationTile = tiles[Helpers
                .getXCoordinateFromBoardPosition(lastMovePlayed.getToBoardPosition())][Helpers
                        .getYCoordinateFromBoardPosition(lastMovePlayed.getToBoardPosition())];

        beginningTile.setState(lastMovePlayed.getFromTileState());
        destinationTile.setState(lastMovePlayed.getToTileState());
    }

    /**
     * Regarde si la pièce fournie en paramètre peut tuer une ou des pièces
     * ennemies.
     * 
     * @param movedPiece La pièce qui vient de bouger.
     */
    private void checkForPieceKill(Tile movedPiece) {
        // will kill a piece if the enemy piece is between two pieces

        Tile nextUp = null, nextDown = null, nextRight = null, nextLeft = null;

        nextUp = movedPiece.getNextUp(tiles);
        nextDown = movedPiece.getNextDown(tiles);
        nextRight = movedPiece.getNextRight(tiles);
        nextLeft = movedPiece.getNextLeft(tiles);

        // Cette section check si la case a coter est de couleur opposee ET que la case
        // APRES celle
        // la est de la meme couleur que movedPiece
        // si oui, KILL LA PIECE AONDOAWIDAOWIDN
        if (nextUp != null && movedPiece.isOppositeColorOf(nextUp)) {
            Tile nextNextUp = nextUp.getNextUp(tiles);
            if (nextNextUp != null
                    && (nextUp.isOppositeColorOf(nextNextUp) || nextNextUp.isMarkedX())) {
                if (nextUp.getState() != TileState.ROI_NOIR) {
                    movesStack.add(new BeforeMoveState(nextUp.getState(), nextUp.getPosition().getBoardPosition()));
                    this.decrementColorPiece(nextUp.getState());
                    nextUp.setState(TileState.EMPTY);
                }
            }
        }
        if (nextDown != null && movedPiece.isOppositeColorOf(nextDown)) {
            Tile nextNextDown = nextDown.getNextDown(tiles);
            if (nextNextDown != null
                    && (nextDown.isOppositeColorOf(nextNextDown) || nextNextDown.isMarkedX())) {
                if (nextDown.getState() != TileState.ROI_NOIR) {
                    movesStack.add(new BeforeMoveState(nextDown.getState(), nextDown.getPosition().getBoardPosition()));
                    this.decrementColorPiece(nextDown.getState());
                    nextDown.setState(TileState.EMPTY);
                }
            }
        }
        if (nextRight != null && movedPiece.isOppositeColorOf(nextRight)) {
            Tile nextNextRight = nextRight.getNextRight(tiles);
            if (nextNextRight != null
                    && (nextRight.isOppositeColorOf(nextNextRight) || nextNextRight.isMarkedX())) {
                if (nextRight.getState() != TileState.ROI_NOIR) {
                    movesStack
                            .add(new BeforeMoveState(nextRight.getState(), nextRight.getPosition().getBoardPosition()));
                    this.decrementColorPiece(nextRight.getState());
                    nextRight.setState(TileState.EMPTY);
                }
            }

        }
        if (nextLeft != null && movedPiece.isOppositeColorOf(nextLeft)) {
            Tile nextNextLeft = nextLeft.getNextLeft(tiles);
            if (nextNextLeft != null
                    && (nextLeft.isOppositeColorOf(nextNextLeft) || nextNextLeft.isMarkedX())) {
                if (nextLeft.getState() != TileState.ROI_NOIR) {
                    movesStack.add(new BeforeMoveState(nextLeft.getState(), nextLeft.getPosition().getBoardPosition()));
                    this.decrementColorPiece(nextLeft.getState());
                    nextLeft.setState(TileState.EMPTY);
                }
            }
        }
    }

    /**
     * Sert à décrementer le nombre de pièces restantes sur le plateau de la couleur
     * fournie en paramètre.
     * 
     * @param tileState La couleur de la pièce à décrementer.
     */
    private void decrementColorPiece(TileState tileState) {
        if (tileState == TileState.ROUGE) {
            redPieces--;
        } else if (tileState == TileState.ROI_NOIR || tileState == TileState.NOIR) {
            blackPieces--;
        }
    }

    /**
     * Sert à incrémenter le nombre de pièces restantes sur le plateau de la couleur
     * fournie en paramètre.
     * 
     * @param tileState La couleur de la pièce à incrémenter.
     */
    private void incrementColorPiece(TileState tileState) {
        if (tileState == TileState.ROUGE) {
            redPieces++;
        } else if (tileState == TileState.ROI_NOIR || tileState == TileState.NOIR) {
            blackPieces++;
        }
    }

    /**
     * Sert à compter le nombre de pièces restantes de chaque couleur sur le
     * plateau.
     */
    private void countCurrentPiecesOnBoard() {
        this.redPieces = 0;
        this.blackPieces = 0;

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (tiles[i][j].getState() == TileState.NOIR
                        || tiles[i][j].getState() == TileState.ROI_NOIR) {
                    blackPieces++;
                } else if (tiles[i][j].getState() == TileState.ROUGE) {
                    redPieces++;
                }
            }
        }
    }

    /**
     * Sert à trouver la tuile qui a la position fournie en paramètre.
     * 
     * @param boardPosition La position de la tuile à trouver.
     * @return La tuile qui a la position fournie en paramètre.
     */
    public Tile findTileWithBoardPosition(String boardPosition) {
        return this.tiles[Helpers.getXCoordinateFromBoardPosition(boardPosition)][Helpers
                .getYCoordinateFromBoardPosition(boardPosition)];
    }

    public int getBlackPieces() {
        return blackPieces;
    }

    public int getRedPieces() {
        return redPieces;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Sert à trouver la tuile qui contient le roi.
     * 
     * @return La tuile qui contient le roi noir.
     */
    public Tile getKingTile() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (tiles[i][j].getState() == TileState.ROI_NOIR) {
                    return tiles[i][j];
                }
            }
        }
        return null;
    }

    public BeforeMoveState getSecondLastMove() {
        for (int i = movesStack.size() - 1; i >= 0; i--) {
            BeforeMoveState currentMove = movesStack.get(i);
            if (currentMove.isKill()) {
                continue;
            }
            for (int j = i - 1; j >= 0; j--) {
                currentMove = movesStack.get(j);
                if (currentMove.isKill()) {
                    continue;
                }
                return currentMove;
            }
        }
        return null;
    }

    /**
     * Sert à trouver le nombre de pièces tuées lors du dernier coup.
     * 
     * @return Le nombre de pièces tuées lors du dernier coup.
     */
    public int getLastMoveKillCount() {
        BeforeMoveState secondLastMove = getSecondLastMove();
        int killCount = 0;
        for (int i = movesStack.indexOf(secondLastMove); i < movesStack.size(); i++) {
            if (i == -1) {
                continue;
            }

            if (movesStack.get(i).isKill()) {
                killCount++;
            } else {
                break;
            }
        }
        return killCount;
    }

    /**
     * Sert à trouver la tuile où s'est déplacée la pièce lors du dernier coup.
     * 
     * @return La tuile où s'est déplacée la pièce lors du dernier coup.
     */
    public Tile getLastMoveUpdatedTile() {
        if (getSecondLastMove() == null) {
            return null;
        }

        Tile lastTile = findTileWithBoardPosition(getSecondLastMove().getToBoardPosition());
        // for (int i = movesStack.size() - 1; i >= movesStack.size() - 4 && i >= 0;
        // i--) {
        // if (movesStack.get(i).isKill()) {
        // continue;
        // } else {
        // lastTile =
        // break;
        // }
        // }

        return lastTile;
    }

    public String toString() {
        StringBuilder boardStr = new StringBuilder("");
        if (iterations == 0) {
            boardStr.append("Game start \n");
        } else {
            boardStr.append("Move number: ").append(iterations).append(" \n");
        }
        for (int i = WIDTH - 1; i >= 0; i--) {
            for (int j = 0; j < WIDTH; j++) {
                boardStr.append(tiles[j][i].singleCharacterStateString()).append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }
}
