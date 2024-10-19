package View;

import Model.Blackboard;
import Model.Circle;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class DrawPanel extends JPanel {

    public DrawPanel(){
        setBackground(Color.WHITE);
        setBorder(new MatteBorder(3, 3, 3, 3, Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Circle c : Blackboard.getInstance().getCircleList()) {
            // do not draw in preferencepanel area
            g.setColor(c.getColor());
            g.fillOval(c.getX() - c.getRadius(), c.getY() - c.getRadius(),
                     2 * c.getRadius(), 2 * c.getRadius());

            g.setColor(Color.BLACK); // Set the outline color to black
            g.drawOval(c.getX() - c.getRadius(), c.getY() - c.getRadius(),
            2 * c.getRadius(), 2 * c.getRadius());
        }
    }
}
