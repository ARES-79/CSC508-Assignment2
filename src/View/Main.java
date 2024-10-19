package View;

import Controller.MainController;
import Model.DataClients.*;
import Model.*;
import TestServers.EmotionDataServer;
import TestServers.EyeTrackingServer;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.*;

public class Main extends JFrame implements PropertyChangeListener {

    private static final String TESTING_FLAG = "-test";

    private final ArrayList<CustomThread> threads;

    public static void main (String[] args){
        Main window = new Main();
        window.setSize(1000,1000); // center on screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (args.length > 0 && args[0].equals(TESTING_FLAG)){
            System.out.println(args[0]);
            window.startServerThreads();
        }
    }
    public Main() {
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

        //listen for errors in the client threads
        Blackboard.getInstance().addChangeSupportListener(Blackboard.PROPERTY_NAME_EYETHREAD_ERROR, this);
        Blackboard.getInstance().addChangeSupportListener(Blackboard.PROPERTY_NAME_EMOTIONTHREAD_ERROR, this);
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
        CustomThread eyeTrackingThread = new EyeTrackingClient(
                                                Blackboard.getInstance().getEyeTrackingSocket_Host(),
                                                eyeTrackingPort);
        CustomThread emotionThread = new EmotionDataClient(
                                                Blackboard.getInstance().getEmotionSocket_Host(),
                                                emotionPort);
        CustomThread dataProcessor = new DataProcessor();
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

    public void createConnectionErrorPopUp(String main_message, String error_message){
        JOptionPane.showMessageDialog(this,
                String.format("%s\n\n%s\nError: %s", main_message,
                        Blackboard.getInstance().getFormattedConnectionSettings(),
                        error_message));
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()){
            //disconnect everything if we can't connect to the eye tracking server
            case Blackboard.PROPERTY_NAME_EYETHREAD_ERROR -> {
                cleanUpThreads();
                createConnectionErrorPopUp("Unable to connect to Eye Tracking server. \n" +
                        "Please check that the server is running and the IP address is correct.", (String) evt.getNewValue());
            }
            //run without emotion data otherwise
            case Blackboard.PROPERTY_NAME_EMOTIONTHREAD_ERROR ->
                createConnectionErrorPopUp("Unable to connect to Emotion server. \n" +
                        "Application will run without emotion data.", (String) evt.getNewValue());
        }
    }
}
