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
            System.out.println(temp);
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
                    Thread.sleep(1000);
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
                if (serverdata.getState() == 2) {
                    clientBoard.current_state(1);
                    clientBoard.setThis_player_move(true);
                } else {
                    System.err.println("[SEND] Wrong Reason");
                }
                break;
            case 29:
                panel.placeStone(client.getSendata().getStone());
                clientBoard.insert_board(client.getSendata().getStone());
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
            default:
                System.out.println("[SEND] Smth went wrong!!");
                break;
        }
    }
}
