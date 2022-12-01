package Data;

import java.io.Serializable;

public class PlayData implements Serializable {

    private int state = -1;
    private Stone stone = null;
    private String player = null;
    private int reason = -1;
    private Stone destination = null;

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

    public int getState() {
        return state;
    }

    public Stone getStone() {
        return stone;
    }

    public String getPlayer() {
        return player;
    }

    public int getReason() {
        return reason;
    }
}



/*
    state:
        -1 - init (Client -> Server)

        0  - Player move was accepted by the server and send to the other player (Server -> client)
        1  - Player move was declined : reason (Server -> Client)
             Reasons:
                1 - Position was taken
                2 - Not a possible move
                3 - Not your Stone
                4 - Wrong Stone (Remove)
                5 - Wrong Player
                6 - Unknown Cause


        10 - player wants to place a stone (Client -> Server) <-> move was accepted (Server -> Client)
        11 - placement was declined (Server -> Client)
        12 - other Client places this stone on his board (Sever -> Client)




 */
