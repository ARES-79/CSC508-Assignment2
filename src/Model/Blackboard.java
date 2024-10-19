package Model;

import View.DrawPanel;
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
 * 
 * This class follows the singleton design pattern, ensuring that only one instance of {@code Blackboard}
 * exists during the application's lifecycle. It provides synchronized access to the data being exchanged 
 * between components, and manages the state of data retrieval.
 */
public class Blackboard {

    private static final int TIMEOUT_IN_MS = 500;
    private String emotionServerIp = "localhost";
    private int emotionServerPort = 6000;
    private String eyeTrackingServerIp = "localhost";
    private int eyeTrackingServerPort = 6001;

    private int maxCircles = 5;
    private int thresholdRadius = 50;
    private int circleRadius = 50;

    private final BlockingQueue<String> eyeTrackingQueue;
    private final BlockingQueue<String> emotionQueue;
    private final Queue<ProcessedDataObject> processedDataQueue;
    private boolean startFlag;
    private Deque<Circle> circleList;

    private DrawPanel drawPanel;

    private static final Blackboard INSTANCE = new Blackboard();

    // Private constructor to prevent instantiation from other classes
    private Blackboard() {
        eyeTrackingQueue = new LinkedBlockingQueue<>();
        emotionQueue = new LinkedBlockingQueue<>();
        processedDataQueue  = new ConcurrentLinkedQueue<>();
        circleList = new ConcurrentLinkedDeque<>();
        drawPanel = new DrawPanel();
        startFlag = false;
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
    }

    public ProcessedDataObject getFromProcessedDataObjectQueue(){
        return processedDataQueue.poll();
    }

    public Deque<Circle> getCircleList() {
        return circleList;
    }

    public void setCircleList(Deque<Circle> circleList) {
        this.circleList = circleList;
    }

    public void addCircleToList(Circle circle){
        circleList.add(circle);
    }

    public DrawPanel getDrawPanel() {
        return drawPanel;
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

    public String getEmotionServerIp() {
        return emotionServerIp;
    }

    public void setEmotionServerIp(String emotionServerIp) {
        this.emotionServerIp = emotionServerIp;
    }

    public int getEmotionServerPort() {
        return emotionServerPort;
    }

    public void setEmotionServerPort(int emotionServerPort) {
        this.emotionServerPort = emotionServerPort;
    }

    public String getEyeTrackingServerIp() {
        return eyeTrackingServerIp;
    }

    public void setEyeTrackingServerIp(String eyeTrackingServerIp) {
        this.eyeTrackingServerIp = eyeTrackingServerIp;
    }

    public int getEyeTrackingServerPort() {
        return eyeTrackingServerPort;
    }

    public void setEyeTrackingServerPort(int eyeTrackingServerPort) {
        this.eyeTrackingServerPort = eyeTrackingServerPort;
    }

    public boolean getStartFlag(){return startFlag;}
    public void startDataRetrieval(){startFlag = true;}

    public void stopDataRetrieval(){startFlag = false;}
}
