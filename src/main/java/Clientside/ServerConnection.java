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

    public ServerConnection(Socket server) throws IOException {
        objReader = new ObjectInputStream(server.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                serverdata = (PlayData) objReader.readObject();
                switch (serverdata.getState()) {
                    case 10, 90, 0, 29, 20, 23:
                        allowed_move = 1;
                        break;
                    case 11, 1:
                        allowed_move = 0;
                        break;
                    case 12, -1, 91, 22, 24, 28:
                        achived_data = true;
                        break;

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int isAllowed_move() {
        int temp = allowed_move;
        allowed_move = -1;
        return temp;
    }

    public PlayData getServerdata() {
        return serverdata;
    }

    public boolean isAchived_data() {
        if (achived_data) {
            achived_data = false;
            return true;
        } else {
            return false;
        }
    }
}
