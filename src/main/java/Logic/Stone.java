package Logic;

import java.util.Objects;

public class Stone {

    private final String player;
    private final int posOne;
    private final int posTwo;
    private final int posThree;

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

    public boolean equal(Stone stone){
        return stone.getPosOne() == this.getPosOne() && stone.getPosTwo() == this.getPosTwo() && stone.getPosThree() == this.getPosThree() && Objects.equals(stone.getPlayer(), this.getPlayer());
    }
    @Override
    public String toString() {
        return "Stone{" +
                "player='" + player + '\'' +
                ", posOne=" + posOne +
                ", posTwo=" + posTwo +
                ", posThree=" + posThree +
                '}';
    }
}
