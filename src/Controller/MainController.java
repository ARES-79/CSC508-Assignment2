package Controller;

import Model.Blackboard;
import View.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class MainController implements ActionListener {

    private final Main parent;

    private static final Logger controllerLog = Logger.getLogger(MainController.class.getName());

    public MainController(Main parent) {
        this.parent = parent;
    }

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
