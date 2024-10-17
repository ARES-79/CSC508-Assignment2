package Model;

import View.DisplayArea;
import java.awt.Color;
import java.util.Deque;

public class Observer implements Runnable {

    private static final int MAX_CIRCLES = 5; // FIFO size limit
    private static final int THRESHOLD_RADIUS = 50; // Radius threshold for consolidation

    @Override
    public void run() {
        try {
            while (true) {
                ProcessedDataObject data = Blackboard.getInstance().getFromProcessedDataObjectQueue();
                if (data != null) {
                    handleProcessedData(data);
                    Blackboard.getInstance().getDisplayArea().repaint(); // Trigger repaint
                } else {
                    Thread.sleep(200); // Add some sleep to avoid busy waiting
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleProcessedData(ProcessedDataObject data) {
        Deque<Circle> circleList = Blackboard.getInstance().getCircleList();
        Color circleColor = getColorFromEmotion(data.prominentEmotion());
        Circle newCircle = new Circle(data.xCoord(), data.yCoord(), circleColor, DisplayArea.CIRCLE_RADIUS);

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
                circleList.pollFirst();
            }
            circleList.addLast(newCircle); // Add the new circle
        }

        Blackboard.getInstance().setCircleList(circleList);
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
