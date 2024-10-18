package View;

import Model.Blackboard;
import Model.Circle;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class DrawPanel extends JPanel implements PropertyChangeListener {

    public DrawPanel(){
        setBackground(Color.WHITE);
        Blackboard.getInstance().addChangeSupportListener(
                Blackboard.PROPERTY_NAME_VIEW_DATA, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Circle c : Blackboard.getInstance().getCircleList()) {
            g.setColor(c.getColor());
            g.fillOval(c.getX() - c.getRadius(), c.getY() - c.getRadius(),
                    2 * c.getRadius(), 2 * c.getRadius());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }
}
