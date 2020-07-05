package View;

import javafx.scene.shape.Arc;

public class ArcView extends Arc {
    private int prize;

    public ArcView(int prize) {
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }
}
