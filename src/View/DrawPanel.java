package View;

import Model.Blackboard;
import Model.Circle;
import Model.DataProcessor;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class DrawPanel extends JPanel {
    public static final int TRACKING_RADIUS = 50;
    public static final int CIRCLE_RADIUS = 50;
    private int occupied_x = 0;
    private int occupied_y = 0;

    private int x, y, radius;
    private final Random random = new Random();

    public DrawPanel(){
        setBackground(Color.WHITE);
    }

    // takes in Emotion, x/y
    // NEUTRAL, FOCUS, STRESS, ENGAGEMENT, EXCITEMENT, INTEREST
    // GRAY, YELLOW, RED, BLUE, GREEN, PURPLE
    public void generateCircle(DataProcessor.Emotion e, int x, int y) {
        // take bundle from DataProcessor and draw circle
        // determine if circle already exists at x/y
        // if already in occupied_x +-50 and occupied_y +-50,
        // expand visual radius by 50
        if (occupied_x - TRACKING_RADIUS <= x && x <= occupied_x + TRACKING_RADIUS &&
                occupied_y - TRACKING_RADIUS <= y && y <= occupied_y + TRACKING_RADIUS) {
            // expand visual radius by 50
            radius += 50;
        } else {
            // set new occupied x and y
            occupied_x = x;
            occupied_y = y;
            radius = CIRCLE_RADIUS;
        }
        switch(e) {
            case NEUTRAL:
                Color color = Color.GRAY;
                break;
            case FOCUS:
                color = Color.YELLOW;
                break;
            case STRESS:
                color = Color.RED;
                break;
            case ENGAGEMENT:
                color = Color.BLUE;
                break;
            case EXCITEMENT:
                color = Color.GREEN;
                break;
            case INTEREST:
                color = Color.MAGENTA;
                break;
            default:
                // default to black
                color = Color.BLACK;
        }
        System.out.println("Repainting...");
        invalidate();
        revalidate();
        repaint();
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
