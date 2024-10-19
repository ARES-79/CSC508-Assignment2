package View;

import Model.Blackboard;
import Data.Circle;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class DrawPanel extends JPanel implements PropertyChangeListener {

    public DrawPanel(){
        setBackground(Color.WHITE);
        setBorder(new MatteBorder(3, 3, 3, 3, Color.BLACK));
        Blackboard.getInstance().addChangeSupportListener(
                Blackboard.PROPERTY_NAME_VIEW_DATA, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Circle c : Blackboard.getInstance().getCircleList()) {
            c.drawCircle(g);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }
}
