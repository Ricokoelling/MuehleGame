package Serverside;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import Data.PlayData;
import Data.Stone;
import Logic.LogicDealer;

public class Clienthandler implements Runnable {
    private final ObjectInputStream objReader;
    private final ObjectOutputStream objWriter;
    private final LogicDealer logic;
    private final ArrayList<Clienthandler> clients;
    private String player_name;
    private Clienthandler opponent;

    public Clienthandler(Socket client, ArrayList<Clienthandler> clients, LogicDealer logic) throws IOException {
        this.clients = clients;
        this.logic = logic;
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
                                this.objWriter.writeObject(new PlayData(90, data.getStone(), player_name));
                                opponent.objWriter.writeObject(new PlayData(91, data.getStone(), player_name));
                            } else {
                                if (logic.get_player_stones(player_name) + logic.get_player_stones(opponent.player_name) == logic.getMaxstones()) {
                                    System.out.println("Phase 2");
                                    this.objWriter.writeObject(new PlayData(29, data.getStone(), player_name));
                                    opponent.objWriter.writeObject(new PlayData(28, data.getStone(), player_name));
                                } else {
                                    this.objWriter.writeObject(new PlayData(10, data.getStone(), player_name));
                                    opponent.objWriter.writeObject(new PlayData(12, data.getStone(), player_name));
                                    logic.print_player_stones();
                                }
                            }
                        } else {
                            this.objWriter.writeObject(new PlayData(11, data.getStone(), player_name, 1));
                        }
                        break;
                    case 0:
                        Stone remove = new Stone(opponent.player_name, data.getStone().getPosOne(), data.getStone().getPosTwo(), data.getStone().getPosThree());
                        data.setStone(remove);
                        if (logic.remove(data.getStone(), opponent.player_name)) {
                            if (logic.get_player_stones(player_name) + logic.get_player_stones(opponent.player_name) == logic.getMaxstones()) {
                                if (logic.get_player_stones(opponent.player_name) == 3) {
                                    // phase 3
                                } else {
                                    this.objWriter.writeObject(new PlayData(23, data.getStone(), player_name));
                                    opponent.objWriter.writeObject(new PlayData(24, data.getStone(), player_name));
                                }
                            }
                            this.objWriter.writeObject(new PlayData(0, data.getStone(), player_name));
                            opponent.objWriter.writeObject(new PlayData(2, data.getStone(), player_name));
                        } else {
                            this.objWriter.writeObject(new PlayData(1, data.getStone(), player_name, 2));
                        }
                        break;
                    case 20:
                        System.out.println(" start: " + data.getStone() + " destination: " + data.getDestination());
                        if (!data.getStone().equal(data.getDestination()) && logic.player_stone(data.getStone(), player_name) && logic.move_possible(data.getStone(), data.getDestination(), player_name)) {
                            logic.move_stone(data.getStone(), data.getDestination(), data.getPlayer());
                            this.objWriter.writeObject(new PlayData(20, data.getStone(), data.getDestination(), player_name));
                            opponent.objWriter.writeObject(new PlayData(22, data.getStone(), data.getDestination(), player_name));
                        } else {
                            System.out.println("Bill Clinton"); //test
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
