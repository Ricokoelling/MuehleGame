import Clientside.ClientBoard;
import OfflineGame.BoardFrame;

public class Main {
    public static void main(String[] args) {
        //BoardFrame frame = new BoardFrame("Rico", "max");
        ClientBoard clientBoard = new ClientBoard("Rico");
    }
}

/*

TODO:   - phase 4
        - win conditions
        - test everything 10 times (full matches)
        - test win cant move (...)


Phases:     0. One Player removes a Stone
            1. All PLayer Place their Stones
            2. All player move their stones
            3. One player is allowed to jump on the board
            4. All player jump on the board
            5. One Player won
 */