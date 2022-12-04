package Clientside;

import Data.Stone;
import Logic.LogicDealer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

public class ClientBoard extends JFrame implements MouseMotionListener, MouseListener {

    private static final Stone[][][] board = new Stone[3][3][3];

    private final String player_name;
    private final BoardPanel panel = new BoardPanel();
    private final Client client;
    int radius = panel.getWidth() / 14;
    int circleDiameter = 50;
    int circleRadius = circleDiameter / 2;
    private String opponent_name;
    private int phase = -1;
    private boolean this_player_move = false;


    public ClientBoard(String player) throws HeadlessException {
        this.player_name = player;

        this.client = new Client(player);
        panel.setPlayer_one_name(player_name);
        this.setTitle(this.player_name);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setSize(1080, 720);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(panel);
        this.setVisible(true);
        init();
    }

    private void init() {
        current_state(-1);
        new WaitSwingWorker(this.client, this.player_name, this.panel, this).execute();
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (phase == 1 && this_player_move) {
            Stone stone = get_stone_position(e.getX(), e.getY());
            if (stone != null) {
                client.sendData(stone, "mouse_pressed");
                new SendSwingWorker(this.client, this.player_name, this.panel, this).execute();
                this_player_move = false;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void insert_board(Stone stone) {
        board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = stone;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    private Stone get_stone_position(int position_x, int position_y) {
        Stone stone = null;
        if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [1]
            stone = new Stone(this.player_name, 0, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [2]
            stone = new Stone(this.player_name, 0, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [3]
            stone = new Stone(this.player_name, 0, 0, 2);
        }
        // pos 10 & 15
        else if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 60) {   //point [10]
            stone = new Stone(this.player_name, 0, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 60) {   //point [15]
            stone = new Stone(this.player_name, 0, 1, 2);
        }
        //pos 22 23 24
        else if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [22]
            stone = new Stone(this.player_name, 0, 2, 0);


        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 1 + radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + 1 + radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [23]
            stone = new Stone(this.player_name, 0, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [24]
            stone = new Stone(this.player_name, 0, 2, 2);
        }
        // middle rect
        //pos 4 5 6
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [4]
            stone = new Stone(this.player_name, 1, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [5]
            stone = new Stone(this.player_name, 1, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [6]
            stone = new Stone(this.player_name, 1, 0, 2);
        }
        // pos 11 14
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 60) {   //point [11]
            stone = new Stone(this.player_name, 1, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 60) {   //point [14]
            stone = new Stone(this.player_name, 1, 1, 2);
        }
        //pos 19 20 21
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [19]
            stone = new Stone(this.player_name, 1, 2, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + 1 + radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [20]
            stone = new Stone(this.player_name, 1, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [21]
            stone = new Stone(this.player_name, 1, 2, 2);
        }
        //small rect
        //pos 7 8 9
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [4]
            stone = new Stone(this.player_name, 2, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [5]
            stone = new Stone(this.player_name, 2, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [6]
            stone = new Stone(this.player_name, 2, 0, 2);
        }
        // pos 12 13
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + radius - circleRadius + 60) {   //point [12]
            stone = new Stone(this.player_name, 2, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + radius - circleRadius + 60) {   //point [13]
            stone = new Stone(this.player_name, 2, 1, 2);
        }
        //pos 16 17 18
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [16]
            stone = new Stone(this.player_name, 2, 2, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 1 + radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + 1 + radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [17]
            stone = new Stone(this.player_name, 2, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [18]
            stone = new Stone(this.player_name, 2, 2, 2);
        }
        assert stone != null;
        return stone;
    }

    public void setOpponent(String player) {
        opponent_name = player;
        panel.setPlayer_two_name(player);
    }

    public void setThis_player_move(boolean this_player_move) {
        this.this_player_move = this_player_move;
    }

    public void setPlayer_Position(boolean position) {
        if (!position) {
            new WaitSwingWorker(this.client, this.player_name, this.panel, this).execute();
            current_state(11);
        } else {
            current_state(10);
            this_player_move = true;
        }
    }

    protected void current_state(int state) {
        switch (state) {
            case -1:
                panel.setCurrent_state("Wait for Server answer!");
                break;
            case 10:
                panel.setCurrent_state(player_name + " you can move!");
                break;
            case 11:
                panel.setCurrent_state(opponent_name + " is on his move!");
                break;
            default:
                panel.setCurrent_state("Something went wrong!");
                break;
        }
    }

    private void print_board() {
        System.out.println("Printboard: ");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    System.out.println(board[i][j][k]);
                }
            }
        }
        System.out.println("end Print");
    }
}
