import GUI.BoardFrame;

public class Main {
    public static void main(String[] args) {
        BoardFrame game = new BoardFrame("Rico", "Max");
    }
}

/*

TODO:   -jump one player (work on playerchange after jump) (muehle etc.)
        -jump two player
        -win conditions
        -Server

Phases:     0. One Player removes a Stone
            1. All PLayer Place their Stones
            2. All player move their stones
            3. One player is allowed to jump on the board
            4. All player jump on the board
            5. One Player won
 */