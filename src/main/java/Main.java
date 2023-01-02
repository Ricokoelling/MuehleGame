import Clientside.ClientBoard;
import OfflineGame.BoardFrame;

public class Main {
    public static void main(String[] args) {
        //BoardFrame frame = new BoardFrame("Rico", "Max");
        ClientBoard clientBoard = new ClientBoard("Rico");
    }
}

/*

Phases:     0. One Player removes a Stone
            1. All PLayer Place their Stones
            2. All player move their stones
            3. One player is allowed to jump on the board
            4. All player jump on the board
            5. One Player won
 */