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

/**
 * The {@code Main} class serves as the entry point for the Eye Tracking & Emotion Hub application.
 * It sets up the main window, initializes the user interface components, and starts the necessary threads
 * for data retrieval, processing, and visualization.
 * <p>
 * The application displays a user interface with a panel for adjusting preferences, a draw panel for
 * visualizing circles representing emotion and eye-tracking data, and a key panel explaining the color-coded emotions.
 * <p>
 * Main also acts as the default factory for necessary components.
 * <p>
 * If run with the "-test" flag, this class will also start the test servers for emotion and eye-tracking data.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 */
public class Main extends JFrame implements PropertyChangeListener {

    private static final String TESTING_FLAG = "-test";

    private final ArrayList<CustomThread> threads;

    /**
     * The main method starts the application, setting up the main window.
     *
     * @param args command-line arguments; if "-test" is passed, test servers are started
     */
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

    /**
     * Constructs the main window of the application, setting up the layout,
     * menu bar, and integrating the preference panel, draw panel, and color key panel.
     */
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
        drawPanel.setPreferredSize(new Dimension(1000,1000));
        add(drawPanel, BorderLayout.CENTER);

        PreferencePanel preferencePanel = new PreferencePanel();
        add(preferencePanel, BorderLayout.NORTH);

        ColorKeyPanel colorKeyPanel = new ColorKeyPanel();
        colorKeyPanel.setPreferredSize(new Dimension(200,1000));
        add(colorKeyPanel, BorderLayout.EAST);

        //listen for errors in the client threads
        Blackboard.getInstance().addChangeSupportListener(Blackboard.PROPERTY_NAME_EYETHREAD_ERROR, this);
        Blackboard.getInstance().addChangeSupportListener(Blackboard.PROPERTY_NAME_EMOTIONTHREAD_ERROR, this);
    }

    /**
     * Function to be called when the user presses "Start".
     * Closes all previous connections if present.
     * Attempts to connect to the sever IP addresses in blackboard.
     * Creates and starts all necessary threads.
     */
    public void connectClients() {
        int eyeTrackingPort = Blackboard.getInstance().getEyeTrackingSocket_Port();
        int emotionPort = Blackboard.getInstance().getEmotionSocket_Port();
        cleanUpThreads();
        CustomThread eyeTrackingThread = new EyeTrackingClient(
                                                Blackboard.getInstance().getEyeTrackingSocket_Host(),
                                                eyeTrackingPort);
        CustomThread emotionThread = new EmotionDataClient(
                                                Blackboard.getInstance().getEmotionSocket_Host(),
                                                emotionPort);
        CustomThread dataProcessor = new RawDataProcessor();
        ViewDataProcessor dpDelegate = new ViewDataProcessor();

        threads.add(eyeTrackingThread);
        threads.add(emotionThread);
        threads.add(dataProcessor);
        threads.add(dpDelegate);
        for (CustomThread thread : threads){thread.start();}
    }

    /**
     * Stop all threads from running.
     * They clean up all resources.
     */
    public void cleanUpThreads(){
        for (CustomThread thread: threads){
            if (thread != null) {
                thread.stopThread();
            }
        }
        threads.clear();
    }

    /**
     * Starts the test servers for emotion data and eye-tracking data.
     * This method is called if the "-test" flag is provided as a command-line argument.
     */
    private void startServerThreads(){
        System.out.println("Starting test servers.");
        Thread emotionServerThread = new Thread(new EmotionDataServer());
        Thread eyeTrackingThread = new Thread(new EyeTrackingServer());

        emotionServerThread.start();
        eyeTrackingThread.start();
    }

    /**
     * Display a pop-up to show error to user.
     *
     * @param main_message Message to be displayed on top
     * @param error_message Message associated with error
     */
    public void createConnectionErrorPopUp(String main_message, String error_message){
        JOptionPane.showMessageDialog(this,
                String.format("%s\n\n%s\nError: %s", main_message,
                        Blackboard.getInstance().getFormattedConnectionSettings(),
                        error_message));
    }

    /**
     * Deal with thread errors as necessary.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
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
