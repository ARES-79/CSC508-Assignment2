package Model;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Blackboard {

    private static final int TIMEOUT_IN_MS = 200;
    private final BlockingQueue<String> eyeTrackingQueue;
    private final BlockingQueue<String> emotionQueue;
    private final Queue<ProcessedDataObject> processedDataQueue;
    private ArrayList<Circle> circleList;

    private static final Blackboard INSTANCE = new Blackboard();

    // Private constructor to prevent instantiation from other classes
    private Blackboard() {
        eyeTrackingQueue = new LinkedBlockingQueue<>();
        emotionQueue = new LinkedBlockingQueue<>();
        processedDataQueue  = new ConcurrentLinkedQueue<>();
        circleList = new ArrayList<>();
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
    }

    public ProcessedDataObject getFromProcessedDataObjectQueue(){
        return processedDataQueue.poll();
    }

    public ArrayList<Circle> getCircleList() {
        return circleList;
    }

    public void setCircleList(ArrayList<Circle> circleList) {
        this.circleList = circleList;
    }

    public void addCircleToList(Circle circle){
        circleList.add(circle);
    }
}
