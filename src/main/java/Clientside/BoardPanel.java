package Clientside;

import Data.Stone;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class BoardPanel extends JPanel {

    protected static Label current_state;
    private final Color playerone_stone_color = Color.BLACK;
    private final Color playertwo_stone_color = Color.CYAN;
    private final Stone[][][] board = new Stone[3][3][3];
    private String player_one_name;
    private String player_two_name;

    /**
     * constructor
     */
    public BoardPanel() {
        this.setSize(1080, 720);
        this.setVisible(true);
        new GridBagLayout();
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.NONE;
        current_state = new Label("Wait for other Player \t", SwingConstants.CENTER);
        current_state.setFont(new Font("Arial", Font.PLAIN, 37));
        current_state.setBackground(getWoodenColor());
        current_state.setMinimumSize(new Dimension(800, 65));
        current_state.setPreferredSize(new Dimension(850, 70));
        current_state.setMaximumSize(new Dimension(900, 75));
        current_state.setAlignment(Label.CENTER);
        add(current_state, g);
    }

    /**
     * the new positions are complied to old ones to ensure the right position on the board
     */
    private static int setPos(Stone stone) {
        if (stone == null) {
            return 0;
        }
        if (stone.getPosOne() == 0 && stone.getPosTwo() == 0 && stone.getPosThree() == 0) {
            return 1;
        } else if (stone.getPosOne() == 0 && stone.getPosTwo() == 0 && stone.getPosThree() == 1) {
            return 2;
        } else if (stone.getPosOne() == 0 && stone.getPosTwo() == 0 && stone.getPosThree() == 2) {
            return 3;
        } else if (stone.getPosOne() == 1 && stone.getPosTwo() == 0 && stone.getPosThree() == 0) {
            return 4;
        } else if (stone.getPosOne() == 1 && stone.getPosTwo() == 0 && stone.getPosThree() == 1) {
            return 5;
        } else if (stone.getPosOne() == 1 && stone.getPosTwo() == 0 && stone.getPosThree() == 2) {
            return 6;
        } else if (stone.getPosOne() == 2 && stone.getPosTwo() == 0 && stone.getPosThree() == 0) {
            return 7;
        } else if (stone.getPosOne() == 2 && stone.getPosTwo() == 0 && stone.getPosThree() == 1) {
            return 8;
        } else if (stone.getPosOne() == 2 && stone.getPosTwo() == 0 && stone.getPosThree() == 2) {
            return 9;
        } else if (stone.getPosOne() == 0 && stone.getPosTwo() == 1 && stone.getPosThree() == 0) {
            return 10;
        } else if (stone.getPosOne() == 1 && stone.getPosTwo() == 1 && stone.getPosThree() == 0) {
            return 11;
        } else if (stone.getPosOne() == 2 && stone.getPosTwo() == 1 && stone.getPosThree() == 0) {
            return 12;
        } else if (stone.getPosOne() == 2 && stone.getPosTwo() == 1 && stone.getPosThree() == 2) {
            return 13;
        } else if (stone.getPosOne() == 1 && stone.getPosTwo() == 1 && stone.getPosThree() == 2) {
            return 14;
        } else if (stone.getPosOne() == 0 && stone.getPosTwo() == 1 && stone.getPosThree() == 2) {
            return 15;
        } else if (stone.getPosOne() == 2 && stone.getPosTwo() == 2 && stone.getPosThree() == 0) {
            return 16;
        } else if (stone.getPosOne() == 2 && stone.getPosTwo() == 2 && stone.getPosThree() == 1) {
            return 17;
        } else if (stone.getPosOne() == 2 && stone.getPosTwo() == 2 && stone.getPosThree() == 2) {
            return 18;
        } else if (stone.getPosOne() == 1 && stone.getPosTwo() == 2 && stone.getPosThree() == 0) {
            return 19;
        } else if (stone.getPosOne() == 1 && stone.getPosTwo() == 2 && stone.getPosThree() == 1) {
            return 20;
        } else if (stone.getPosOne() == 1 && stone.getPosTwo() == 2 && stone.getPosThree() == 2) {
            return 21;
        } else if (stone.getPosOne() == 0 && stone.getPosTwo() == 2 && stone.getPosThree() == 0) {
            return 22;
        } else if (stone.getPosOne() == 0 && stone.getPosTwo() == 2 && stone.getPosThree() == 1) {
            return 23;
        } else if (stone.getPosOne() == 0 && stone.getPosTwo() == 2 && stone.getPosThree() == 2) {
            return 24;
        }
        return -1;
    }

    /**
     * sets label text
     */
    public void setCurrent_state(String text) {
        current_state.setText(text);
        update(this.getGraphics());
    }

    public void setPlayer_one_name(String player_one_name) {
        this.player_one_name = player_one_name;
    }

    public void setPlayer_two_name(String player_two_name) {
        this.player_two_name = player_two_name;
    }

    /**
     * shows stone on the board
     */
    protected void placeStone(Stone stone) {
        board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = stone;
        repaint();
    }

    /**
     * removes stone from the board
     */
    protected void remove(Stone stone) {
        board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = null;
        repaint();
    }

    /**
     * moves stone on the board
     */
    protected void move_stone(Stone start, Stone destination) {
        board[start.getPosOne()][start.getPosTwo()][start.getPosThree()] = null;
        board[destination.getPosOne()][destination.getPosTwo()][destination.getPosThree()] = destination;
        repaint();
    }

    /**
     * clears everything before repaint to ensure nothing gets mixed up
     */
    private void clearRect(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    protected Color getWoodenColor() {
        return new Color(202, 164, 114);
    }

    private void drawStone(Graphics2D g2D, int pos, String player) {
        int radius = this.getWidth() / 14;
        int circleDiameter = 50;
        int circleRadius = circleDiameter / 2;
        if (Objects.equals(player, player_one_name)) {
            g2D.setColor(playerone_stone_color);
        } else if (Objects.equals(player, player_two_name)) {
            g2D.setColor(playertwo_stone_color);
        } else {
            System.err.println("[PANEL] Wrong Playername!");
        }

        if (pos == -1) {
            System.err.println("[Panel] Wrong Pos!");
        } else if (pos == 0) {
            System.out.println("[Panel] Init");
        }

        switch (pos) {
            case 1 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 3 - circleRadius, (this.getHeight() / 2) - radius * 3 - circleRadius, circleDiameter, circleDiameter);
            case 2 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 3 + radius * 3 - circleRadius, (this.getHeight() / 2) - radius * 3 - circleRadius, circleDiameter, circleDiameter);
            case 3 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 3 + 2 * radius * 3 - circleRadius, (this.getHeight() / 2) - radius * 3 - circleRadius, circleDiameter, circleDiameter);
            case 4 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 2 - circleRadius, (this.getHeight() / 2) - radius * 2 - circleRadius, circleDiameter, circleDiameter);
            case 5 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 2 + radius * 2 - circleRadius, (this.getHeight() / 2) - radius * 2 - circleRadius, circleDiameter, circleDiameter);
            case 6 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 2 + 2 * radius * 2 - circleRadius, (this.getHeight() / 2) - radius * 2 - circleRadius, circleDiameter, circleDiameter);
            case 7 ->
                    g2D.fillOval((this.getWidth() / 2) - radius - circleRadius, (this.getHeight() / 2) - radius - circleRadius, circleDiameter, circleDiameter);
            case 8 ->
                    g2D.fillOval((this.getWidth() / 2) - radius + radius - circleRadius, (this.getHeight() / 2) - radius - circleRadius, circleDiameter, circleDiameter);
            case 9 ->
                    g2D.fillOval((this.getWidth() / 2) - radius + 2 * radius - circleRadius, (this.getHeight() / 2) - radius - circleRadius, circleDiameter, circleDiameter);
            case 10 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 3 - circleRadius, (this.getHeight() / 2) - radius * 3 + radius * 3 - circleRadius, circleDiameter, circleDiameter);
            case 11 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 2 - circleRadius, (this.getHeight() / 2) - radius * 2 + radius * 2 - circleRadius, circleDiameter, circleDiameter);
            case 12 ->
                    g2D.fillOval((this.getWidth() / 2) - radius - circleRadius, (this.getHeight() / 2) - radius + radius - circleRadius, circleDiameter, circleDiameter);
            case 13 ->
                    g2D.fillOval((this.getWidth() / 2) - radius + 2 * radius - circleRadius, (this.getHeight() / 2) - radius + radius - circleRadius, circleDiameter, circleDiameter);
            case 14 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 2 + 2 * radius * 2 - circleRadius, (this.getHeight() / 2) - radius * 2 + radius * 2 - circleRadius, circleDiameter, circleDiameter);
            case 15 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 3 + 2 * radius * 3 - circleRadius, (this.getHeight() / 2) - radius * 3 + radius * 3 - circleRadius, circleDiameter, circleDiameter);
            case 16 ->
                    g2D.fillOval((this.getWidth() / 2) - radius - circleRadius, (this.getHeight() / 2) - radius + 2 * radius - circleRadius, circleDiameter, circleDiameter);
            case 17 ->
                    g2D.fillOval((this.getWidth() / 2) - radius + radius - circleRadius, (this.getHeight() / 2) - radius + 2 * radius - circleRadius, circleDiameter, circleDiameter);
            case 18 ->
                    g2D.fillOval((this.getWidth() / 2) - radius + 2 * radius - circleRadius, (this.getHeight() / 2) - radius + 2 * radius - circleRadius, circleDiameter, circleDiameter);
            case 19 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 2 - circleRadius, (this.getHeight() / 2) - radius * 2 + 2 * radius * 2 - circleRadius, circleDiameter, circleDiameter);
            case 20 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 2 + radius * 2 - circleRadius, (this.getHeight() / 2) - radius * 2 + 2 * radius * 2 - circleRadius, circleDiameter, circleDiameter);
            case 21 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 2 + 2 * radius * 2 - circleRadius, (this.getHeight() / 2) - radius * 2 + 2 * radius * 2 - circleRadius, circleDiameter, circleDiameter);
            case 22 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 3 - circleRadius, (this.getHeight() / 2) - radius * 3 + 2 * radius * 3 - circleRadius, circleDiameter, circleDiameter);
            case 23 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 3 + radius * 3 - circleRadius, (this.getHeight() / 2) - radius * 3 + 2 * radius * 3 - circleRadius, circleDiameter, circleDiameter);
            case 24 ->
                    g2D.fillOval((this.getWidth() / 2) - radius * 3 + 2 * radius * 3 - circleRadius, (this.getHeight() / 2) - radius * 3 + 2 * radius * 3 - circleRadius, circleDiameter, circleDiameter);
        }
    }

    public void paint(Graphics g) {
        int diameter = this.getWidth() / 7;
        int radius = this.getWidth() / 14;
        int numbersOfRectangles = 3;
        Graphics2D g2D = (Graphics2D) g;
        clearRect(g);
        g2D.setColor(getWoodenColor());
        g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2D.setStroke(new BasicStroke(5));
        g2D.setColor(Color.BLACK);
        //Rectangles
        for (int i = 1; i <= numbersOfRectangles; i++) {
            int x = (this.getWidth() / 2) - radius * i;
            int y = (this.getHeight() / 2) - radius * i;
            g2D.drawRect(x, y, diameter * i, diameter * i);
        }

        //Lines
        //Line 1
        g2D.drawLine((this.getWidth() / 2) - radius * 3, this.getHeight() / 2, (this.getWidth() / 2) - radius, this.getHeight() / 2);
        //Line 2
        g2D.drawLine((this.getWidth() / 2), (this.getHeight() / 2) - radius * 3, this.getWidth() / 2, (this.getHeight() / 2) - radius);
        //Line 3
        g2D.drawLine((this.getWidth() / 2) + radius, this.getHeight() / 2, (this.getWidth() / 2) + diameter * 3 / 2, this.getHeight() / 2);
        //Line 4
        g2D.drawLine(this.getWidth() / 2, (this.getHeight() / 2) + radius, this.getWidth() / 2, (this.getHeight() / 2) + radius * 3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (board[i][j][k] != null) {
                        drawStone(g2D, setPos(board[i][j][k]), board[i][j][k].getPlayer());
                    }
                }
            }
        }
    }
}
