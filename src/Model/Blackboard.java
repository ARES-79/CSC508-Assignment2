package Model;

import Data.Circle;
import Data.ProcessedDataObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * The {@code Blackboard} class serves as the central hub for managing data across different components
 * of the system. It holds data queues for eye-tracking and emotion information, a list of circles for the display,
 * and settings for server information and display behavior.
 * <p>
 * This class follows the singleton design pattern, ensuring that only one instance of {@code Blackboard}
 * exists during the application's lifecycle. It provides synchronized access to the data being exchanged
 * between components, and manages the state of data retrieval.
 */
public class Blackboard {

    // EYE TRACKING DATA
    private String eyeTrackingSocket_Host = "localhost";  // default for testing
    private int eyeTrackingSocket_Port = 6001;  // default for testing

    private final BlockingQueue<String> eyeTrackingQueue;

    // EMOTION TRACKING DATA
    private String emotionSocket_Host = "localhost"; // default for testing
    private int emotionSocket_Port = 6000; // default for testing
    private final BlockingQueue<String> emotionQueue;

    //COMBINED DATA
    private final Queue<ProcessedDataObject> processedDataQueue;
    public static final String PROPERTY_NAME_PROCESSED_DATA = "processed data";
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    //VIEW DATA
    public static final String PROPERTY_NAME_VIEW_DATA = "view data";
    private Deque<Circle> circleList;
    private int maxCircles = 5;
    private int thresholdRadius = 50;
    private int circleRadius = 50;
    public static final int paddingFromTop = 150; // height of the top panel

    //THREAD MAINTENANCE
    public static final String PROPERTY_NAME_EYETHREAD_ERROR = "eye tracking thread error";
    public static final String PROPERTY_NAME_EMOTIONTHREAD_ERROR = "eye emotion thread error";

    //MISC
    private static final int TIMEOUT_IN_MS = 500;


    private static final Blackboard INSTANCE = new Blackboard();

    // Private constructor to prevent instantiation from other classes
    private Blackboard() {
        eyeTrackingQueue = new LinkedBlockingQueue<>();
        emotionQueue = new LinkedBlockingQueue<>();
        processedDataQueue  = new ConcurrentLinkedQueue<>();
        circleList = new ConcurrentLinkedDeque<>();
    }

    // Provide a global point of access to the singleton instance
    public static Blackboard getInstance() {
        return INSTANCE;
    }

    public void addToEyeTrackingQueue(String data) throws InterruptedException {
        eyeTrackingQueue.put(data);
    }

    /**
    * Retrieves and removes the head of the eye-tracking queue, waiting up to a specified
    * timeout if necessary for an element to become available.
    *
    * @return the eye-tracking data or null if the specified timeout is reached
    * @throws InterruptedException if interrupted while waiting
    */
    public String pollEyeTrackingQueue() throws InterruptedException {
        return eyeTrackingQueue.poll(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS);
    }

    public void addToEmotionQueue(String data) throws InterruptedException {
        emotionQueue.put(data);
    }

    /**
    * Retrieves and removes the head of the emotion queue, waiting up to a specified
    * timeout if necessary for an element to become available.
    *
    * @return the emotion data or null if the specified timeout is reached
    * @throws InterruptedException if interrupted while waiting
    */
    public String pollEmotionQueue() throws InterruptedException {
        return emotionQueue.poll(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS);
    }

    public void addToProcessedDataQueue(ProcessedDataObject data){
        processedDataQueue.add(data);
        changeSupport.firePropertyChange(PROPERTY_NAME_PROCESSED_DATA, null, null);
    }

    public ProcessedDataObject getFromProcessedDataObjectQueue(){
        return processedDataQueue.poll();
    }

    public Deque<Circle> getCircleList() {
        return circleList;
    }

    public void setCircleList(Deque<Circle> circleList) {
        this.circleList = circleList;
        changeSupport.firePropertyChange(PROPERTY_NAME_VIEW_DATA, null, null);
    }

    public String getFormattedConnectionSettings(){
        return String.format(
                """
                        \t\tEye Tracking Socket IP: %s:%s
                        \t\tEmotion Tracking Socket IP: %s:%s
                        """,
            eyeTrackingSocket_Host, eyeTrackingSocket_Port,
            emotionSocket_Host, emotionSocket_Port);}


    public String getEyeTrackingSocket_Host() {
        return eyeTrackingSocket_Host;
    }

    public int getEyeTrackingSocket_Port() {
        return eyeTrackingSocket_Port;
    }

    public String getEmotionSocket_Host() {
        return emotionSocket_Host;
    }

    public int getEmotionSocket_Port() {
        return emotionSocket_Port;
    }

    public void setEyeTrackingSocket_Host(String eyeTrackingSocket_Host) {
        this.eyeTrackingSocket_Host = eyeTrackingSocket_Host;
    }

    public void setEyeTrackingSocket_Port(int eyeTrackingSocket_Port) {
        this.eyeTrackingSocket_Port = eyeTrackingSocket_Port;
    }

    public void setEmotionSocket_Host(String emotionSocket_Host) {
        this.emotionSocket_Host = emotionSocket_Host;
    }

    public void setEmotionSocket_Port(int emotionSocket_Port) {
        this.emotionSocket_Port = emotionSocket_Port;
    }

    public void addChangeSupportListener(String propertyName, PropertyChangeListener pcl) {
        changeSupport.addPropertyChangeListener(propertyName, pcl);
    }
    public int getMaxCircles() {
        return maxCircles;
    }

    public void setMaxCircles(int maxCircles) {
        this.maxCircles = maxCircles;
    }

    public int getThresholdRadius() {
        return thresholdRadius;
    } 

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public void setThresholdRadius(int thresholdRadius) {
        this.thresholdRadius = thresholdRadius;
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener pcl) {
        changeSupport.removePropertyChangeListener(propertyName, pcl);
    }

    public void reportEyeThreadError(String ex_message){
        changeSupport.firePropertyChange(PROPERTY_NAME_EYETHREAD_ERROR, null, ex_message);
    }

    public void reportEmotionThreadError(String ex_message){
        changeSupport.firePropertyChange(PROPERTY_NAME_EMOTIONTHREAD_ERROR, null, ex_message);
    }
}
