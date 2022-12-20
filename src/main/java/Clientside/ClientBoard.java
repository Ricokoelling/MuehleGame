package Clientside;

import Data.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
    private Stone mouse_pressed = null;
    private boolean released_mouse_btn_phase_2 = true;
    private boolean this_player_phase_3 = false;


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
        Stone stone = get_stone_position(e.getX(), e.getY());
        if (stone != null && mouse_pressed != null) {
            if ((phase == 2 || (phase == 3 && !this_player_phase_3)) && !stone.equals(mouse_pressed) && !released_mouse_btn_phase_2 && this_player_move) {
                if (phase == 2) {
                    client.sendData(mouse_pressed, stone, "mouse_dragged");
                } else if (phase == 3) {
                    client.sendData(mouse_pressed, stone, "2_mouse_released");
                }
                new SendSwingWorker(this.client, this.player_name, this.panel, this).execute();
                this_player_move = false;
                released_mouse_btn_phase_2 = true;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this_player_move) {
            mouse_pressed = get_stone_position(e.getX(), e.getY());
            if (phase >= 2) {
                released_mouse_btn_phase_2 = false;
            }
            if (mouse_pressed != null && phase < 2) {
                if (phase == 1) {
                    client.sendData(mouse_pressed, "mouse_pressed");
                } else if (phase == 0) {
                    client.sendData(mouse_pressed, "remove_mouse_pressed");
                }
                new SendSwingWorker(this.client, this.player_name, this.panel, this).execute();
                this_player_move = false;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this_player_move) {
            if (phase == 2 && mouse_pressed != null) {
                released_mouse_btn_phase_2 = false;
            }
            if (mouse_pressed != null && ((phase == 3 && this_player_phase_3) || phase == 4)) {
                Stone stone = get_stone_position(e.getX(), e.getY());
                if (phase == 3) {
                    client.sendData(mouse_pressed, stone, "3_mouse_released");
                } else if (phase == 4) {
                    client.sendData(mouse_pressed, stone, "4_mouse_released");
                }
                new SendSwingWorker(this.client, this.player_name, this.panel, this).execute();
                this_player_move = false;
            }
        }
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

    public void remove_board(Stone stone) {
        board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = null;
    }

    public void move_board(Stone start, Stone destination) {
        board[start.getPosOne()][start.getPosTwo()][start.getPosThree()] = null;
        board[destination.getPosOne()][destination.getPosTwo()][destination.getPosThree()] = destination;
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
        client.setOpponent(opponent_name);
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

    public void setPhase3_player(int x) {
        if (x == 30) {
            this_player_phase_3 = true;
        }
    }

    protected void current_state(int state) {
        switch (state) {
            case -1:
                panel.setCurrent_state("Wait for Server answer!");
                break;
            case 10:
                panel.setCurrent_state(player_name + " you can place!");
                break;
            case 11:
                panel.setCurrent_state(opponent_name + " places a stone!");
                break;
            case 12:
                panel.setCurrent_state(player_name + " not a free positon!");
                break;
            case 90:
                panel.setCurrent_state(player_name + " got a mill!");
                break;
            case 91:
                panel.setCurrent_state(opponent_name + " got a mill!");
                break;
            case 0:
                panel.setCurrent_state(player_name + " removes a Stone!");
                break;
            case 1:
                panel.setCurrent_state(opponent_name + " removes a stone!");
                break;
            case 2:
                panel.setCurrent_state(player_name + " ");
                break;
            case 3:
                panel.setCurrent_state(player_name + " can't remove stone");
                break;
            case 29:
                panel.setCurrent_state(opponent_name + " moves a stone!");
                break;
            case 22:
                panel.setCurrent_state(player_name + " do your move!");
                break;
            case 20:
                panel.setCurrent_state(opponent_name + "  moves a stone!");
                break;
            case 21:
                panel.setCurrent_state(player_name + " move not possible!");
                break;
            case 30:
                panel.setCurrent_state(player_name + " jump!");
                break;
            case 32:
                panel.setCurrent_state(opponent_name + " jumps!");
                break;
            case 31:
                panel.setCurrent_state(player_name + " jump was not possible!");
                break;
            case 50:
                panel.setCurrent_state("shit man someone won");
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
