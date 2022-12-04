package Clientside;

import javax.swing.*;

import Data.*;

public class SendSwingWorker extends SwingWorker<Boolean, String> {

    private final Client client;
    private final String player;
    private final BoardPanel panel;
    private final ClientBoard clientBoard;
    private PlayData serverdata;
    private int temp = -1;


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
            temp = client.wait_for_allowed();
            if (temp == 0 || temp == 1) {
                serverdata = client.getServerdata();
                System.out.println("jude");
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
        if (temp == 1) {
            switch (serverdata.getState()) {
                case 10:
                    panel.placeStone(client.getSendata().getStone());
                    clientBoard.insert_board(client.getSendata().getStone());
                    clientBoard.setPhase(1);
                    clientBoard.current_state(11);
                    break;
                default:
                    System.out.println("[SEND] Smth went wrong!!");
                    break;
            }
            new WaitSwingWorker(this.client, this.player, this.panel, this.clientBoard).execute();
        }
    }
}
