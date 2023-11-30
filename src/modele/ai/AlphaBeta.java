package modele.ai;

import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.TileState;

import java.util.*;

public class AlphaBeta {
    int numExploredNodes;
    public static final int depth = 3;

    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seulement si plusieurs coups
    // ont le même score.
//    public Map<String, ArrayList<String>> getNextMoveAB(Board board) {
//        numExploredNodes = 0;
//
//        Map<String, ArrayList<String>> result = new HashMap<>();
//        double resultValue = Double.NEGATIVE_INFINITY;
//
//        // get possible moves
//        Map<String, List<String>> possibleMoves = board.getPossibleMoves(board.getMaxPlayer());
//
//        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
//            String coloredPieceBoardPosition = entry.getKey();
//            List<String> possibleMovesFromColoredPiece = entry.getValue();
//
//            ArrayList<String> positionsToSend = new ArrayList<>();
//
//            for (String position : possibleMovesFromColoredPiece) {
//                board.playMoveOnBoard(coloredPieceBoardPosition + " - " + position);
//                // Board boardWithNextMove = board.checkMove(coloredPieceBoardPosition,
//                // position);
//                // double value = minValueAlphaBeta(boardWithNextMove,
//                // boardWithNextMove.getMinPlayer(), resultValue,
//                // Double.POSITIVE_INFINITY, depth);
//                double value = minValueAlphaBeta(board, board.getMinPlayer(), resultValue,
//                        Double.POSITIVE_INFINITY, depth);
//                board.undoLastMove();
//                if (value >= resultValue) {
//                    if (value > resultValue) {
//                        // result.clear();
//                        positionsToSend.clear();
//                    }
//                    positionsToSend.add(position); // ???????????
//                    // System.out.println(positionsToSend);
//                    resultValue = value;
//                    System.out.println("resultValue: " + resultValue);
//                }
//            }
//            if (!positionsToSend.isEmpty()) {
//                result.put(coloredPieceBoardPosition, positionsToSend);
//            }
//        }
//        return result;
//    }
//
//    public double maxValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta, int depth) {
//        numExploredNodes++;
//        int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur, depth);
//
//        if (depth == 0) {
//            return positionActuelleEstFinale;
//        }
//
//        // Game is not done
//        double value = Double.NEGATIVE_INFINITY;
//        Map<String, List<String>> possibleMoves = positionActuelle.getPossibleMoves(positionActuelle.getMaxPlayer());
//
//        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
//            List<String> positions = entry.getValue();
//
//            for (String position : positions) {
//                positionActuelle.playMoveOnBoard(entry.getKey() + " - " + position);
//                // Board boardWithNextMove = positionActuelle.checkMove(entry.getKey(),
//                // position);
//                // double score = minValueAlphaBeta(boardWithNextMove,
//                // boardWithNextMove.getMinPlayer(), Math.max(alpha, value), beta, depth - 1);
//                double score = minValueAlphaBeta(positionActuelle, positionActuelle.getMinPlayer(),
//                        Math.max(alpha, value), beta, depth - 1);
//
//                positionActuelle.undoLastMove();
//                value = Math.max(value, score);
//                alpha = Math.max(alpha, value);
//                if (alpha >= beta)
//                    return value;
//            }
//        }
//        // System.out.println("maxValueAlphaBeta for depth " + depth + ": " + value);
//        return value;
//    }
//
//    public double minValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta, int depth) {
//        numExploredNodes++;
//        int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur, depth);
//
//        if (depth == 0) {
//            return positionActuelleEstFinale;
//        }
//
//        double value = Double.POSITIVE_INFINITY;
//        Map<String, List<String>> possibleMoves = positionActuelle.getPossibleMoves(joueur);
//
//        for (Map.Entry<String, List<String>> entry : possibleMoves.entrySet()) {
//            List<String> positions = entry.getValue();
//
//            for (String position : positions) {
//                positionActuelle.playMoveOnBoard(entry.getKey() + " - " + position);
//                double score = maxValueAlphaBeta(positionActuelle, positionActuelle.getMaxPlayer(),
//                        Math.min(alpha, value), beta, depth - 1);
//                // Board boardWithNextMove = positionActuelle.checkMove(entry.getKey(),
//                // position);
//                // double score = maxValueAlphaBeta(boardWithNextMove,
//                // boardWithNextMove.getMinPlayer(),
//                // Math.min(alpha, value), beta, depth - 1);
//                positionActuelle.undoLastMove();
//                value = Math.min(value, score);
//                beta = Math.min(beta, value);
//                if (beta <= alpha)
//                    return value;
//            }
//        }
//        // System.out.println("minValueAlphaBeta for depth " + depth + ": " + value);
//        return value;
//    }

    public static Map.Entry<String, String> getBestMove(Board board) {
        String from = null;
        String to = null;
        int bestValue = Integer.MIN_VALUE;
        var maxPlayer = board.getMaxPlayer();

        Map<String, List<String>> possibleMoves = board.getPossibleMoves(maxPlayer);

        for (var move : possibleMoves.entrySet()) {
            String coloredPieceBoardPosition = move.getKey();

            for (var pos : move.getValue()) {

                board.playMoveOnBoard(coloredPieceBoardPosition + " - " + pos);

                int moveValue = Minimax(board, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, false);

                if (moveValue > bestValue) {
                    from = move.getKey();
                    to = pos;
                    bestValue = moveValue;
                }

                board.undoLastMove();
            }
        }

        return new AbstractMap.SimpleEntry(from, to);
    }

    public static int Minimax(Board board, int a, int b, int depth, boolean isMaxPlayer) {
        int positionActuelleEstFinale = EvaluationFunctions.evaluate(board, board.getMaxPlayer(), depth);

        if (depth == 0) { return positionActuelleEstFinale; }

        int high, low;
        Map<String, List<String>> possibleMoves;

        if (isMaxPlayer) {
            high = Integer.MIN_VALUE;
            possibleMoves = board.getPossibleMoves(board.getMaxPlayer());

            for (var move : possibleMoves.entrySet()) {
                String coloredPieceBoardPosition = move.getKey();

                for (var pos : move.getValue()) {

                    board.playMoveOnBoard(coloredPieceBoardPosition + " - " + pos);

                    high = Math.max(high, Minimax(board, a, b, depth - 1, false));

                    board.undoLastMove();

                    // Alpha-Beta pruning
                    a = Math.max(a, high);
                    if (a >= b) { return high; }
                }
            }

            return high;
        } else {
            low = Integer.MAX_VALUE;

            possibleMoves = board.getPossibleMoves(board.getMinPlayer());

            for (var move : possibleMoves.entrySet()) {
                String coloredPieceBoardPosition = move.getKey();

                for (var pos : move.getValue()) {

                    board.playMoveOnBoard(coloredPieceBoardPosition + " - " + pos);

                    low = Math.min(low, Minimax(board, a, b, depth - 1, true));

                    board.undoLastMove();

                    // Alpha-Beta pruning
                    b = Math.min(b, low);
                    if (b <= a) { return low; }
                }
            }
            return low;
        }
    }
}
