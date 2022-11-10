package GUI;

import Logic.LogicDealer;
import Logic.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

public class BoardFrame extends JFrame implements MouseMotionListener, MouseListener {

    private static final Stone[][][] board = new Stone[3][3][3];

    private final String playerOne_name;
    private final String playerTwo_name;
    private final BoardPanel panel = new BoardPanel();
    private final LogicDealer logic;
    private final int MAX_STONES = 18;
    int radius = panel.getWidth() / 14;
    int circleDiameter = 30;
    int circleRadius = circleDiameter / 2;
    private String player;
    private String opposite_player;
    private int phase = -1;
    private int player_one_stones = 0;
    private int player_two_stones = 0;
    private int maxStones;
    private Stone stone_pressed = null;
    //checking variables
    private boolean phase_0_wrong_player_check = false;


    public BoardFrame(String playerOne, String playerTwo) throws HeadlessException {
        this.playerOne_name = playerOne;
        this.playerTwo_name = playerTwo;

        logic = new LogicDealer(playerOne, playerTwo);
        newGame();
        panel.setPlayer_one_name(playerOne_name);
        panel.setPlayer_two_name(playerTwo_name);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setSize(1080, 720);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(panel);
        this.setVisible(true);
    }

    private void newGame() {
        this.phase = 1;
        maxStones = MAX_STONES;
        setPlayer();
    }

    private void setPlayer() {
        if (player == null) {
            player = playerOne_name;
            opposite_player = playerTwo_name;
        } else if (player.equalsIgnoreCase(playerOne_name)) {
            player_one_stones++;
            player = playerTwo_name;
            opposite_player = playerOne_name;
        } else if (player.equalsIgnoreCase(playerTwo_name)) {
            player_two_stones++;
            player = playerOne_name;
            opposite_player = playerTwo_name;
        } else {
            System.err.println("[Board] Error at Player!");
        }
        if (phase < 2 && player_two_stones + player_one_stones == maxStones) {
            phase = 2;
            System.out.println("[BOARD] phase changed: phase " + phase);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (phase == 2) {
            Stone stone = get_stone_position(e.getX(), e.getY());
            if (stone != null) {
                if (!stone.equal(stone_pressed) && logic.move_possible(stone_pressed, stone, player)) {
                    board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] = null;
                    board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = stone;
                    setPlayer();
                }
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
        if (phase == 0 && !phase_0_wrong_player_check) {
            String temp = player;
            player = opposite_player;
            opposite_player = temp;
        }
        stone_pressed = get_stone_position(e.getX(), e.getY());
        if (stone_pressed != null) {
            if (phase == 0) {
                if (logic.remove(stone_pressed, player)) {
                    maxStones--;
                    if (Objects.equals(player, playerOne_name)) {
                        player_two_stones--;
                    } else if (Objects.equals(player, playerTwo_name)) {
                        player_one_stones--;
                    }
                    panel.remove(stone_pressed);
                    board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] = null;
                    phase = 1;
                    phase_0_wrong_player_check = false;
                    System.out.println("[BOARD] Player: " + player + " removed a stone: [" + stone_pressed.getPosOne() + "] [" + stone_pressed.getPosTwo() + "] [" + stone_pressed.getPosThree() + "]");
                } else {
                    phase_0_wrong_player_check = true;
                    System.out.println("[BOARD] Wrong Stone!");
                }
            } else {
                if (board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] == null) {
                    logic.placeStone(stone_pressed);
                    board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] = stone_pressed;
                    panel.placeStone(stone_pressed);
                    if (player_two_stones >= 2) {
                        if (logic.muehle(player)) {
                            System.out.println("[BOARD] Player " + player + " removes a stone.");
                            phase = 0;
                        } else {
                            setPlayer();
                        }
                    } else {
                        setPlayer();
                    }
                } else {
                    System.out.println("[BOARD] There is already a stone " + stone_pressed);
                }
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

    private Stone get_stone_position(int position_x, int position_y) {
        Stone stone = null;
        if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [1]
            stone = new Stone(this.player, 0, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + radius * 3 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 3) + radius * 3 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [2]
            stone = new Stone(this.player, 0, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [3]
            stone = new Stone(this.player, 0, 0, 2);
        }
        // pos 10 & 15
        else if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 60) {   //point [10]
            stone = new Stone(this.player, 0, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 60) {   //point [15]
            stone = new Stone(this.player, 0, 1, 2);
        }
        //pos 22 23 24
        else if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 10 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [22]
            stone = new Stone(this.player, 0, 2, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 1 + radius * 3 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 3) + 1 + radius * 3 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [23]
            stone = new Stone(this.player, 0, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [24]
            stone = new Stone(this.player, 0, 2, 2);
        }
        // middle rect
        //pos 4 5 6
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [4]
            stone = new Stone(this.player, 1, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [5]
            stone = new Stone(this.player, 1, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [6]
            stone = new Stone(this.player, 1, 0, 2);
        }
        // pos 11 14
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 60) {   //point [11]
            stone = new Stone(this.player, 1, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 60) {   //point [14]
            stone = new Stone(this.player, 1, 1, 2);
        }
        //pos 19 20 21
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [19]
            stone = new Stone(this.player, 1, 2, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 2) + 1 + radius * 2 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [20]
            stone = new Stone(this.player, 1, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [21]
            stone = new Stone(this.player, 1, 2, 2);
        }
        //small rect
        //pos 7 8 9
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [4]
            stone = new Stone(this.player, 2, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + radius - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius) + radius - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [5]
            stone = new Stone(this.player, 2, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [6]
            stone = new Stone(this.player, 2, 0, 2);
        }
        // pos 12 13
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius) + radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + radius - circleRadius + 60) {   //point [12]
            stone = new Stone(this.player, 2, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius) + radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + radius - circleRadius + 60) {   //point [13]
            stone = new Stone(this.player, 2, 1, 2);
        }
        //pos 16 17 18
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [16]
            stone = new Stone(this.player, 2, 2, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 1 + radius - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius) + 1 + radius - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [17]
            stone = new Stone(this.player, 2, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius - 10 && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 40 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [18]
            stone = new Stone(this.player, 2, 2, 2);
        }
        return stone;
    }
}
