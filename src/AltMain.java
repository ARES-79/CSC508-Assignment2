import DataClients.EmotionDataClient;
import DataClients.EyeTrackingClient;
import Model.Blackboard;
import Model.DataProcessor;
import Model.Observer;
import TestServers.EmotionDataServer;
import TestServers.EyeTrackingServer;
import View.DrawPanel;
import java.awt.*;
import javax.swing.*;

public class AltMain extends JFrame {

    private static final String TESTING_FLAG = "-test";
    public static void main (String[] args){
        AltMain window = new AltMain();
        window.setSize(1000,1000); // center on screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (args.length > 0 && args[0].equals(TESTING_FLAG)){
            window.startServerThreads();
        }
        window.startAllThreads();
    }
    public AltMain() {
        super("Eye Tracking & Emotion Hub");
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu actionsMenu = new JMenu("Actions");
        JMenuItem start = new JMenuItem("Start");
        JMenuItem stop = new JMenuItem("Stop");

        menuBar.add(actionsMenu);
        actionsMenu.add(start);
        actionsMenu.add(stop);
        setJMenuBar(menuBar);

        DrawPanel drawPanel = Blackboard.getInstance().getDrawPanel();
        add(drawPanel, BorderLayout.CENTER);
    }

    private void startAllThreads() {

        Thread emotionThread = new Thread(new EmotionDataClient());
        Thread eyeTrackingThread = new Thread(new EyeTrackingClient());
        Thread dataThread = new Thread(new DataProcessor());
        Thread observerThread = new Thread(new Observer());

        emotionThread.start();
        eyeTrackingThread.start();
        dataThread.start();
        observerThread.start();
    }

    private void startServerThreads(){
        Thread emotionServerThread = new Thread(new EmotionDataServer());
        Thread eyeTrackingThread = new Thread(new EyeTrackingServer());

        emotionServerThread.start();
        eyeTrackingThread.start();
    }


}
