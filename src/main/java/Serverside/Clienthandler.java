package Serverside;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import Data.PlayData;
import Logic.LogicDealer;

public class Clienthandler implements Runnable {
    private final Socket client;
    private final ObjectInputStream objReader;
    private final ObjectOutputStream objWriter;
    private final ArrayList<Clienthandler> ALLclients;
    private final ArrayList<Clienthandler> clients = new ArrayList<>();
    private final LogicDealer logic = new LogicDealer();
    private String player_name;
    private Clienthandler opponent;

    public Clienthandler(Socket client, ArrayList<Clienthandler> clients) throws IOException {
        this.client = client;
        this.ALLclients = clients;
        objWriter = new ObjectOutputStream(client.getOutputStream());
        objReader = new ObjectInputStream(client.getInputStream());
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setOpponent(ArrayList<Clienthandler> clients) {
        if (!Objects.equals(clients.get(0).getPlayer_name(), player_name)) {
            opponent = clients.get(0);
        } else if (!Objects.equals(clients.get(1).getPlayer_name(), player_name)) {
            opponent = clients.get(1);
        }
        logic.setPlayerOne(player_name);
        logic.setPlayerTwo(opponent.getPlayer_name());
    }

    @Override
    public void run() {
        try {
            while (true) {
                PlayData data = (PlayData) objReader.readObject();
                switch (data.getState()) {
                    case -1:
                        player_name = data.getPlayer();
                        while (true) {
                            Thread.sleep(20);
                            if (clients.size() > 1) {
                                setOpponent(clients);
                                break;
                            }
                        }
                        break;
                    case 10:
                        if (logic.free_position(data.getStone())) {
                            logic.placeStone(data.getStone());
                            if (logic.get_player_stones(player_name) > 3) {
                                if (logic.muehle(player_name)) {
                                    System.out.println("Player " + player_name + "got a mill.");

                                }
                            }
                            this.objWriter.writeObject(new PlayData(10, data.getStone(), player_name));
                            opponent.objWriter.writeObject(new PlayData(12, data.getStone(), player_name));
                        }
                    default:
                        System.err.println("Wrong State: " + data.getState());
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
