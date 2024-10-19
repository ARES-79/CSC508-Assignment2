package Controller;

import Model.Blackboard;
import View.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * The {@code MainController} class serves as an event handler for UI actions, implementing
 * the {@link ActionListener} interface. It handles "Start" and "Stop" actions by interacting
 * with the {@link Blackboard} to start and stop data retrieval.
 *
 * This controller listens for user actions (such as button clicks) and triggers the appropriate
 * data retrieval methods in the {@code Blackboard} instance based on the action command.
 */
public class MainController implements ActionListener {

    private final Main parent;

    private static final Logger controllerLog = Logger.getLogger(MainController.class.getName());

    public MainController(Main parent) {
        this.parent = parent;
    }

    /**
     * Responds to user actions from the UI by processing the action command.
     *
     * If the "Start" action is triggered, the method calls {@link Blackboard#startDataRetrieval()}
     * to start data retrieval. If the "Stop" action is triggered, it calls
     * {@link Blackboard#stopDataRetrieval()} to stop data retrieval.
     *
     * @param e the {@link ActionEvent} containing the action command
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ("Start") -> {
                controllerLog.info(String.format("Connection attempted with:\n%s",
                        Blackboard.getInstance().getFormattedConnectionSettings()));
                parent.connectClients();
            }
            case ("Stop") -> {
                controllerLog.info("Stop Pressed. Disconnecting.");
                parent.cleanUpThreads();
            }
        }
    }
}
