package modele.ai;

import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.TileState;

import java.util.*;

public class AlphaBeta {
    int numExploredNodes;
    public static final int depth = 10;
    private Timer timer;
    private boolean timeIsUp;


    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seulement si plusieurs coups
    // ont le même score.
    public Map<String, ArrayList<String>> getNextMoveAB(Board board) {
        timer = new Timer();
        timeIsUp = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeIsUp = true;
            }
        }, 4999);

        numExploredNodes = 0;

        Map<String, ArrayList<String>> result = new HashMap<>();
        double resultValue = Double.NEGATIVE_INFINITY;

        // get possible moves
        Map<String, List<String>> possibleMoves = board.getPossibleMoves(board.getMaxPlayer());

        // Pour chaque piece de la couleur concernée
        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
            String coloredPieceBoardPosition = entry.getKey();
            List<String> possibleMovesFromColoredPiece = entry.getValue();
            ArrayList<String> bestMovesForCurrentPiece = new ArrayList<>();

            double beforeValue = resultValue;

            // Pour chaque coup possible de la piece courante
            for (String position : possibleMovesFromColoredPiece) {
                board.playMoveOnBoard(coloredPieceBoardPosition + " - " + position);
                double value = minValueAlphaBeta(board, board.getMinPlayer(), resultValue,
                        Double.POSITIVE_INFINITY, depth);
                board.undoLastMove();
                if (value >= resultValue) {
                    if (value > resultValue) {
                        bestMovesForCurrentPiece.clear();
                    }
                    bestMovesForCurrentPiece.add(position);
                    resultValue = value;
                }
            }

            if (resultValue > beforeValue) {
                result.clear();
            }

            if(!bestMovesForCurrentPiece.isEmpty()){
                result.put(coloredPieceBoardPosition, bestMovesForCurrentPiece);
            }

        }
        timer.cancel();
        return result;

    }

    public double maxValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta, int depth) {
        numExploredNodes++;

        if (depth == 0 || timeIsUp) {

            System.out.println("THE DEPTH IS: " + depth);
            int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur, depth);
            // System.out.println(positionActuelle);

            return positionActuelleEstFinale;
        }

        // Game is not done
        double value = Double.NEGATIVE_INFINITY;
        Map<String, List<String>> possibleMoves = positionActuelle.getPossibleMoves(positionActuelle.getMaxPlayer());

        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
            List<String> positions = entry.getValue();

            for (String position : positions) {
                positionActuelle.playMoveOnBoard(entry.getKey() + " - " + position);
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

        if (depth == 0) {
            int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur, depth);
            // System.out.println(positionActuelle);
            return positionActuelleEstFinale;
        }

        double value = Double.POSITIVE_INFINITY;
        Map<String, List<String>> possibleMoves = positionActuelle.getPossibleMoves(joueur);

        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
            List<String> positions = entry.getValue();

            for (String position : positions) {
                positionActuelle.playMoveOnBoard(entry.getKey() + " - " + position);
                double score = maxValueAlphaBeta(positionActuelle, positionActuelle.getMaxPlayer(),
                        Math.min(alpha, value), beta, depth - 1);
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
