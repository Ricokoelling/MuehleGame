package Data;

import java.io.Serializable;
import java.util.Objects;

public class Stone implements Serializable {

    private final String player;
    private int posOne;
    private int posTwo;
    private int posThree;

    public Stone(String player, int posOne, int posTwo, int posThree) {
        this.player = player;
        this.posOne = posOne;
        this.posTwo = posTwo;
        this.posThree = posThree;
    }

    public int getPosOne() {
        return posOne;
    }

    public int getPosTwo() {
        return posTwo;
    }

    public int getPosThree() {
        return posThree;
    }

    public String getPlayer() {
        return player;
    }

    public boolean equal(Stone stone) {
        return stone.getPosOne() == this.getPosOne() && stone.getPosTwo() == this.getPosTwo() && stone.getPosThree() == this.getPosThree() && Objects.equals(stone.getPlayer(), this.getPlayer());
    }

    public boolean equals(Stone stone) {
        return stone.getPosOne() == this.getPosOne() && stone.getPosTwo() == this.getPosTwo() && stone.getPosThree() == this.getPosThree();
    }

    public void setPositions(int x, int y, int z) {
        this.posOne = x;
        this.posTwo = y;
        this.posThree = z;

    }

    @Override
    public String toString() {
        return "Stone{" +
                "player = '" + player + '\'' +
                ", posOne = " + posOne +
                ", posTwo = " + posTwo +
                ", posThree = " + posThree +
                '}';
    }
}
