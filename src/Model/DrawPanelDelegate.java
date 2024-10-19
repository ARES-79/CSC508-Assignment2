package Model;

import Data.Circle;
import Data.ProcessedDataObject;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Deque;
import java.util.logging.Logger;

public class DrawPanelDelegate extends CustomThread implements PropertyChangeListener {

    private static final int MAX_CIRCLES = 5; // FIFO size limit
    private static final int THRESHOLD_RADIUS = 50; // Radius threshold for consolidation
    public static final int CIRCLE_RADIUS = 50;
    private static final String THREAD_NAME = "ViewLogic";
    public DrawPanelDelegate(){
        super();
        super.setLog(Logger.getLogger(DrawPanelDelegate.class.getName()));
        super.setName(THREAD_NAME);
        Blackboard.getInstance().addChangeSupportListener(
                Blackboard.PROPERTY_NAME_PROCESSED_DATA, this);
    }

    @Override
    public void doYourWork() throws InterruptedException, IOException {
        //keep the thread alive and idle while waiting for new data
        synchronized (this) {
            wait();
        }
    }

    @Override
    public void cleanUpThread() {
        Blackboard.getInstance().removePropertyChangeListener(
                Blackboard.PROPERTY_NAME_PROCESSED_DATA, this);
    }

    private void handleProcessedData(ProcessedDataObject data) {
        Deque<Circle> circleList = Blackboard.getInstance().getCircleList();
        Color circleColor = data.prominentEmotion().getColor();
        Circle newCircle = new Circle(data.xCoord(), data.yCoord(), circleColor, CIRCLE_RADIUS);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ProcessedDataObject data = Blackboard.getInstance().getFromProcessedDataObjectQueue();
        System.out.println("retrieved processed data: " + data);
        if (data != null) {
            handleProcessedData(data);
        }
    }
}
