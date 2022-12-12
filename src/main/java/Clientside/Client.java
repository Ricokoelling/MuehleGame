package Clientside;

import Data.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class Client {

    private final String player;
    ServerConnection serverConn;
    private ObjectOutputStream objWriter;
    private PlayData serverdata;
    private PlayData sendata;


    public Client(String player) {
        try {
            Socket client = new Socket("localhost", 1337);
            serverConn = new ServerConnection(client);
            objWriter = new ObjectOutputStream(client.getOutputStream());
            System.out.println("[ClientSide.Client] connected");
        } catch (IOException e) {
            System.out.println("Beim Erstellen des Clients ist ein Fehler aufgetreten");
            e.printStackTrace();
        }
        new Thread(serverConn).start();
        this.player = player;
        send_init();

    }
    // *******************************************************************************

    // Send Stuff
    private void send_init() {
        PlayData data = new PlayData(-1, null, player);
        try {
            objWriter.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendData(Stone stone, String type) {
        PlayData data = null;
        if (Objects.equals("mouse_pressed", type)) {
            data = new PlayData(10, stone, player);
        } else if (Objects.equals("remove_mouse_pressed", type)) {
            data = new PlayData(0, stone, player);
        }

        assert data != null;
        sendata = data;
        try {
            objWriter.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendData(Stone start, Stone destination, String type) {
        PlayData data = null;
        if (Objects.equals("mouse_dragged", type)) {
            data = new PlayData(20, start, destination, player);
        }

        assert data != null;
        sendata = data;
        try {
            objWriter.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // *******************************************************************************

    //Wait Stuff
    //
    public int wait_for_allowed() {
        int temp = serverConn.isAllowed_move();
        if (temp == 1) {
            serverdata = serverConn.getServerdata();
            return 1;
        } else if (temp == 0) {
            serverdata = serverConn.getServerdata();
            return 0;
        }
        return -1;
    }

    public boolean wait_for_data() {
        if (serverConn.isAchived_data()) {
            serverdata = serverConn.getServerdata();
            return true;
        }
        return false;
    }

    // *******************************************************************************

    //Getter
    public PlayData getServerdata() {
        return serverdata;
    }

    public PlayData getSendata() {
        return sendata;
    }

    //Setter

    public void setOpponent(String opponent) {
    }
}
