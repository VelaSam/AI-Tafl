package modele.ai;

import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.Position;
import modele.plan_de_jeu.Tile;
import modele.plan_de_jeu.TileState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphaBeta {
    int numExploredNodes;
    private TileState playerColor;

    public static final int depth = 2;

    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seulement si plusieurs coups
    // ont le même score.
    public Map<Tile, ArrayList<Position>> getNextMoveAB(Board board) {


        playerColor = board.getMaxPlayer();
        numExploredNodes = 0;

        Map<Tile, ArrayList<Position>> result = new HashMap<>();
        double resultValue = Double.NEGATIVE_INFINITY;

        //get possible moves
        Map<Tile, List<Position>> possibleMoves = board.getPossibleMoves();

        for (Map.Entry<Tile, List<Position>> entry : possibleMoves.entrySet()) {
            Tile coloredPiece = entry.getKey();
            List<Position> possibleMovesFromColoredPiece = entry.getValue();

            ArrayList<Position> positionsToSend = new ArrayList<>();

            for (Position position : possibleMovesFromColoredPiece) {
                Board boardWithNextMove = board.checkMove(coloredPiece, position, playerColor);
                double value = minValueAlphaBeta(boardWithNextMove, boardWithNextMove.getMinPlayer(), resultValue, Double.POSITIVE_INFINITY, depth);
                if (value >= resultValue) {
                    if (value > resultValue) {
                      // result.clear();
                        positionsToSend.clear();
                    }
                    positionsToSend.add(position); // ???????????
                    System.out.println(positionsToSend);
                    resultValue = value;
                }
            }
            if (!positionsToSend.isEmpty()) {
                result.put(coloredPiece, positionsToSend);
            }
        }
        return result;
    }

    public double maxValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta, int depth) {
        numExploredNodes++;
        int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur);

        if(depth == 0){
            return positionActuelleEstFinale;
        }

        // Game is not done
        double value = Double.NEGATIVE_INFINITY;
        Map<Tile, List<Position>> possibleMoves = positionActuelle.getPossibleMoves();

        for (Map.Entry<Tile, List<Position>> entry : possibleMoves.entrySet()) {
            List<Position> positions = entry.getValue();

            for (Position position : positions) {
                Board boardWithNextMove = positionActuelle.checkMove(entry.getKey(), position, playerColor);
                double score = minValueAlphaBeta(boardWithNextMove, boardWithNextMove.getMinPlayer(), Math.max(alpha, value), beta, depth - 1);
                value = Math.max(value, score);
                alpha = Math.max(alpha, value);
                if (alpha >= beta)
                    return value;
            }
        }

        return value;
    }

    public double minValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta, int depth) {
        numExploredNodes++;
        int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur);

        if(depth == 0){
            return positionActuelleEstFinale;
        }

        double value = Double.POSITIVE_INFINITY;
        Map<Tile, List<Position>> possibleMoves = positionActuelle.getPossibleMoves();

        for (Map.Entry<Tile, List<Position>> entry : possibleMoves.entrySet()) {
            List<Position> positions = entry.getValue();

            for (Position position : positions) {
                Board boardWithNextMove = positionActuelle.checkMove(entry.getKey(), position, playerColor);
                double score = maxValueAlphaBeta(boardWithNextMove, boardWithNextMove.getMinPlayer(),
                        Math.min(alpha, value), beta, depth - 1);

                value = Math.min(value, score);
                beta = Math.min(beta, value);
                if (beta <= alpha)
                    return value;
            }
        }
        return value;
    }

}
