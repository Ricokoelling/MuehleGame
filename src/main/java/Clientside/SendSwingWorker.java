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
            if (client.wait_for_allowed() == 0 || client.wait_for_allowed() == 1) {
                serverdata = client.getServerdata();
                if (client.wait_for_allowed() == 1) {
                    System.out.println("[WORKER] Data was allowed.. ");
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
        if (client.wait_for_allowed() == 1) {
            if (client.getSendata().getState() == 10) {
                panel.placeStone(client.getSendata().getStone());
                clientBoard.insert_board(client.getSendata().getStone());
                clientBoard.setPhase(1);
            }
        }
    }
}
