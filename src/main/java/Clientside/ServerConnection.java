package Clientside;

import Data.PlayData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private final ObjectInputStream objReader;
    private int allowed_move = -1;
    private boolean achived_data = false;
    private PlayData serverdata;

    /**
     * Constructor
     */
    public ServerConnection(Socket server) throws IOException {
        objReader = new ObjectInputStream(server.getInputStream());
    }

    /**
     * holds constant server connection
     */
    @Override
    public void run() {
        try {
            while (true) {
                serverdata = (PlayData) objReader.readObject();
                switch (serverdata.getState()) {
                    case 10, 90, 0, 29, 20, 23, 25, 39, 30, 33, 35, 49, 40 -> allowed_move = 1;
                    case 11, 1, 21, 31 -> allowed_move = 0;
                    case 12, -1, 2, 91, 22, 24, 28, 26, 38, 32, 36, 48, 42 -> achived_data = true;
                    case 50 -> {
                        allowed_move = 1;
                        achived_data = true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * getter which automatically sets his value to default
     */
    public int isAllowed_move() {
        int temp = allowed_move;
        allowed_move = -1;
        return temp;
    }

    public PlayData getServerdata() {
        return serverdata;
    }

    /**
     * getter which automatically sets his value to default
     **/

    public boolean isAchived_data() {
        if (achived_data) {
            achived_data = false;
            return true;
        } else {
            return false;
        }
    }
}
