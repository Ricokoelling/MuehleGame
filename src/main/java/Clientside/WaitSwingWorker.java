package Clientside;

import Data.PlayData;

import javax.swing.*;

public class WaitSwingWorker extends SwingWorker<Boolean, String> {

    private final Client client;
    private final String player;
    private final BoardPanel panel;
    private final ClientBoard clientBoard;
    private PlayData serverdata;


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
            case -1:
                clientBoard.setOpponent(serverdata.getPlayer());
                clientBoard.setPlayer_Position(serverdata.isInit());
                clientBoard.setPhase(1);
                break;
            case 12:
                clientBoard.insert_board(serverdata.getStone());
                panel.placeStone(serverdata.getStone());
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(10);
                break;
            case 91:
                clientBoard.insert_board(serverdata.getStone());
                panel.placeStone(serverdata.getStone());
                clientBoard.setThis_player_move(false);
                clientBoard.current_state(91);
                try {
                    Thread.sleep(1000);
                    clientBoard.current_state(1);
                    new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                clientBoard.remove_board(serverdata.getStone());
                panel.remove(serverdata.getStone());
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(10);
                break;
            case 22:
                clientBoard.move_board(serverdata.getStone(), serverdata.getDestination());
                panel.move_stone(serverdata.getStone(), serverdata.getDestination());
                clientBoard.setThis_player_move(true);
                clientBoard.current_state(22);
                break;
            default:
                System.err.println("[WAIT] smth went wrong!");
                break;
        }
        super.done();

    }
}
