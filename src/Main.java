import Controller.MainController;
import DataClients.EmotionDataClient;
import DataClients.EyeTrackingClient;
import Model.Blackboard;
import Model.DataProcessor;
import Model.Observer;
import TestServers.EmotionDataServer;
import TestServers.EyeTrackingServer;
import View.ColorKeyPanel;
import View.DrawPanel;
import View.PreferencePanel;
import java.awt.*;
import javax.swing.*;

/**
 * The {@code Main} class serves as the entry point for the Eye Tracking & Emotion Hub application.
 * It sets up the main window, initializes the user interface components, and starts the necessary threads
 * for data retrieval, processing, and visualization.
 * 
 * The application displays a user interface with a panel for adjusting preferences, a draw panel for
 * visualizing circles representing emotion and eye-tracking data, and a key panel explaining the color-coded emotions.
 * 
 * If run with the "-test" flag, this class will also start the test servers for emotion and eye-tracking data.
 */
public class Main extends JFrame {

    private static final String TESTING_FLAG = "-test";

    /**
     * The main method starts the application, setting up the main window and initiating
     * the necessary background threads for data retrieval and processing.
     * 
     * @param args command-line arguments; if "-test" is passed, test servers are started
     */
    public static void main (String[] args) {
        Main window = new Main();
        window.setSize(1200,1000);  // Set the size of the window
        window.setLocationRelativeTo(null);  // Center the window on the screen
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start server threads if testing flag is provided
        if (args.length > 0 && args[0].equals(TESTING_FLAG)) {
            window.startServerThreads();
        }

        // Start all necessary threads for data processing and visualization
        window.startAllThreads();
    }

    /**
     * Constructs the main window of the application, setting up the layout,
     * menu bar, and integrating the preference panel, draw panel, and color key panel.
     */
    public Main() {
        super("Eye Tracking & Emotion Hub");
        setLayout(new BorderLayout());

        // Create and configure the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu actionsMenu = new JMenu("Actions");
        JMenuItem start = new JMenuItem("Start");
        JMenuItem stop = new JMenuItem("Stop");

        menuBar.add(actionsMenu);
        actionsMenu.add(start);
        actionsMenu.add(stop);
        setJMenuBar(menuBar);

        // Attach event listeners to start/stop buttons
        MainController controller = new MainController();
        start.addActionListener(controller);
        stop.addActionListener(controller);

        // Add the preference panel at the top of the window
        PreferencePanel preferencePanel = new PreferencePanel();
        add(preferencePanel, BorderLayout.NORTH);

        // Add the draw panel to the center of the window
        DrawPanel drawPanel = Blackboard.getInstance().getDrawPanel();
        drawPanel.setPreferredSize(new Dimension(1000,1000));
        add(drawPanel, BorderLayout.CENTER);

        // Add the color key panel to the right side of the window
        ColorKeyPanel colorKeyPanel = new ColorKeyPanel();
        colorKeyPanel.setPreferredSize(new Dimension(200,1000));
        add(colorKeyPanel, BorderLayout.EAST);
    }

    /**
     * Starts all necessary threads for handling emotion data, eye-tracking data,
     * data processing, and observing/displaying the results.
     */
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

    /**
     * Starts the test servers for emotion data and eye-tracking data.
     * This method is called if the "-test" flag is provided as a command-line argument.
     */
    private void startServerThreads() {
        Thread emotionServerThread = new Thread(new EmotionDataServer());
        Thread eyeTrackingThread = new Thread(new EyeTrackingServer());

        emotionServerThread.start();
        eyeTrackingThread.start();
    }
}
