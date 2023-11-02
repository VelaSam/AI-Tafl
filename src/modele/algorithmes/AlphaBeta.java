package modele.algorithmes;

import modele.plan_de_jeu.Board;

import java.util.ArrayList;

public class AlphaBeta {
    private int numExploredNodes;

//    // Retourne la liste des coups possibles. Cette liste contient
//    // plusieurs coups possibles si et seuleument si plusieurs coups
//    // ont le même score.
//    public ArrayList<Move> getNextMoveAB(Board board) {
//        numExploredNodes = 0;
//        ArrayList<Move> result = new ArrayList<>();
//
//        // Nous commençons avec le joueur Max, car nous sommes le joueur Min pour le
//        // cpu.
//        double resultValue = Double.NEGATIVE_INFINITY;
//        ArrayList<Move> possibleMoves = board.getPossibleMoves();
//
//        for (Move move : possibleMoves) {
//            Board boardWithNextMove = board.checkMove(move, playerMark);
//            double value = minValueAlphaBeta(boardWithNextMove, boardWithNextMove.getNextMarkToPlay(), resultValue, Double.POSITIVE_INFINITY);
//            if (value >= resultValue) {
//                if (value > resultValue) {
//                    result.clear();
//                }
//                result.add(move);
//                resultValue = value;
//            }
//        }
//        return result;
//    }
//
//    /**
//     *
//     * @param positionActuelle
//     * @param joueur
//     * @param alpha
//     * @param beta
//     * @return
//     */
//    public double maxValueAlphaBeta(Board positionActuelle, Mark joueur, double alpha, double beta) {
//        numExploredNodes++;
//        int positionActuelleEstFinale = positionActuelle.evaluate(playerMark);
//        // Game is done
//        if (positionActuelleEstFinale != -1)
//            return positionActuelleEstFinale;
//
//        // Game is not done
//        double value = Double.NEGATIVE_INFINITY;
//        ArrayList<Move> possibleMoves = positionActuelle.getPossibleMoves();
//
//        for (Move move : possibleMoves) {
//            Board boardWithNextMove = positionActuelle.checkMove(move, joueur);
//            double score = minValueAlphaBeta(boardWithNextMove, boardWithNextMove.getNextMarkToPlay(),
//                    Math.max(alpha, value), beta);
//            value = Math.max(value, score);
//            alpha = Math.max(alpha, value);
//            if (alpha >= beta)
//                return value;
//        }
//
//        return value;
//    }
//
//    /**
//     *
//     * @param positionActuelle
//     * @param joueur
//     * @param alpha
//     * @param beta
//     * @return
//     */
//    public double minValueAlphaBeta(Board positionActuelle, Mark joueur, double alpha, double beta) {
//        numExploredNodes++;
//        int positionActuelleEstFinale = positionActuelle.evaluate(playerMark);
//        // Game is done
//        if (positionActuelleEstFinale != -1)
//            return positionActuelleEstFinale;
//
//        double value = Double.POSITIVE_INFINITY;
//        ArrayList<Move> possibleMoves = positionActuelle.getPossibleMoves();
//
//        for (Move move : possibleMoves) {
//            Board boardWithNextMove = positionActuelle.checkMove(move, joueur);
//            double score = maxValueAlphaBeta(boardWithNextMove, boardWithNextMove.getNextMarkToPlay(),
//                    Math.min(alpha, value), beta);
//            value = Math.min(value, score);
//            beta = Math.min(beta, value);
//            if (beta <= alpha)
//                return value;
//        }
//        return value;
//    }
}
