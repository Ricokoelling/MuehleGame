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
        if (serverdata.getState() == 12) {
            clientBoard.insert_board(serverdata.getStone());
            panel.placeStone(serverdata.getStone());
        }
    }
}
