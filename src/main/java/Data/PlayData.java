package Data;

import java.io.Serializable;

public class PlayData implements Serializable {

    private final int state;
    private final String player;
    private Stone stone = null;
    private int reason = -1;
    private Stone destination = null;
    private boolean init;

    /**
     * Constructor only for the client
     *
     * @param state  of the game
     * @param stone  send
     * @param player this.player
     */
    public PlayData(int state, Stone stone, String player) {
        this.state = state;
        this.stone = stone;
        this.player = player;
    }

    public PlayData(int state, Stone start, Stone destination, String player) {
        this.state = state;
        this.stone = start;
        this.destination = destination;
        this.player = player;
    }

    /**
     * Constructor only for the client
     *
     * @param state  of the game
     * @param stone  send
     * @param player this.player
     * @param reason siehe reason
     */
    public PlayData(int state, Stone stone, String player, int reason) {
        this.state = state;
        this.stone = stone;
        this.player = player;
        this.reason = reason;
    }

    public PlayData(int state, Stone start, Stone destination, String player, int reason) {
        this.state = state;
        this.stone = start;
        this.destination = destination;
        this.player = player;
        this.reason = reason;
    }

    public PlayData(int state, String opponent, boolean init) {
        this.state = state;
        this.init = init;
        this.player = opponent;  // opponent name gets send to client.
    }

    // Setter
    public int getState() {
        return state;
    }

    public Stone getStone() {
        return stone;
    }

    public void setStone(Stone stone) {
        this.stone = stone;
    }


    // Getter
    public String getPlayer() {
        return player;
    }

    public int getReason() {
        return reason;
    }

    public Stone getDestination() {
        return destination;
    }

    public boolean isInit() {
        return init;
    }
}



/*
    state:
        -1 - init (Client -> Server)

           Reasons:
                1 - Position was taken
                2 - not your stone
                3 - move wasn't possible
                4 - jump wasn't possible


        10 - player wants to place a stone (Client -> Server) <-> move was accepted (Server -> Client)
        11 - placement was declined (Server -> Client) (siehe Reasons)
        12 - other Client places this stone on his board (Sever -> Client)


        90 - player who just placed his stone got a mill (Server -> Client) ----> needs another server message
        91 - other player got a mill, so you have to wait (Server -> Client) ----> waits for another message

        Phase change to 0:
        00 - player wants to remove a stone (Client -> Server) <-> move was accepted (Server -> Client)
        01 - remove was declined (Server - Client)
        02 - other player removes a stone (Server -> Client)

        Phase change to 2:
        28 - phase change to 2 and the player is on the move  (Server -> Client)
        29 - phase change to 2 and the other player can move (Server -> Client) (isn't on move)
        20 - player wants to move to a certain position (Client -> Server)  <-> move was accepted (Server -> Client)
        21 - move was declined (Server -> Client)
        22 - other player moves a stone (Server -> Client)
        23 - from remove to player (Server -> Client)
        24 - from remove to opponent (Server -> Client)
        25 - allowed remove (Server -> Client)
        26 - other player removes a stone( Server -> Client)

        Phase change to 3: reason 30 (player jump) reason 20 (player move)
        38 - phase change to phase 3 and this player moves (he is in phase 3 ) (Server -> Client)
        39 - phase change to phase 3 and the other player can move (Server -> Client)
        30 - player wants to jump (Client -> Server) <-> move was accepted (Server -> Client)
        31 - move was declined (Server -> Client)
        32 - other player jumps (Server -> Client)
        33 - from remove to player (Server -> Client)
        34 - from remove to opponent (Server -> Client)
        35 - allowed remove (Server -> Client)
        36 - other player removes (Server -> Client)


 */
