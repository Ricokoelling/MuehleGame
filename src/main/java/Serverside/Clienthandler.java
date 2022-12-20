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
    private int old_case = -1; //used for remove from different phases

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

    public void reset() { //resets the howl board
        old_case = -1;
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
                                old_case = 10;
                                this.objWriter.writeObject(new PlayData(90, data.getStone(), player_name));
                                opponent.objWriter.writeObject(new PlayData(91, data.getStone(), player_name));
                            } else {
                                if (logic.get_player_stones(player_name) + logic.get_player_stones(opponent.player_name) == logic.getMaxstones()) {
                                    System.out.println("Phase 2");
                                    this.objWriter.writeObject(new PlayData(29, data.getStone(), player_name, 1));
                                    opponent.objWriter.writeObject(new PlayData(28, data.getStone(), player_name, 1));
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
                            System.out.println(player_name + " " + logic.get_player_stones(player_name));
                            System.out.println(opponent.player_name + " " + logic.get_player_stones(opponent.player_name));
                            System.out.println("maxstones: " + logic.getMaxstones());
                            if (logic.get_player_stones(player_name) + logic.get_player_stones(opponent.player_name) == logic.getMaxstones()) {
                                if (logic.get_player_stones(opponent.player_name) == 3 || logic.get_player_stones(player_name) == 3) {
                                    if (logic.get_player_stones(opponent.player_name) == 3) {
                                        this.objWriter.writeObject(new PlayData(39, data.getStone(), player_name, 20));
                                        opponent.objWriter.writeObject(new PlayData(38, data.getStone(), player_name, 30));
                                    } else {
                                        this.objWriter.writeObject(new PlayData(39, data.getStone(), player_name, 30));
                                        opponent.objWriter.writeObject(new PlayData(38, data.getStone(), player_name, 20));
                                    }
                                } else {
                                    this.objWriter.writeObject(new PlayData(29, data.getStone(), player_name, 0));
                                    opponent.objWriter.writeObject(new PlayData(28, data.getStone(), player_name, 0));
                                }
                            } else {
                                if (old_case == 10) {
                                    this.objWriter.writeObject(new PlayData(0, data.getStone(), player_name));
                                    opponent.objWriter.writeObject(new PlayData(2, data.getStone(), player_name));
                                } else if (old_case == 20) {
                                    this.objWriter.writeObject(new PlayData(25, data.getStone(), player_name));
                                    opponent.objWriter.writeObject(new PlayData(26, data.getStone(), player_name));
                                } else if (old_case == 30) {
                                    if (logic.get_player_stones(player_name) == 3 ^ logic.get_player_stones(opponent.getPlayer_name()) == 3) {
                                        this.objWriter.writeObject(new PlayData(35, data.getStone(), player_name));
                                        opponent.objWriter.writeObject(new PlayData(26, data.getStone(), player_name));
                                    } else if (logic.get_player_stones(player_name) == 3 && logic.get_player_stones(opponent.getPlayer_name()) == 3) {
                                        this.objWriter.writeObject(new PlayData(49, data.getStone(), player_name));
                                        opponent.objWriter.writeObject(new PlayData(48, data.getStone(), player_name));
                                    }
                                }
                            }
                        } else {
                            this.objWriter.writeObject(new PlayData(1, data.getStone(), player_name, 2));
                        }
                        break;
                    case 20:
                        System.out.println(" start: " + data.getStone() + " destination: " + data.getDestination());
                        if (!data.getStone().equal(data.getDestination()) && logic.player_stone(data.getStone(), player_name) && logic.move_possible(data.getStone(), data.getDestination(), player_name)) {
                            logic.move_stone(data.getStone(), data.getDestination(), data.getPlayer());
                            if (logic.muehle(player_name)) {
                                old_case = 20;
                                this.objWriter.writeObject(new PlayData(23, data.getStone(), data.getDestination(), player_name));
                                opponent.objWriter.writeObject(new PlayData(24, data.getStone(), data.getDestination(), player_name));
                            } else {
                                this.objWriter.writeObject(new PlayData(20, data.getStone(), data.getDestination(), player_name));
                                opponent.objWriter.writeObject(new PlayData(22, data.getStone(), data.getDestination(), player_name));
                            }
                        } else {
                            this.objWriter.writeObject(new PlayData(21, data.getStone(), data.getDestination(), player_name, 3));
                        }
                        break;
                    case 30:
                        if (logic.free_position(data.getDestination()) && !data.getStone().equal(data.getDestination()) && logic.player_stone(data.getStone(), player_name)) {
                            logic.jump_stone(data.getStone(), data.getDestination(), player_name);
                            if (data.getReason() == 30) {
                                if (logic.muehle(player_name)) {
                                    old_case = 30;
                                    this.objWriter.writeObject(new PlayData(23, data.getStone(), data.getDestination(), player_name));
                                    opponent.objWriter.writeObject(new PlayData(24, data.getStone(), data.getDestination(), player_name));
                                } else {
                                    this.objWriter.writeObject(new PlayData(30, data.getStone(), data.getDestination(), player_name, 30));
                                    opponent.objWriter.writeObject(new PlayData(32, data.getStone(), data.getDestination(), player_name, 30));
                                }
                            } else if (data.getReason() == 20) {
                                if (logic.muehle(player_name)) { // if this player wins the other player doesn't have enough stones to play
                                    this.objWriter.writeObject(new PlayData(50, data.getStone(), data.getDestination(), player_name));
                                    opponent.objWriter.writeObject(new PlayData(50, data.getStone(), data.getDestination(), player_name));
                                } else {
                                    this.objWriter.writeObject(new PlayData(30, data.getStone(), data.getDestination(), player_name, 20));
                                    opponent.objWriter.writeObject(new PlayData(32, data.getStone(), data.getDestination(), player_name, 20));
                                }
                            } else {
                                System.err.println("Wrong reason! " + data.getReason());
                            }
                        } else {
                            if (data.getReason() == 20) {
                                this.objWriter.writeObject(new PlayData(31, data.getStone(), data.getDestination(), player_name, 3));
                            } else if (data.getReason() == 30) {
                                this.objWriter.writeObject(new PlayData(31, data.getStone(), data.getDestination(), player_name, 4));
                            }
                        }
                        break;
                    case 40:

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
