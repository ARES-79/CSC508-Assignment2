package View;

import Controller.MainController;
import DataClients.*;
import Model.Alt_DataProcessor;
import Model.Blackboard;
import Model.DataProcessor;
import Model.Observer;
import TestServers.EmotionDataServer;
import TestServers.EyeTrackingServer;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;

public class AltMain extends JFrame {

    private static final String TESTING_FLAG = "-test";

    private final ArrayList<CustomThread> threads;

    public static void main (String[] args){
        AltMain window = new AltMain();
        window.setSize(1000,1000); // center on screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (args.length > 0 && args[0].equals(TESTING_FLAG)){
            System.out.println(args[0]);
            window.startServerThreads();
        }
    }
    public AltMain() {
        super("Eye Tracking & Emotion Hub");
        threads = new ArrayList<>();
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu actionsMenu = new JMenu("Actions");
        JMenuItem start = new JMenuItem("Start");
        JMenuItem stop = new JMenuItem("Stop");

        menuBar.add(actionsMenu);
        actionsMenu.add(start);
        actionsMenu.add(stop);
        setJMenuBar(menuBar);

        MainController controller = new MainController(this);
        start.addActionListener(controller);
        stop.addActionListener(controller);

        DrawPanel drawPanel = Blackboard.getInstance().getDrawPanel();
        add(drawPanel, BorderLayout.CENTER);
    }

    private void startAllThreads() {

        Thread emotionThread = new Thread(new EmotionDataClient());
        Thread eyeTrackingThread = new Thread(new EyeTrackingClient());
        Thread dataThread = new Thread(new Alt_DataProcessor());
        Thread observerThread = new Thread(new Observer());

        emotionThread.start();
        eyeTrackingThread.start();
        dataThread.start();
        observerThread.start();
    }

    /**
     * Function to be called when the user presses "Start".
     * Closes all previous connections if present and attempts to connect to the
     * IP addresses in blackboard.
     * <p>
     * Allows process to continue if the Emotion client is unsuccessful, but not if the
     * Eye Tracking client is unsuccessful
     *
     * @param eyeTrackingPort
     * @param emotionPort
     * @throws IOException
     */
    public void connectClients(int eyeTrackingPort, int emotionPort) throws IOException {
        cleanUpThreads();
        CustomThread eyeTrackingThread = new Alt_EyeTrackingClient(
                                                Blackboard.getInstance().getEyeTrackingSocket_Host(),
                                                eyeTrackingPort);
        CustomThread emotionThread = new Alt_EmotionDataClient(
                                                Blackboard.getInstance().getEmotionSocket_Host(),
                                                emotionPort);
        CustomThread dataProcessor = new Alt_DataProcessor();

        threads.add(eyeTrackingThread);
        threads.add(emotionThread);
        threads.add(dataProcessor);
        for (CustomThread thread : threads){thread.start();}

        Thread observerThread = new Thread(new Observer());
        observerThread.start();

    }

    public void cleanUpThreads(){
        for (CustomThread thread: threads){
            if (thread != null) {
                thread.stopThread();
            }
        }
        threads.clear();
    }

    private void startServerThreads(){
        System.out.println("Starting test servers.");
        Thread emotionServerThread = new Thread(new EmotionDataServer());
        Thread eyeTrackingThread = new Thread(new EyeTrackingServer());

        emotionServerThread.start();
        eyeTrackingThread.start();
    }


}
