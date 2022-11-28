package GUI;

import Logic.LogicDealer;
import Logic.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.SQLOutput;
import java.util.Objects;

public class BoardFrame extends JFrame implements MouseMotionListener, MouseListener {

    private static final Stone[][][] board = new Stone[3][3][3];

    private final String playerOne_name;
    private final String playerTwo_name;
    private final BoardPanel panel = new BoardPanel();
    private final LogicDealer logic;
    private final int MAX_STONES = 18;
    int radius = panel.getWidth() / 14;
    int circleDiameter = 50;
    int circleRadius = circleDiameter / 2;
    private String player;
    private String opposite_player;
    private String phase_3_player;
    private int phase = -1;
    private int player_one_stones = 0;
    private int player_two_stones = 0;
    private int maxStones;
    private Stone stone_pressed = null;
    //checking variables
    private boolean phase_0_wrong_player_check = false;

    private boolean released_mouse_btn_phase_2 = true;
    private int phase_before = -1;


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
        this.setResizable(false);
        this.add(panel);
        this.setVisible(true);
    }

    private void newGame() {
        this.phase = 1;
        this.phase_before = 1;
        maxStones = MAX_STONES;
        setPlayer();
    }

    private void setPlayer() {
        if (player == null) {
            player = playerOne_name;
            opposite_player = playerTwo_name;
        } else if (player.equals(playerOne_name)) {
            if (phase == 1 || phase_before == 1) {
                player_one_stones++;
            }
            if (phase != 0) {
                player = playerTwo_name;
                opposite_player = playerOne_name;
            }
        } else if (player.equals(playerTwo_name)) {
            if (phase == 1 || phase_before == 1) {
                player_two_stones++;
            }
            if (phase != 0) {
                player = playerOne_name;
                opposite_player = playerTwo_name;
            }
        } else {
            System.err.println("[Board] Error at Player!");
        }
        if (phase < 2 && player_two_stones + player_one_stones == maxStones) {
            if (player_two_stones == 3) {
                phase = 3;
                phase_3_player = playerTwo_name;
            } else if (player_one_stones == 3) {
                phase_3_player = playerOne_name;
            } else if (player_one_stones > 3 || player_two_stones > 3) {
                phase = 2;
            } else {
                System.err.println("[BOARD] Wrong Stone count!");
            }
            System.out.println("[BOARD] phase changed: phase " + phase + " player moves: " + player);
        }
        System.out.println("setPlayer: " + player + " phase: " + phase);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (phase == 2 || (phase == 3 && !Objects.equals(phase_3_player, player))) {
            Stone stone = get_stone_position(e.getX(), e.getY());
            if (stone != null && stone_pressed != null) {
                System.out.println("yeeeeet");
                System.out.println(!stone.equal(stone_pressed) + " " + Objects.equals(stone_pressed.getPlayer(), player) + " " + logic.move_possible(stone_pressed, stone, player) + " " + !released_mouse_btn_phase_2);
                if (!stone.equal(stone_pressed) && Objects.equals(stone_pressed.getPlayer(), player) && logic.move_possible(stone_pressed, stone, player) && !released_mouse_btn_phase_2) {
                    System.out.println("[LOGIC] " + player + " moves a stone ");
                    released_mouse_btn_phase_2 = true;
                    board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] = null;
                    board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = stone;
                    panel.move_stone(stone_pressed, stone);
                    logic.move_stone(stone_pressed, stone, player);
                    if (logic.muehle(player)) {
                        System.out.println("[BOARD] Player " + player + " removes a stone. phase: " + phase);
                        if (phase == 2) {
                            phase_before = 2;
                        }
                        if (phase == 3) {
                            phase = 5;
                            System.out.println("Player" + player + "won the Game!");
                        } else {
                            phase = 0;
                        }
                    }
                    setPlayer();
                    System.out.println("plone: " + player_one_stones + " pltwo: " + player_two_stones);
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
        if (phase == 0 && !phase_0_wrong_player_check && stone_pressed != null) {
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
                        player_one_stones--;
                    } else if (Objects.equals(player, playerTwo_name)) {
                        player_two_stones--;
                    }
                    System.out.println("remove: plone: " + player_one_stones + " pltwo: " + player_two_stones);
                    panel.remove(stone_pressed);
                    board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] = null;
                    if (phase_before == 1) {
                        phase = 1;
                    } else if (phase_before == 2) {
                        phase = 2;
                        phase_3_player = logic.check_for_phase_3();
                        if (phase_3_player != null) {
                            phase = 3;
                        }
                    } else if (phase_before == 3) {
                        phase = 3;
                        if (Objects.equals(playerOne_name, phase_3_player)) {
                            if (player_one_stones == 3) {
                                phase = 4;
                            }
                        } else if (Objects.equals(playerTwo_name, phase_3_player)) {
                            if (player_two_stones == 3) {
                                phase = 4;
                            }
                        } else {
                            System.err.println("[BOARD] Something went wrong!");
                        }
                    } else {
                        System.err.println("[BOARD] Wrong Phase");
                    }
                    phase_0_wrong_player_check = false;
                    //System.out.println("[BOARD] Player: " + opposite_player + " removed a stone: [" + stone_pressed.getPosOne() + "] [" + stone_pressed.getPosTwo() + "] [" + stone_pressed.getPosThree() + "]");
                } else {
                    phase_0_wrong_player_check = true;
                    System.out.println("[BOARD] Wrong Stone!");
                }
            } else if (phase == 1) {
                if (board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] == null) {
                    logic.placeStone(stone_pressed);
                    board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] = stone_pressed;
                    panel.placeStone(stone_pressed);
                    //System.out.println(player_two_stones);
                    //System.out.println("[BOARD] Player: " + player + " placed a stone: [" + stone_pressed.getPosOne() + "] [" + stone_pressed.getPosTwo() + "] [" + stone_pressed.getPosThree() + "]");
                    if (player_two_stones >= 2) {
                        if (logic.muehle(player)) {
                            System.out.println("[BOARD] Player " + player + " removes a stone. phase: " + phase);
                            phase_before = 1;
                            phase = 0;
                        }
                        setPlayer();
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
        if (phase >= 2 && stone_pressed != null) {
            released_mouse_btn_phase_2 = false;
        }
        if ((phase == 3 && Objects.equals(phase_3_player, player)) || phase == 4) {
            Stone stone = get_stone_position(e.getX(), e.getY());
            assert stone != null;
            assert stone_pressed != null;
            if (!stone_pressed.equal(stone) && Objects.equals(stone.getPlayer(), player) && logic.free_position(stone)) {
                board[stone_pressed.getPosOne()][stone_pressed.getPosTwo()][stone_pressed.getPosThree()] = null;
                board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = stone;
                panel.move_stone(stone_pressed, stone);
                logic.jump_stone(stone_pressed, stone, player);
                if (logic.muehle(player)) {
                    phase = 0;
                    phase_before = 3;
                    System.out.println("[BOARD] Player " + player + " removes a stone. phase: " + phase);
                }
                setPlayer();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private Stone get_stone_position(int position_x, int position_y) {
        Stone stone = null;
        if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [1]
            stone = new Stone(this.player, 0, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [2]
            stone = new Stone(this.player, 0, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [3]
            stone = new Stone(this.player, 0, 0, 2);
        }
        // pos 10 & 15
        else if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 60) {   //point [10]
            stone = new Stone(this.player, 0, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 60) {   //point [15]
            stone = new Stone(this.player, 0, 1, 2);
        }
        //pos 22 23 24
        else if (position_x > ((this.getWidth() / 2) - radius * 3) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [22]
            stone = new Stone(this.player, 0, 2, 0);


        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 1 + radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + 1 + radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [23]
            stone = new Stone(this.player, 0, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [24]
            stone = new Stone(this.player, 0, 2, 2);
        }
        // middle rect
        //pos 4 5 6
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [4]
            stone = new Stone(this.player, 1, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [5]
            stone = new Stone(this.player, 1, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [6]
            stone = new Stone(this.player, 1, 0, 2);
        }
        // pos 11 14
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 60) {   //point [11]
            stone = new Stone(this.player, 1, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 60) {   //point [14]
            stone = new Stone(this.player, 1, 1, 2);
        }
        //pos 19 20 21
        else if (position_x > ((this.getWidth() / 2) - radius * 2) - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [19]
            stone = new Stone(this.player, 1, 2, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + 1 + radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [20]
            stone = new Stone(this.player, 1, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius && position_x < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [21]
            stone = new Stone(this.player, 1, 2, 2);
        }
        //small rect
        //pos 7 8 9
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [4]
            stone = new Stone(this.player, 2, 0, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [5]
            stone = new Stone(this.player, 2, 0, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [6]
            stone = new Stone(this.player, 2, 0, 2);
        }
        // pos 12 13
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + radius - circleRadius + 60) {   //point [12]
            stone = new Stone(this.player, 2, 1, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + radius - circleRadius + 60) {   //point [13]
            stone = new Stone(this.player, 2, 1, 2);
        }
        //pos 16 17 18
        else if (position_x > ((this.getWidth() / 2) - radius) - circleRadius && position_x < ((this.getWidth() / 2) - radius) - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [16]
            stone = new Stone(this.player, 2, 2, 0);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 1 + radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + 1 + radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [17]
            stone = new Stone(this.player, 2, 2, 1);
        } else if (position_x > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius && position_x < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 50 && position_y > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && position_y < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [18]
            stone = new Stone(this.player, 2, 2, 2);
        }
        return stone;
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
