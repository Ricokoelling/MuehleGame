package Logic;

import java.sql.SQLOutput;

public class LogicDealer {

    private static Stone[][][] board = new Stone[3][3][3];

    public LogicDealer() {
    }

    public void placeStone(Stone stone){
        board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = stone;
    }

}
