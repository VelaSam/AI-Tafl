package modele.helpers;

public class IllegalMoveException extends Exception{
    public IllegalMoveException(String move, String reason){
        super("The move(" + move + ") is illegal because of this reason: \"" + reason + "\"");
    }
}
