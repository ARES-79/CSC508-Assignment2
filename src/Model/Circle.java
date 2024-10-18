package Model;

import java.awt.*;
import javax.swing.JTextArea;

public class Circle {

    //x and y coordinate for the center of the circle
    private final int xCoord;
    private final int yCoord;
    private Color color;
    private JTextArea labelArea = null;
    private int radius;

    //constructor without label
    public Circle(int xCoord, int yCoord, Color color, int radius) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.color = color;
        this.radius = radius;
    }  
    // constructor with label
    public Circle(int xCoord, int yCoord, Color color, int radius, JTextArea label) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.color = color;
        this.radius = radius;
        labelArea = label;
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
    
    public JTextArea getLabel() {
        return labelArea;
    }

    public void increaseRadius(int increment) {
        this.radius += increment;
        //increase font size of label if exists
    }

}
