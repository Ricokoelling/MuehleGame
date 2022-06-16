package Logic;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class LogicDealer {

    private static Stone[][][] board = new Stone[3][3][3];
    private static ArrayList<Stone> playerOneStones = new ArrayList<>();
    private static ArrayList<Stone> playerTwoStones = new ArrayList<>();
    private String playerOne;
    private String playerTwo;
    public LogicDealer() {
    }
    public LogicDealer(String playerOne, String playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }
    public void placeStone(Stone stone){
        board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = stone;
        if(stone.getPlayer().equalsIgnoreCase(this.playerOne)){
            playerOneStones.add(stone);
        }else if(stone.getPlayer().equalsIgnoreCase(this.playerTwo)){
            playerTwoStones.add(stone);
        }else {
            System.err.println("[Logic] false Player name" + stone.getPlayer());
        }
    }

}
