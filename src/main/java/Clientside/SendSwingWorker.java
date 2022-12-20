package Clientside;

import javax.swing.*;

import Data.*;

public class SendSwingWorker extends SwingWorker<Boolean, String> {

    private final Client client;
    private final String player;
    private final BoardPanel panel;
    private final ClientBoard clientBoard;
    private PlayData serverdata;


    public SendSwingWorker(Client client, String player, BoardPanel panel, ClientBoard clientBoard) {
        this.client = client;
        this.player = player;
        this.panel = panel;
        this.clientBoard = clientBoard;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        System.out.println("[WORKER] Wait for allowed....");
        while (true) {
            Thread.sleep(20);
            int temp = client.wait_for_allowed();
            if (temp != -1) {
                serverdata = client.getServerdata();
                if (temp == 1) {
                    System.out.println("[WORKER] Data was allowed.. " + serverdata.getState());
                } else {
                    System.out.println("[WORKER] Data wasn't allowed");
                }
                break;
            }
        }

        return null;
    }

    @Override
    protected void done() {
        System.out.println("state: " + serverdata.getState());
        switch (serverdata.getState()) {
            case 10:
                panel.placeStone(client.getSendata().getStone());
                clientBoard.insert_board(client.getSendata().getStone());
                clientBoard.setPhase(1);
                clientBoard.current_state(11);
                new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                break;
            case 11:
                if (serverdata.getReason() == 1) {
                    clientBoard.current_state(12);
                    clientBoard.setThis_player_move(true);
                } else {
                    System.err.println("[SEND] Wrong Reason");
                }
                break;
            case 90:
                panel.placeStone(client.getSendata().getStone());
                clientBoard.insert_board(client.getSendata().getStone());
                clientBoard.setPhase(0);
                clientBoard.current_state(90);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(0);
                break;
            case 0:
                panel.remove(serverdata.getStone());
                clientBoard.remove_board(serverdata.getStone());
                clientBoard.setPhase(1);
                clientBoard.current_state(11);
                new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                break;
            case 1:
                if (serverdata.getReason() == 2) {
                    clientBoard.current_state(3);
                    clientBoard.setThis_player_move(true);
                } else {
                    System.err.println("[SEND] Wrong Reason");
                }
                break;
            case 29:
                if (serverdata.getReason() == 1) {
                    clientBoard.insert_board(serverdata.getStone());
                    panel.placeStone(serverdata.getStone());
                } else if (serverdata.getReason() == 0) {
                    clientBoard.remove_board(serverdata.getStone());
                    panel.remove(serverdata.getStone());
                } else {
                    System.err.println("Wrong Reason");
                }
                clientBoard.setPhase(2);
                clientBoard.current_state(29);
                new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                break;
            case 20:
                panel.move_stone(client.getSendata().getStone(), client.getSendata().getDestination());
                clientBoard.move_board(client.getSendata().getStone(), client.getServerdata().getDestination());
                clientBoard.current_state(20);
                new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                break;
            case 23:
                clientBoard.move_board(client.getSendata().getStone(), client.getServerdata().getDestination());
                panel.move_stone(client.getSendata().getStone(), client.getServerdata().getDestination());
                clientBoard.setPhase(0);
                clientBoard.current_state(90);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(0);
                break;
            case 21:
                if (serverdata.getReason() == 3) {
                    clientBoard.current_state(21);
                    clientBoard.setThis_player_move(true);
                } else {
                    System.err.println("[SEND] Wrong Reason!");
                }
                break;
            case 25:
                panel.remove(serverdata.getStone());
                clientBoard.remove_board(serverdata.getStone());
                clientBoard.setPhase(2);
                clientBoard.current_state(20);
                new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                break;
            case 39:
                panel.remove(serverdata.getStone());
                clientBoard.remove_board(serverdata.getStone());
                clientBoard.setPhase(3);
                clientBoard.setPhase3_player(serverdata.getReason());
                if (serverdata.getReason() == 20) {
                    clientBoard.current_state(32);
                } else {
                    clientBoard.current_state(20);
                }
                new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                break;
            case 30:
                panel.move_stone(serverdata.getStone(), serverdata.getDestination());
                clientBoard.move_board(serverdata.getStone(), serverdata.getDestination());
                if (serverdata.getReason() == 30) {
                    clientBoard.current_state(20);
                } else {
                    clientBoard.current_state(32);
                }
                new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                break;
            case 31:
                if (serverdata.getReason() == 3) {
                    clientBoard.current_state(21);
                    clientBoard.setThis_player_move(true);
                } else if (serverdata.getReason() == 4) {
                    clientBoard.current_state(31);
                    clientBoard.setThis_player_move(true);
                } else {
                    System.err.println("[SEND] Wrong Reason!");
                }
                break;
            case 35:
                clientBoard.setPhase(3);
                clientBoard.remove_board(serverdata.getStone());
                panel.remove(serverdata.getStone());
                clientBoard.current_state(32);
                new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                break;
            case 50:
                clientBoard.current_state(50);
                break;
            default:
                System.err.println("[SEND] Something went wrong!!");
                break;
        }
    }
}
