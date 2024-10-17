package View;

import Model.Blackboard;
import Model.Circle;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    public DrawPanel(){
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("paintComponent called");
        for (Circle c : Blackboard.getInstance().getCircleList()) {
            g.setColor(c.getColor());
            g.fillOval(c.getX() - c.getRadius(), c.getY() - c.getRadius(),
                    2 * c.getRadius(), 2 * c.getRadius());
        }
    }
}
