package View;

import Controller.MainController;
import DataClients.*;
import Model.*;
import TestServers.EmotionDataServer;
import TestServers.EyeTrackingServer;

import java.awt.*;
import java.beans.PropertyChangeListener;
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

        DrawPanel drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);
    }

    /**
     * Function to be called when the user presses "Start".
     * Closes all previous connections if present.
     * Attempts to connect to the sever IP addresses in blackboard.
     * Creates and starts all necessary threads.
     *
     *
     * @param eyeTrackingPort port for IP of eye tracking server as int
     * @param emotionPort port for IP of emotion server as int
     */
    public void connectClients(int eyeTrackingPort, int emotionPort) {
        cleanUpThreads();
        CustomThread eyeTrackingThread = new Alt_EyeTrackingClient(
                                                Blackboard.getInstance().getEyeTrackingSocket_Host(),
                                                eyeTrackingPort);
        CustomThread emotionThread = new Alt_EmotionDataClient(
                                                Blackboard.getInstance().getEmotionSocket_Host(),
                                                emotionPort);
        CustomThread dataProcessor = new Alt_DataProcessor();
        DrawPanelDelegate dpDelegate = new DrawPanelDelegate();

        threads.add(eyeTrackingThread);
        threads.add(emotionThread);
        threads.add(dataProcessor);
        threads.add(dpDelegate);
        for (CustomThread thread : threads){thread.start();}
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
