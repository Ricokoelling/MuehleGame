package Clientside;

import Data.PlayData;

import javax.swing.*;

public class WaitSwingWorker extends SwingWorker<Boolean, String> {

    private final Client client;
    private final String player;
    private final BoardPanel panel;
    private final ClientBoard clientBoard;
    private PlayData serverdata;

    /**
     * Constructor for the thread which waits for server data from the opponent move
     */
    public WaitSwingWorker(Client client, String player, BoardPanel panel, ClientBoard clientBoard) {
        this.client = client;
        this.player = player;
        this.panel = panel;
        this.clientBoard = clientBoard;
    }

    @Override
    protected Boolean doInBackground() throws Exception {

        System.out.println("[WAITWORKER] Wait for op data....");
        while (true) {
            Thread.sleep(20);
            if (client.wait_for_data()) {
                serverdata = client.getServerdata();
                break;
            }
        }
        return null;
    }

    @Override
    protected void done() {
        System.out.println("Got op Data...");
        switch (serverdata.getState()) {
            case -1 -> {
                clientBoard.setOpponent(serverdata.getPlayer());
                clientBoard.setPlayer_Position(serverdata.isInit());
                clientBoard.setPhase(1);
            }
            case 12 -> {
                clientBoard.insert_board(serverdata.getStone());
                panel.placeStone(serverdata.getStone());
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(10);
            }
            case 91 -> {
                clientBoard.insert_board(serverdata.getStone());
                panel.placeStone(serverdata.getStone());
                clientBoard.setThis_player_move(false);
                clientBoard.current_state(91);
                try {
                    Thread.sleep(100);
                    clientBoard.current_state(1);
                    new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case 2 -> {
                clientBoard.remove_board(serverdata.getStone());
                panel.remove(serverdata.getStone());
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(10);
            }
            case 22 -> {
                clientBoard.move_board(serverdata.getStone(), serverdata.getDestination());
                panel.move_stone(serverdata.getStone(), serverdata.getDestination());
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(22);
            }
            case 28 -> {
                if (serverdata.getReason() == 1) {
                    clientBoard.insert_board(serverdata.getStone());
                    panel.placeStone(serverdata.getStone());
                } else if (serverdata.getReason() == 0) {
                    clientBoard.remove_board(serverdata.getStone());
                    panel.remove(serverdata.getStone());
                } else {
                    System.err.println("Wrong Reason");
                }
                clientBoard.current_state(22);
                clientBoard.setPhase(2);
                clientBoard.setThis_player_move(true);
            }
            case 24 -> {
                clientBoard.move_board(serverdata.getStone(), serverdata.getDestination());
                panel.move_stone(serverdata.getStone(), serverdata.getDestination());
                clientBoard.setThis_player_move(false);
                clientBoard.current_state(91);
                try {
                    Thread.sleep(100);
                    clientBoard.current_state(1);
                    new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case 26 -> {
                clientBoard.remove_board(serverdata.getStone());
                panel.remove(serverdata.getStone());
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(22);
            }
            case 38 -> {
                clientBoard.remove_board(serverdata.getStone());
                panel.remove(serverdata.getStone());
                clientBoard.setPhase(3);
                clientBoard.setThis_player_move(true);
                clientBoard.setPhase3_player(serverdata.getReason());
                if (serverdata.getReason() == 20) {
                    clientBoard.current_state(22);
                } else {
                    clientBoard.current_state(30);
                }
            }
            case 32 -> {
                clientBoard.move_board(serverdata.getStone(), serverdata.getDestination());
                panel.move_stone(serverdata.getStone(), serverdata.getDestination());
                clientBoard.setThis_player_move(true);
                if (serverdata.getReason() == 20) {
                    clientBoard.current_state(30);
                } else {
                    clientBoard.current_state(22);
                }
            }
            case 48 -> {
                clientBoard.remove_board(serverdata.getStone());
                panel.remove(serverdata.getStone());
                clientBoard.setPhase(4);
                clientBoard.current_state(30);
                clientBoard.setThis_player_move(true);
            }
            case 42 -> {
                clientBoard.move_board(serverdata.getStone(), serverdata.getDestination());
                panel.move_stone(serverdata.getStone(), serverdata.getDestination());
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(30);
            }
            case 50 -> {
                clientBoard.setPhase(5);
                clientBoard.current_state(52);
                clientBoard.move_board(serverdata.getStone(), serverdata.getDestination());
                panel.move_stone(serverdata.getStone(), serverdata.getDestination());
            }
            default -> System.err.println("[WAIT] something went wrong!");
        }
        super.done();

    }
}
