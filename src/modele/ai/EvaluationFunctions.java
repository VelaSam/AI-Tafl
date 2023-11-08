package modele.ai;

import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.TileState;

import java.util.Random;

public class EvaluationFunctions {
    static Random random = new Random();

    public static int evaluate(Board board, TileState color){
        return random.nextInt();
    }
}
