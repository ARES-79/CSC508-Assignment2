package Model;

import View.DisplayArea;
import java.awt.*;
import java.util.Deque;
import javax.swing.JTextArea;

public class Observer implements Runnable {

    private static final int MAX_CIRCLES = 5; // FIFO size limit
    private static final int THRESHOLD_RADIUS = 50; // Radius threshold for consolidation
    private static final Font font = new Font("Dialog", Font.PLAIN, 10);

    @Override
    public void run() {
        try {
            while (true) {
                while (Blackboard.getInstance().getStartFlag()) {
                    ProcessedDataObject data = Blackboard.getInstance().getFromProcessedDataObjectQueue();
                    System.out.println("retrieved processed data: " + data);
                    if (data != null) {
                        handleProcessedData(data);
                        //Blackboard.getInstance().getDisplayArea().repaint(); // Trigger repaint
                        Blackboard.getInstance().getDrawPanel().repaint();
                    } else {
                        Thread.sleep(100); // Add some sleep to avoid busy waiting
                    }
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleProcessedData(ProcessedDataObject data) {
        Deque<Circle> circleList = Blackboard.getInstance().getCircleList();
        Color circleColor = getColorFromEmotion(data.prominentEmotion());
        //if (createLabel) {}
        JTextArea label = createLabelFromData(data);
        Circle newCircle = new Circle(data.xCoord(), data.yCoord(), circleColor, DisplayArea.CIRCLE_RADIUS, label);
        ///Circle newCircle = new Circle(data.xCoord(), data.yCoord(), circleColor, DisplayArea.CIRCLE_RADIUS);

        // Check if the new circle is within the threshold of any existing circle
        boolean consolidated = false;
        for (Circle circle : circleList) {
            if (isWithinThreshold(circle, newCircle)) {
                circle.increaseRadius(50); // Consolidate by increasing the radius
                consolidated = true;
                break;
            }
        }

        if (!consolidated) {
            // If the list is full, remove the oldest entry
            if (circleList.size() == MAX_CIRCLES) {
                JTextArea labelToRemove = circleList.peekFirst().getLabel();
                if (labelToRemove != null) {
                   Blackboard.getInstance().getDrawPanel().remove(labelToRemove);
                }
                circleList.pollFirst();
            }
            circleList.addLast(newCircle); // Add the new circle
        }

        Blackboard.getInstance().setCircleList(circleList);
    }

    private JTextArea createLabelFromData(ProcessedDataObject data) {
         JTextArea label = new JTextArea();
         Color circleColor = getColorFromEmotion(data.prominentEmotion());
         label.setText(String.valueOf(circleColor.getRGB()));
         label.setEditable(false);
         label.setForeground(Color.WHITE);
         label.setBackground(circleColor);
         label.setAlignmentX(0.5f); //center alignment
         label.setFont(font);
         label.setBounds(data.xCoord() - 25,data.yCoord() - 5,25,10);
         return label;
    }

    private boolean isWithinThreshold(Circle existing, Circle newCircle) {
        int dx = existing.getX() - newCircle.getX();
        int dy = existing.getY() - newCircle.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= THRESHOLD_RADIUS;
    }

    private Color getColorFromEmotion(DataProcessor.Emotion emotion) {
        switch (emotion) {
            case NEUTRAL: return Color.GRAY;
            case FOCUS: return Color.YELLOW;
            case STRESS: return Color.RED;
            case ENGAGEMENT: return Color.BLUE;
            case EXCITEMENT: return Color.GREEN;
            case INTEREST: return Color.MAGENTA;
            default: return Color.BLACK;
        }
    }
}
