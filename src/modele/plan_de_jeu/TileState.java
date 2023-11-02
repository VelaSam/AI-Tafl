package modele.plan_de_jeu;

public enum TileState {
    EMPTY(0),
    NOIR(2),
    ROUGE(4),
    ROI(5);

    private final int value;

    TileState(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public boolean isEmpty(){
        return this == EMPTY;
    }
}
