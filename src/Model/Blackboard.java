package Model;

import View.DrawPanel;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Blackboard {

    private static final int TIMEOUT_IN_MS = 500;
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

    public boolean getStartFlag(){return startFlag;}
    public void startDataRetrieval(){startFlag = true;}

    public void stopDataRetrieval(){startFlag = false;}
}
