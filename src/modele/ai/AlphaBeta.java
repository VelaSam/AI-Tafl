package modele.ai;

import modele.helpers.Helpers;
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

    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seulement si plusieurs coups
    // ont le même score.
    public Map<Tile, ArrayList<Position>> getNextMoveAB(Board board) {

        playerColor = board.getMaxPlayer();
        numExploredNodes = 0;
        Map<Tile, ArrayList<Position>> result = new HashMap<>();

        // Nous commençons avec le joueur Max, car nous sommes le joueur Min pour le
        // cpu.
        double resultValue = Double.NEGATIVE_INFINITY;

        Map<Tile, List<Position>> possibleMoves = board.getPossibleMoves();

        for (Map.Entry<Tile, List<Position>> entry : possibleMoves.entrySet()) {
            Tile tile = entry.getKey();
            List<Position> positions = entry.getValue();
            ArrayList<Position> positionsToSend = new ArrayList<>();

            for (Position position : positions) {
                Board boardWithNextMove = board.checkMove(tile, position, playerColor);
                double value = minValueAlphaBeta(boardWithNextMove, boardWithNextMove.getMinPlayer(), resultValue, Double.POSITIVE_INFINITY);
                if (value >= resultValue) {
                    if (value > resultValue) {
                        result.clear();
                    }
                    positionsToSend.add(position); // ???????????
                    System.out.println(positionsToSend);
                    resultValue = value;
                }
            }
            if (!positionsToSend.isEmpty()) {
                result.put(tile, positionsToSend);
            }
        }
        return result;
    }

    public double maxValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta) {
        numExploredNodes++;
        int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur);
        // Game is done
        if (positionActuelleEstFinale != -1)
            return positionActuelleEstFinale;

        // Game is not done
        double value = Double.NEGATIVE_INFINITY;
        Map<Tile, List<Position>> possibleMoves = positionActuelle.getPossibleMoves();

        for (Map.Entry<Tile, List<Position>> entry : possibleMoves.entrySet()) {
            List<Position> positions = entry.getValue();

            for (Position position : positions) {
                Board boardWithNextMove = positionActuelle.checkMove(entry.getKey(), position, playerColor);
                double score = minValueAlphaBeta(boardWithNextMove, boardWithNextMove.getMinPlayer(),
                        Math.max(alpha, value), beta);
                value = Math.max(value, score);
                alpha = Math.max(alpha, value);
                if (alpha >= beta)
                    return value;
            }
        }

        return value;
    }

    public double minValueAlphaBeta(Board positionActuelle, TileState joueur, double alpha, double beta) {
        numExploredNodes++;
        int positionActuelleEstFinale = EvaluationFunctions.evaluate(positionActuelle, joueur);
        // Game is done
        if (positionActuelleEstFinale != -1)
            return positionActuelleEstFinale;

        double value = Double.POSITIVE_INFINITY;
        Map<Tile, List<Position>> possibleMoves = positionActuelle.getPossibleMoves();

        for (Map.Entry<Tile, List<Position>> entry : possibleMoves.entrySet()) {
            List<Position> positions = entry.getValue();

            for (Position position : positions) {
                Board boardWithNextMove = positionActuelle.checkMove(entry.getKey(), position, playerColor);
                double score = maxValueAlphaBeta(boardWithNextMove, boardWithNextMove.getMinPlayer(),
                        Math.min(alpha, value), beta);
                value = Math.min(value, score);
                beta = Math.min(beta, value);
                if (beta <= alpha)
                    return value;
            }
        }
        return value;
    }

}
