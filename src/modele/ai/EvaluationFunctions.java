package modele.ai;

import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.TileState;

public class EvaluationFunctions {

    public static int evaluate(Board board, TileState color){

        TileState opposite = color == TileState.NOIR ? TileState.ROUGE : TileState.NOIR;

        if(kingInCorner(board)){
            return color == TileState.ROUGE ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }

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

    private static boolean kingInCorner(Board board){
        if(board.getTiles()[0][0].getState() == TileState.ROI_NOIR || board.getTiles()[board.WIDTH - 1][board.WIDTH - 1].getState() == TileState.ROI_NOIR
            || board.getTiles()[board.WIDTH - 1][0].getState() == TileState.ROI_NOIR || board.getTiles()[0][board.WIDTH - 1].getState() == TileState.ROI_NOIR){
            return true;
        }
        return false;
    }


}
