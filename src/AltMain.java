import DataClients.EmotionDataClient;
import DataClients.EyeTrackingClient;
import Model.Blackboard;
import Model.DataProcessor;
import Model.Observer;
import View.DrawPanel;

import javax.swing.*;
import java.awt.*;

public class AltMain extends JFrame {

    public static void main (String[] args){
        AltMain window = new AltMain();
        window.setSize(1000,1000); // center on screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        EmotionDataClient emotionDataClient = new EmotionDataClient();
        Thread emotionThread = new Thread(emotionDataClient);

        EyeTrackingClient eyeTrackingClient = new EyeTrackingClient();
        Thread eyeTrackingThread = new Thread(eyeTrackingClient);

        DataProcessor dataProcessor = new DataProcessor();
        Thread dataThread = new Thread(dataProcessor);

        Observer observer = new Observer();
        Thread observerThread = new Thread(observer);

        emotionThread.start();
        eyeTrackingThread.start();
        dataThread.start();
        observerThread.start();
    }


}
