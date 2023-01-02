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


    /**
     * Constructor
     */
    public Client(String player) {
        try {
            Socket client = new Socket("localhost", 1337);
            serverConn = new ServerConnection(client);
            objWriter = new ObjectOutputStream(client.getOutputStream());
            System.out.println("[Client] connected");

        } catch (IOException e) {
            System.out.println("Beim Erstellen des Clients ist ein Fehler aufgetreten");
            e.printStackTrace();
        }
        new Thread(serverConn).start();
        this.player = player;
        send_init();

    }
    // *******************************************************************************

    // Send

    /**
     * sends initial data to the server
     */
    private void send_init() {
        PlayData data = new PlayData(-1, null, player);
        try {
            objWriter.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends data from the mouse events to the server
     *
     * @param type whether it was place or remove
     */
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

    /**
     * sends data from the mouse events to the server
     *
     * @param type whether it was move or jump
     */
    protected void sendData(Stone start, Stone destination, String type) {
        PlayData data = null;
        if (Objects.equals("mouse_dragged", type)) {
            data = new PlayData(20, start, destination, player);
        } else if (Objects.equals("2_mouse_released", type)) {
            data = new PlayData(30, start, destination, player, 20);
        } else if (Objects.equals("3_mouse_released", type)) {
            data = new PlayData(30, start, destination, player, 30);
        } else if (Objects.equals("4_mouse_released", type)) {
            data = new PlayData(40, start, destination, player);
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

    //Wait

    /**
     * waits till the server responded after sending out data
     */
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

    /**
     * waits for data from the server
     */
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
}
