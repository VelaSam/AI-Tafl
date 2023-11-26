package modele.ai;

import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.Position;
import modele.plan_de_jeu.Tile;
import modele.plan_de_jeu.TileState;

import java.util.*;

public class AlphaBeta {
    int numExploredNodes;
    private TileState playerColor;

    public static final int depth = 2;

    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seulement si plusieurs coups
    // ont le même score.
    public static Map.Entry<Tile, Position> getNextMoveAB(Board board) {
        Tile from = null;
        Position to = null;
        int bestValue = Integer.MIN_VALUE;
        var maxPlayer = board.getMaxPlayer();

        TileState stateTo, stateFrom;

        Map<Tile, List<Position>> possibleMoves = board.getPossibleMoves();

        for (var move : possibleMoves.entrySet()) {
            for (Position pos : move.getValue()) {

                // Make move

//                Board boardWithNextMove = board.checkMove(board.getTiles()[pos.getX()][pos.getY()], move.getKey().getPosition(), board.getMaxPlayer());
                Board boardWithNextMove = board.checkMove(move.getKey(), board.getTiles()[pos.getX()][pos.getY()].getPosition(), board.getMaxPlayer());

                int moveValue = AlphaBeta.Minimax(boardWithNextMove, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, false);

                if (moveValue > bestValue) {
                    from = move.getKey();
                    to = pos;
                    bestValue = moveValue;
                }
            }
        }

        return new AbstractMap.SimpleEntry(from, to);
    }

    public static int Minimax(Board board, int a, int b, int depth, boolean isMaxPlayer) {
        int value = EvaluationFunctions.evaluate(board, board.getMaxPlayer());

        if (depth == 0) { return value; }

        int high, low;
        TileState stateTo, stateFrom;
        Map<Tile, List<Position>> possibleMoves;

        if (isMaxPlayer) {
            high = Integer.MIN_VALUE;

            possibleMoves = board.getPossibleMoves();

            for (var move : possibleMoves.entrySet()) {
                for (Position pos : move.getValue()) {
//                    Board boardWithNextMove = board.checkMove(board.getTiles()[pos.getX()][pos.getY()], move.getKey().getPosition(), board.getMaxPlayer());
                    Board boardWithNextMove = board.checkMove(move.getKey(), board.getTiles()[pos.getX()][pos.getY()].getPosition(), board.getMaxPlayer());

                    high = Math.max(high, Minimax(boardWithNextMove, a, b, depth - 1, false));

                    // Alpha-Beta pruning
                    a = Math.max(a, high);
                    if (a >= b) { return high; }
                }
            }

            return high;
        } else {
            low = Integer.MAX_VALUE;

            possibleMoves = board.getPossibleMoves();

            for (var move : possibleMoves.entrySet()) {
                for (Position pos : move.getValue()) {
//                    Board boardWithNextMove = board.checkMove(board.getTiles()[pos.getX()][pos.getY()], move.getKey().getPosition(), board.getMaxPlayer());
                    Board boardWithNextMove = board.checkMove(move.getKey(), board.getTiles()[pos.getX()][pos.getY()].getPosition(), board.getMaxPlayer());

                    low = Math.min(low, Minimax(boardWithNextMove, a, b, depth - 1, true));

                    // Alpha-Beta pruning
                    b = Math.min(b, low);
                    if (b <= a) { return low; }
                }
            }

            return low;
        }
    }
//    public Map<Tile, ArrayList<Position>> getNextMoveAB(Board board) {
//
//
//        playerColor = board.getMaxPlayer();
//        numExploredNodes = 0;
//
//        Map<Tile, ArrayList<Position>> result = new HashMap<>();
//        double resultValue = Double.NEGATIVE_INFINITY;
//
//        //get possible moves
//        Map<Tile, List<Position>> possibleMoves = board.getPossibleMoves();
//
//        for (Map.Entry<Tile, List<Position>> entry : possibleMoves.entrySet()) {
//            Tile coloredPiece = entry.getKey();
//            List<Position> possibleMovesFromColoredPiece = entry.getValue();
//
//            ArrayList<Position> positionsToSend = new ArrayList<>();
//
//            for (Position position : possibleMovesFromColoredPiece) {
//                Board boardWithNextMove = board.checkMove(coloredPiece, position, playerColor);
//                double value = minValueAlphaBeta(boardWithNextMove, boardWithNextMove.getMinPlayer(), resultValue, Double.POSITIVE_INFINITY, depth);
//                if (value >= resultValue) {
//                    if (value > resultValue) {
////                        result.clear();
//                        positionsToSend.clear();
//                    }
//                    positionsToSend.add(position); // ???????????
////                    System.out.println(positionsToSend);
//                    resultValue = value;
//                }
//            }
//            if (!positionsToSend.isEmpty()) {
//                result.put(coloredPiece, positionsToSend);
//            }
//        }
//        return result;
//    }

    public double maxValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta, int depth) {
        numExploredNodes++;
        int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur);

        if(depth == 0){
            if (positionActuelleEstFinale != 0) {
                System.out.println("yo gee MAX " + positionActuelleEstFinale);
            }
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
            if (positionActuelleEstFinale != 0) {
                System.out.println("yo gee MIN " + positionActuelleEstFinale);
            }
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
