package Model;

import javafx.scene.paint.Color;

public class SectorModel
{
    private int prize;
    private Color color;

    public SectorModel(int prize, Color color) {
        this.prize = prize;
        this.color = color;
    }

    public int getPrize() {
        return prize;
    }

    public Color getColor() {
        return color;
    }
}