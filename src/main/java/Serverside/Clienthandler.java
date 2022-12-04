package Serverside;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import Data.PlayData;
import Logic.LogicDealer;

public class Clienthandler implements Runnable {
    private final ObjectInputStream objReader;
    private final ObjectOutputStream objWriter;
    private final LogicDealer logic;
    private final Socket client;
    private ArrayList<Clienthandler> clients = new ArrayList<>();
    private String player_name;
    private Clienthandler opponent;

    public Clienthandler(Socket client, ArrayList<Clienthandler> clients, LogicDealer logic) throws IOException {
        this.client = client;
        this.clients = clients;
        this.logic = logic;
        objWriter = new ObjectOutputStream(client.getOutputStream());
        objReader = new ObjectInputStream(client.getInputStream());
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setOpponent(ArrayList<Clienthandler> clients) throws IOException {
        if (!Objects.equals(clients.get(0).getPlayer_name(), player_name)) {
            opponent = clients.get(0);
        } else if (!Objects.equals(clients.get(1).getPlayer_name(), player_name)) {
            opponent = clients.get(1);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                PlayData data = (PlayData) objReader.readObject();
                System.out.println("**********************************************************");
                System.out.println();
                System.out.println(player_name);
                System.out.println("State: " + data.getState() + " send from: " + data.getPlayer());
                switch (data.getState()) {
                    case -1:
                        player_name = data.getPlayer();
                        if (clients.size() == 1) {
                            logic.setPlayerOne(player_name);
                        } else if (clients.size() == 2) {
                            logic.setPlayerTwo(player_name);
                            setOpponent(clients);
                            this.objWriter.writeObject(new PlayData(-1, opponent.getPlayer_name(), false));
                            opponent.objWriter.writeObject(new PlayData(-1, this.getPlayer_name(), true));
                            opponent.setOpponent(clients);
                        } else {
                            System.err.println("[SERVER] Too many clients!!");
                        }
                        break;
                    case 10:
                        if (logic.free_position(data.getStone())) {
                            logic.placeStone(data.getStone());
                            System.out.println(logic.get_player_stones(player_name));
                            if (logic.get_player_stones(player_name) > 2 && logic.muehle(player_name)) {
                                System.out.println("Player " + player_name + " got a mill.");

                            } else {
                                this.objWriter.writeObject(new PlayData(10, data.getStone(), player_name));
                                opponent.objWriter.writeObject(new PlayData(12, data.getStone(), player_name));
                                logic.print_player_stones();
                            }
                        } else {
                            this.objWriter.writeObject(new PlayData(11, data.getStone(), player_name));
                        }
                        break;
                    default:
                        System.err.println("Wrong State: " + data.getState());
                        break;
                }
                System.out.println();
                System.out.println("**********************************************************");
                System.out.println("/////////////////////////////////////////");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
