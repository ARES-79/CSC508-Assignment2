package Data;

import Model.Blackboard;

import java.awt.*;

/**
 * The {@code Circle} class represents a circle with a specified center (x, y), color, and radius.
 * This class is used for drawing and managing circle objects in the graphical interface.
 */
public class Circle {

    //x and y coordinate for the center of the circle
    private final int xCoord;
    private final int yCoord;
    private Color color;
    private int radius;

    /**
    * Constructs a new {@code Circle} with specified coordinates, color, and radius.
    *
    * @param xCoord the x-coordinate of the center of the circle
    * @param yCoord the y-coordinate of the center of the circle
    * @param color  the color of the circle
    * @param radius the radius of the circle
    */
    public Circle(int xCoord, int yCoord, Color color, int radius) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.color = color;
        this.radius = radius;
    }
    public int getX() {
        return xCoord; 
    }

    public int getY() {
        return yCoord;
    }

    public int getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public void increaseRadius(int increment) {
        this.radius += increment;
    }

    public void drawCircle(Graphics g){
        // do not draw in preferencepanel area
        if (xCoord - radius >= Blackboard.paddingFromTop) {
            g.setColor(color);
            g.fillOval(xCoord - radius, yCoord - radius,
                    2 * radius, 2 * radius);
            g.setColor(Color.BLACK);
            g.drawOval(xCoord - radius, yCoord - radius,
                    2 * radius, 2 * radius);
        }
    }

}
