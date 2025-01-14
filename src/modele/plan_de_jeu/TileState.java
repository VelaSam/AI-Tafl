package modele.plan_de_jeu;

public enum TileState {
    EMPTY(0), NOIR(2), ROUGE(4), ROI_NOIR(5);

    private final int value;

    TileState(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public static TileState getTileState(int tileStateValue) {
        for (TileState t : TileState.values()) {
            if (t.value == tileStateValue)
                return t;
        }
        throw new IllegalArgumentException("TileState not found.");
    }

    public String toString(){
        if(this == EMPTY){
            return "_";
        }
        if(this == NOIR){
            return "N";
        }
        if(this == ROUGE){
            return "R";
        }
        if(this == ROI_NOIR){
            return "K";
        }
        return "?";
    }
}
