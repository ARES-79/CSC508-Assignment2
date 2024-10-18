package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Blackboard {

    // EYE TRACKING DATA
    private String eyeTrackingSocket_Host = "localhost";  // default for testing
    private String eyeTrackingSocket_Port = "6001";  // default for testing
    private final BlockingQueue<String> eyeTrackingQueue;

    // EMOTION TRACKING DATA
    private String emotionSocket_Host = "localhost"; // default for testing
    private String emotionSocket_Port = "6000"; // default for testing
    private final BlockingQueue<String> emotionQueue;

    //COMBINED DATA
    private final Queue<ProcessedDataObject> processedDataQueue;
    public static final String PROPERTY_NAME_PROCESSED_DATA = "processed data";
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    //VIEW DATA
    public static final String PROPERTY_NAME_VIEW_DATA = "view data";
    private Deque<Circle> circleList;

    //MISC
    private static final int TIMEOUT_IN_MS = 500;

    private static final Blackboard INSTANCE = new Blackboard();

    // Private constructor to prevent instantiation from other classes
    private Blackboard() {
        super();
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

    public String pollEyeTrackingQueue() throws InterruptedException {
        return eyeTrackingQueue.poll(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS);
    }

    public void addToEmotionQueue(String data) throws InterruptedException {
        emotionQueue.put(data);
    }

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


    public String getEyeTrackingSocket_Host() {
        return eyeTrackingSocket_Host;
    }

    public String getEyeTrackingSocket_Port() {
        return eyeTrackingSocket_Port;
    }

    public String getEmotionSocket_Host() {
        return emotionSocket_Host;
    }

    public String getEmotionSocket_Port() {
        return emotionSocket_Port;
    }

    public void setEyeTrackingSocket_Host(String eyeTrackingSocket_Host) {
        this.eyeTrackingSocket_Host = eyeTrackingSocket_Host;
    }

    public void setEyeTrackingSocket_Port(String eyeTrackingSocket_Port) {
        this.eyeTrackingSocket_Port = eyeTrackingSocket_Port;
    }

    public void setEmotionSocket_Host(String emotionSocket_Host) {
        this.emotionSocket_Host = emotionSocket_Host;
    }

    public void setEmotionSocket_Port(String emotionSocket_Port) {
        this.emotionSocket_Port = emotionSocket_Port;
    }

    public void addChangeSupportListener(String propertyName, PropertyChangeListener pcl) {
        changeSupport.addPropertyChangeListener(propertyName, pcl);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener pcl) {
        changeSupport.removePropertyChangeListener(propertyName, pcl);
    }
}
