package Clientside;

import Data.PlayData;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection implements Runnable {

    private final Socket server;
    private final ObjectInputStream objReader;
    private int allowed_move = -1;
    private boolean achived_data = false;
    private PlayData serverdata;

    public ServerConnection(Socket server) throws IOException {
        this.server = server;
        objReader = new ObjectInputStream(server.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                serverdata = (PlayData) objReader.readObject();
                switch (serverdata.getState()) {
                    case 10:
                        allowed_move = 1;
                        break;
                    case 11:
                        allowed_move = 0;
                        break;
                    case 12:
                        achived_data = true;
                        break;

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int isAllowed_move() {
        return allowed_move;
    }

    public void setAllowed_move(int allowed_move) {
        this.allowed_move = allowed_move;
    }

    public PlayData getServerdata() {
        return serverdata;
    }

    public boolean isAchived_data() {
        return achived_data;
    }
}
