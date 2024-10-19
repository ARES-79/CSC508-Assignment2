package Model;

import java.awt.*;

public class Circle {

    //x and y coordinate for the center of the circle
    private final int xCoord;
    private final int yCoord;
    private Color color;
    private int radius;

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

}
