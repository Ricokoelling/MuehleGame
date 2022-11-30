package Serverside;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import Logic.LogicDealer;
import Data.PlayData;

public class Clienthandler implements Runnable {
    private final Socket client;
    private final ObjectInputStream objReader;
    private final ObjectOutputStream objWriter;
    private final ArrayList<Clienthandler> ALLclients;
    private final ArrayList<Clienthandler> clients = new ArrayList<>();

    public Clienthandler(Socket client, ArrayList<Clienthandler> clients) throws IOException {
        this.client = client;
        this.ALLclients = clients;
        objWriter = new ObjectOutputStream(client.getOutputStream());
        objReader = new ObjectInputStream(client.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                PlayData data = (PlayData) objReader.readObject();
                System.out.println("Data: " + data.getState() + " " + data.getPlayer() + " \n" + data.getStone());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
