package Logic;

public class Stone {

    private String player;
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
}
