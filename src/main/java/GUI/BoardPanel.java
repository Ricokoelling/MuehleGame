package GUI;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    public BoardPanel() {
        this.setSize(1080,720);



        this.setVisible(true);
    }

    private void clearRect(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.clearRect(0, 0, this.getWidth(), this.getHeight());
    }
    protected Color getWoodenColor(){
        return new Color(202, 164, 114);
    }
    public void paint(Graphics g){
        int diameter = this.getWidth() / 7;
        int radius = this.getWidth() / 14;
        int numbersOfRectangles = 3;
        Graphics2D g2D = (Graphics2D) g;
        clearRect(g);
        g2D.setColor(getWoodenColor());
        g2D.fillRect(0,0, this.getWidth(),this.getHeight());
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

    }
}
