package modele.ai;

import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.TileState;

public class EvaluationFunctions {
    public static int depth = 0;

    public static int evaluate(Board board, TileState color){

        TileState opposite = color == TileState.NOIR ? TileState.ROUGE : TileState.NOIR;

        int colorNumberPieces = board.getPlayerPiecesCounter(color);
        int oppositeNumberPieces = board.getPlayerPiecesCounter(opposite);

        System.out.println(colorNumberPieces +  " AND " + oppositeNumberPieces);


        if(color == TileState.NOIR){
               colorNumberPieces*=24;
               oppositeNumberPieces*=13;

        } else {
            colorNumberPieces*=13;
            oppositeNumberPieces*=24;
        }

        System.out.println(colorNumberPieces +  " AND " + oppositeNumberPieces);

        return colorNumberPieces-oppositeNumberPieces;
    }


}
