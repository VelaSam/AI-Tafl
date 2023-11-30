package modele.ai;

import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.Tile;
import modele.plan_de_jeu.TileState;

public class EvaluationFunctions {

    private static int GAME_OVER = 1000000;
    private static int GAME_OVER_DRAW = 0;
    private static int KILL = 5000;
    private static int POSSIBLE_KING_DEATH = 25000;
    private static int POSSIBLE_PIECE_DEATH = 10000;
    private static int NORMAL_MOVE = 100;
    private static int DEPTH_CORRECTION = 1000;

    /**
     * Évalue la situation du joueur passé en paramètre
     * 
     * @param board Le plateau de jeu
     * @param color La couleur du joueur
     * @return La valeur de l'évaluation
     */
    public static int evaluate(Board board, TileState color, int depth) {
        int correctionDepth = depth * DEPTH_CORRECTION;
        switch (color) {
            case ROUGE:
                return evaluateForRed(board) + correctionDepth;
            case NOIR:
                return evaluateForBlack(board) + correctionDepth;
            default:
                break;
        }

        return 0;

        // TileState opposite = Helpers.getOppositeTileState(color);

        // if (kingInCorner(board)) {
        // return color == TileState.ROUGE ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        // }

        // int colorNumberPieces = board.getPlayerPiecesCounter(color);
        // int oppositeNumberPieces = board.getPlayerPiecesCounter(opposite);

        // // System.out.println(colorNumberPieces + " AND " + oppositeNumberPieces);

        // if (color == TileState.NOIR) {
        // colorNumberPieces *= 24;
        // oppositeNumberPieces *= 13;

        // } else {
        // colorNumberPieces *= 13;
        // oppositeNumberPieces *= 24;
        // }

        // // System.out.println(colorNumberPieces + " AND " + oppositeNumberPieces);

        // return colorNumberPieces - oppositeNumberPieces;
    }

    /**
     * Évalue si le minPlayer est le joueur rouge
     * 
     * @param board Le plateau de jeu
     * @return La valeur de l'évaluation
     */
    private static int evaluateForRed(Board board) {
        /**
         * Ordre de l'évaluation pour savoir si la partie est finie:
         * 1. Si le roi est dans un coin, alors la partie est finie ---
         * 2. Si tous les pions rouges sont capturés, alors la partie est finie ---
         * 3. Si le roi est capturé, alors la partie est finie +++
         * 4. Si tous les pions noirs sont capturés, alors la partie est finie +++
         * 5. Si les les possiblesMoves sont vides, alors la partie est finie (nulle)
         */


        // Case 3
        int redPiecesAroundKingCount = redPiecesAroundKingCount(board);
        if (redPiecesAroundKingCount == 4) {
            return -GAME_OVER;
        }

        // Case 4
        if (board.getPlayerPiecesCounter(TileState.NOIR) == 0) {
            return -GAME_OVER;
        }

        // Case 1
        if (kingInCorner(board)) {
            return GAME_OVER;
        }

        // Case 2
        if (board.getPlayerPiecesCounter(TileState.ROUGE) == 0) {
            return GAME_OVER;
        }

        // Case 5
        if (board.getPossibleMoves(TileState.ROUGE).isEmpty()) {
            return GAME_OVER_DRAW;
        }

        /**
         * Ordre de l'évaluation pour savoir si le move est bon:
         * 1. Si le move mets le pion dans une situation de mort possible, alors le move
         * est mauvais --
         * 2. Si le move mets le roi dans une situation de mort possible, alors le move
         * est bon ++
         * 3. Si le move fait un ou plusieurs kills, alors le move est bon +
         */


        // Case 2
        if (redPiecesAroundKingCount > 0) {
            return (-POSSIBLE_KING_DEATH) * redPiecesAroundKingCount;
        }

        // Case 3
        int lastMoveKillCount = board.getLastMoveKillCount();
        if (lastMoveKillCount > 0) {
            return (-KILL) * board.getLastMoveKillCount();
        }

        // Case 1
        Tile lastMovedTile = board.getLastMoveUpdatedTile();
        if (lastMovedTile != null && isTileInDanger(lastMovedTile, board)) {
            return POSSIBLE_PIECE_DEATH;
        }

        // Comportement par défaut quand rien de spécial ne se passe
        return -NORMAL_MOVE;
    }

    /**
     * Évalue la situation du joueur noir
     * 
     * @param board Le plateau de jeu
     * @return La valeur de l'évaluation
     */
    private static int evaluateForBlack(Board board) {
        if (kingInCorner(board)) {
            return Integer.MAX_VALUE;
        }

        return Integer.MIN_VALUE;
    }

    /**
     * Regarde si le roi est dans un des coins du plateau
     * 
     * @param board Le plateau de jeu
     * @return true si le roi est dans un coin, false sinon
     */
    private static boolean kingInCorner(Board board) {
        if (board.getTiles()[0][0].getState() == TileState.ROI_NOIR
                || board.getTiles()[board.WIDTH - 1][board.WIDTH - 1].getState() == TileState.ROI_NOIR
                || board.getTiles()[board.WIDTH - 1][0].getState() == TileState.ROI_NOIR
                || board.getTiles()[0][board.WIDTH - 1].getState() == TileState.ROI_NOIR) {
            return true;
        }
        return false;
    }

    /**
     * Regarde combien de pions rouges sont autour du roi
     * 
     * @param board Le plateau de jeu
     * @return Le nombre de pions rouges autour du roi
     */
    private static int redPiecesAroundKingCount(Board board) {
        Tile[][] tiles = board.getTiles();
        Tile kingTile = board.getKingTile();

        Tile nextUp = null, nextDown = null, nextRight = null, nextLeft = null;
        nextUp = kingTile.getNextUp(tiles);
        nextDown = kingTile.getNextDown(tiles);
        nextRight = kingTile.getNextRight(tiles);
        nextLeft = kingTile.getNextLeft(tiles);

        int count = 0;
        if (nextUp != null && nextUp.getState() == TileState.ROUGE) {
            count++;
        }
        if (nextDown != null && nextDown.getState() == TileState.ROUGE) {
            count++;
        }
        if (nextRight != null && nextRight.getState() == TileState.ROUGE) {
            count++;
        }
        if (nextLeft != null && nextLeft.getState() == TileState.ROUGE) {
            count++;
        }

        return count;
    }

    /**
     * Regarde si la tuile passée en paramètre est en danger de se faire capturer
     * par l'adversaire.
     * (Ceci est une version de base qui ne prend pas en compte les déplacements des
     * pions enemis sur les rangées et colonnes next)
     * 
     * @param tile  La tuile à vérifier
     * @param board Le plateau de jeu avec toutes les tuiles
     * @return true si la tuile est en danger, false sinon
     */
    private static boolean isTileInDanger(Tile tile, Board board) {
        Tile[][] tiles = board.getTiles();

        Tile nextUp = null, nextDown = null, nextRight = null, nextLeft = null;
        nextUp = tile.getNextUp(tiles);
        nextDown = tile.getNextDown(tiles);
        nextRight = tile.getNextRight(tiles);
        nextLeft = tile.getNextLeft(tiles);

        if (nextUp != null && tile.isOppositeColorOf(nextUp)) {
            return true;
        }
        if (nextDown != null && tile.isOppositeColorOf(nextDown)) {
            return true;
        }
        if (nextRight != null && tile.isOppositeColorOf(nextRight)) {
            return true;
        }
        if (nextLeft != null && tile.isOppositeColorOf(nextLeft)) {
            return true;
        }

        return false;
    }
}
