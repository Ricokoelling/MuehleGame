import Clientside.ClientBoard;
import OfflineGame.BoardFrame;

public class Main {
    public static void main(String[] args) {
        ClientBoard clientBoard = new ClientBoard("Rico");
    }
}

/*

TODO:   - implement what happens mill (remove etc)
        - phase change (send)
        - phase 2
        - test all sends etc.
        - test win cant move (...)


Phases:     0. One Player removes a Stone
            1. All PLayer Place their Stones
            2. All player move their stones
            3. One player is allowed to jump on the board
            4. All player jump on the board
            5. One Player won
 */