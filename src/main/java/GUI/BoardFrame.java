package GUI;

import Logic.LogicDealer;
import Logic.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BoardFrame extends JFrame implements MouseMotionListener, MouseListener {

    private static final Stone[][][] board = new Stone[3][3][3];

    private BoardPanel panel = new BoardPanel();
    private final LogicDealer logic = new LogicDealer();
    private String player;
    private int Phase = -1;
    int radius = panel.getWidth() / 14;
    int circleDiameter = 30;
    int circleRadius = circleDiameter / 2;


    public BoardFrame(String player) throws HeadlessException {
        this.player = player;

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setSize(1080,720);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(panel);
        this.setVisible(true);
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
        if(Phase == 1) {
            Stone stone = null;
            if (e.getX() > ((this.getWidth() / 2) - radius * 3) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 3) - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [1]
                stone = new Stone(this.player, 0, 0, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 3) + radius * 3 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 3) + radius * 3 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [2]
                stone = new Stone(this.player, 0, 0, 1);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 3) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 3) - circleRadius + 60) {   //point [3]
                stone = new Stone(this.player, 0, 0, 2);
            }
            // pos 10 & 15
            else if (e.getX() > ((this.getWidth() / 2) - radius * 3) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 3) - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 60) {   //point [10]
                stone = new Stone(this.player, 0, 1, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 3) + radius * 3 - circleRadius + 60) {   //point [15]
                stone = new Stone(this.player, 0, 1, 2);
            }
            //pos 22 23 24
            else if (e.getX() > ((this.getWidth() / 2) - radius * 3) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 3) - circleRadius + 10 && e.getY() > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [22]
                stone = new Stone(this.player, 0, 2, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 3) + 1 + radius * 3 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 3) + 1 + radius * 3 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [23]
                stone = new Stone(this.player, 0, 2, 1);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 3) + 2 * radius * 3 - circleRadius + 60) {   //point [24]
                stone = new Stone(this.player, 0, 2, 2);
            }
            // middle rect
            //pos 4 5 6
            else if (e.getX() > ((this.getWidth() / 2) - radius * 2) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 2) - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [4]
                stone = new Stone(this.player, 1, 0, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [5]
                stone = new Stone(this.player, 1, 0, 1);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 2) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 2) - circleRadius + 60) {   //point [6]
                stone = new Stone(this.player, 1, 0, 2);
            }
            // pos 11 14
            else if (e.getX() > ((this.getWidth() / 2) - radius * 2) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 2) - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 60) {   //point [11]
                stone = new Stone(this.player, 1, 1, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 2) + radius * 2 - circleRadius + 60) {   //point [14]
                stone = new Stone(this.player, 1, 1, 2);
            }
            //pos 19 20 21
            else if (e.getX() > ((this.getWidth() / 2) - radius * 2) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 2) - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [19]
                stone = new Stone(this.player, 1, 2, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 2) + radius * 2 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 2) + 1 + radius * 2 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [20]
                stone = new Stone(this.player, 1, 2, 1);
            } else if (e.getX() > ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius * 2) + 2 * radius * 2 - circleRadius + 60) {   //point [21]
                stone = new Stone(this.player, 1, 2, 2);
            }
            //small rect
            //pos 7 8 9
            else if (e.getX() > ((this.getWidth() / 2) - radius) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius) - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [4]
                stone = new Stone(this.player, 2, 0, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius) + radius - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius) + radius - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [5]
                stone = new Stone(this.player, 2, 0, 1);
            } else if (e.getX() > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius) - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius) - circleRadius + 60) {   //point [6]
                stone = new Stone(this.player, 2, 0, 2);
            }
            // pos 12 13
            else if (e.getX() > ((this.getWidth() / 2) - radius) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius) - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius) + radius - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius) + radius - circleRadius + 60) {   //point [12]
                stone = new Stone(this.player, 2, 1, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius) + radius - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius) + radius - circleRadius + 60) {   //point [13]
                stone = new Stone(this.player, 2, 1, 2);
            }
            //pos 16 17 18
            else if (e.getX() > ((this.getWidth() / 2) - radius) - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius) - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [16]
                stone = new Stone(this.player, 2, 2, 0);
            } else if (e.getX() > ((this.getWidth() / 2) - radius) + 1 + radius - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius) + 1 + radius - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [17]
                stone = new Stone(this.player, 2, 2, 1);
            } else if (e.getX() > ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius - 10 && e.getX() < ((this.getWidth() / 2) - radius) + 2 * radius - circleRadius + 40 && e.getY() > ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 10 && e.getY() < ((this.getHeight() / 2) - radius) + 2 * radius - circleRadius + 60) {   //point [18]
                stone = new Stone(this.player, 2, 2, 2);
            }
            if(stone != null){
                logic.placeStone(stone);
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
}
