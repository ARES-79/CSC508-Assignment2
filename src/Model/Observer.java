package Model;

import java.awt.Color;
import java.util.Deque;

/**
 * The {@code Observer} class continuously monitors the {@link Blackboard} for processed data objects
 * and updates the display by managing the list of circles shown on the {@link View.DrawPanel}.
 * 
 * This class implements {@link Runnable} and is intended to be run as a separate thread.
 * It handles the consolidation of circles based on proximity and dynamically updates the display.
 */
public class Observer implements Runnable {

    /**
     * Runs the main logic of the {@code Observer}, continuously checking for new processed data in the
     * {@link Blackboard}'s processed data queue. If data is available, it updates the list of circles and repaints
     * the draw panel to reflect the changes.
     * 
     * The method runs in an infinite loop and periodically sleeps to avoid busy waiting when no new data is available.
     */
    @Override
    public void run() {
        try {
            while (true) {
                while (Blackboard.getInstance().getStartFlag()) {
                    ProcessedDataObject data = Blackboard.getInstance().getFromProcessedDataObjectQueue();
                    System.out.println("retrieved processed data: " + data);
                    if (data != null) {
                        handleProcessedData(data);
                        Blackboard.getInstance().getDrawPanel().repaint();
                    } else {
                        Thread.sleep(200); // Add some sleep to avoid busy waiting
                    }
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Processes the received data by checking if the new circle can be consolidated with any existing circle.
     * If the circle can be consolidated (i.e., it is within a specified threshold of another circle), the radius of
     * the existing circle is increased. Otherwise, the new circle is added to the list of circles.
     *
     * If the maximum number of circles is exceeded, the oldest circle is removed.
     * 
     * @param data the processed data object containing the information for the new circle
     */
    private void handleProcessedData(ProcessedDataObject data) {
        Deque<Circle> circleList = Blackboard.getInstance().getCircleList();
        Color circleColor = getColorFromEmotion(data.prominentEmotion());
        Circle newCircle = new Circle(data.xCoord(), data.yCoord(), circleColor, Blackboard.getInstance().getCircleRadius());

        // Check if the new circle is within the threshold of any existing circle
        boolean consolidated = false;
        for (Circle circle : circleList) {
            if (isWithinThreshold(circle, newCircle)) {
                circle.increaseRadius(Blackboard.getInstance().getCircleRadius()); // Consolidate by increasing the radius
                consolidated = true;
                break;
            }
        }

        if (!consolidated) {
            // If the list is full, remove the oldest entry
            if (circleList.size() == Blackboard.getInstance().getMaxCircles()) {
                circleList.pollFirst();
            }
            circleList.addLast(newCircle); // Add the new circle
        }

        Blackboard.getInstance().setCircleList(circleList);
    }

    /**
     * Determines whether the new circle is within the threshold radius of an existing circle.
     * 
     * @param existing the existing circle on the draw panel
     * @param newCircle the new circle to be added
     * @return {@code true} if the new circle is within the threshold radius of the existing circle, {@code false} otherwise
     */
    private boolean isWithinThreshold(Circle existing, Circle newCircle) {
        int dx = existing.getX() - newCircle.getX();
        int dy = existing.getY() - newCircle.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= Blackboard.getInstance().getThresholdRadius();
    }

    /**
     * Maps an emotion to a specific color.
     * 
     * @param emotion the emotion to map
     * @return the corresponding {@link Color} for the given emotion
     */
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
