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
    public static final int depth = 4;

    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seulement si plusieurs coups
    // ont le même score.
    public Map<String, ArrayList<String>> getNextMoveAB(Board board) {
        numExploredNodes = 0;

        Map<String, ArrayList<String>> result = new HashMap<>();
        double resultValue = Double.NEGATIVE_INFINITY;

        // get possible moves
        Map<String, List<String>> possibleMoves = board.getPossibleMoves();

        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
            String coloredPieceBoardPosition = entry.getKey();
            List<String> possibleMovesFromColoredPiece = entry.getValue();

            ArrayList<String> positionsToSend = new ArrayList<>();

            for (String position : possibleMovesFromColoredPiece) {
                board.playMoveOnBoard(coloredPieceBoardPosition + " - " + position);
                // Board boardWithNextMove = board.checkMove(coloredPieceBoardPosition,
                // position);
                // double value = minValueAlphaBeta(boardWithNextMove,
                // boardWithNextMove.getMinPlayer(), resultValue,
                // Double.POSITIVE_INFINITY, depth);
                double value = minValueAlphaBeta(board, board.getMinPlayer(), resultValue,
                        Double.POSITIVE_INFINITY, depth);
                board.undoLastMove();
                if (value >= resultValue) {
                    if (value > resultValue) {
                        // result.clear();
                        positionsToSend.clear();
                    }
                    positionsToSend.add(position); // ???????????
                    // System.out.println(positionsToSend);
                    resultValue = value;
                }
            }
            if (!positionsToSend.isEmpty()) {
                result.put(coloredPieceBoardPosition, positionsToSend);
            }
        }
        return result;
    }

    public double maxValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta, int depth) {
        numExploredNodes++;
        int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur);

        if (depth == 0) {
            return positionActuelleEstFinale;
        }

        // Game is not done
        double value = Double.NEGATIVE_INFINITY;
        Map<String, List<String>> possibleMoves = positionActuelle.getPossibleMoves();

        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
            List<String> positions = entry.getValue();

            for (String position : positions) {
                positionActuelle.playMoveOnBoard(entry.getKey() + " - " + position);
                // Board boardWithNextMove = positionActuelle.checkMove(entry.getKey(),
                // position);
                // double score = minValueAlphaBeta(boardWithNextMove,
                // boardWithNextMove.getMinPlayer(), Math.max(alpha, value), beta, depth - 1);
                double score = minValueAlphaBeta(positionActuelle, positionActuelle.getMinPlayer(),
                        Math.max(alpha, value), beta, depth - 1);

                positionActuelle.undoLastMove();
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

        if (depth == 0) {
            return positionActuelleEstFinale;
        }

        double value = Double.POSITIVE_INFINITY;
        Map<String, List<String>> possibleMoves = positionActuelle.getPossibleMoves();

        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
            List<String> positions = entry.getValue();

            for (String position : positions) {
                positionActuelle.playMoveOnBoard(entry.getKey() + " - " + position);
                double score = maxValueAlphaBeta(positionActuelle, positionActuelle.getMinPlayer(),
                        Math.min(alpha, value), beta, depth - 1);
                // Board boardWithNextMove = positionActuelle.checkMove(entry.getKey(),
                // position);
                // double score = maxValueAlphaBeta(boardWithNextMove,
                // boardWithNextMove.getMinPlayer(),
                // Math.min(alpha, value), beta, depth - 1);
                positionActuelle.undoLastMove();
                value = Math.min(value, score);
                beta = Math.min(beta, value);
                if (beta <= alpha)
                    return value;
            }
        }
        return value;
    }

}
