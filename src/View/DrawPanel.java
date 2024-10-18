package View;

import Model.Blackboard;
import Model.Circle;
import java.awt.*;
import javax.swing.*;

public class DrawPanel extends JPanel {
    private static final Font font = new Font("Dialog", Font.PLAIN, 20);

    public DrawPanel(){
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Circle c : Blackboard.getInstance().getCircleList()) {
            g.setColor(c.getColor());
            g.fillOval(c.getX() - c.getRadius(), c.getY() - c.getRadius(),
                    2 * c.getRadius(), 2 * c.getRadius());
            // label circles
            // if (labels)
            add(c.getLabel());
        }
    }
}
