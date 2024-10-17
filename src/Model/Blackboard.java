package Model;

import DataClients.EmotionDataClient;
import DataClients.EyeTrackingClient;
import View.DrawPanel;
//import View.DisplayArea;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Blackboard {

    private static final int TIMEOUT_IN_MS = 200;
    private final BlockingQueue<String> eyeTrackingQueue;
    private final BlockingQueue<String> emotionQueue;
    private final Queue<ProcessedDataObject> processedDataQueue;
    private Deque<Circle> circleList;
    //private DisplayArea displayArea;

    private DrawPanel drawPanel;

    private static final Blackboard INSTANCE = new Blackboard();

    // Private constructor to prevent instantiation from other classes
    private Blackboard() {
        eyeTrackingQueue = new LinkedBlockingQueue<>();
        emotionQueue = new LinkedBlockingQueue<>();
        processedDataQueue  = new ConcurrentLinkedQueue<>();
        circleList = new ConcurrentLinkedDeque<>();
//        displayArea = new DisplayArea();
        drawPanel = new DrawPanel();

//        startClients();
    }

    // Find a way to remove this method
    public BlockingQueue<String> getEmotionQueue() {
         return emotionQueue;
    }
    public BlockingQueue<String> getEyeTrackingQueue() {
         return eyeTrackingQueue;
    }

    // Provide a global point of access to the singleton instance
    public static Blackboard getInstance() {
        return INSTANCE;
    }

//    private void startClients() {
//      EmotionDataClient emotionDataClient = new EmotionDataClient();
//      Thread emotionThread = new Thread(emotionDataClient);
//
//      EyeTrackingClient eyeTrackingClient = new EyeTrackingClient();
//      Thread eyeTrackingThread = new Thread(eyeTrackingClient);
//
//      emotionThread.start();
//      eyeTrackingThread.start();
//    }

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

    /*public DisplayArea getDisplayArea() {
        return displayArea;
    }*/

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }
}
