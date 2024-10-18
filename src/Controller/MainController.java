package Controller;

import Model.Blackboard;
import View.AltMain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

public class MainController implements ActionListener {

    private final AltMain parent;

    private static final Logger controllerLog = Logger.getLogger(MainController.class.getName());

    public MainController(AltMain parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ("Start") -> {
                controllerLog.info(String.format("Connection attempted with:\n " +
                        "\tEye Tracking Socket IP: %s" +
                        "\tEmotion Tracking Socket IP: %s",
                        Blackboard.getInstance().getEyeTrackingSocket_Host() + ":" +Blackboard.getInstance().getEyeTrackingSocket_Port(),
                        Blackboard.getInstance().getEmotionSocket_Host() + ":" +Blackboard.getInstance().getEmotionSocket_Port()));
                try{
                    int eyeTracking_port = Integer.parseInt(Blackboard.getInstance().getEyeTrackingSocket_Port());
                    int emotion_port = Integer.parseInt(Blackboard.getInstance().getEmotionSocket_Port());
                    parent.connectClients(eyeTracking_port, emotion_port);
                } catch (NumberFormatException exception) {
                    controllerLog.warning("Invalid Ports.");
                    JOptionPane.showMessageDialog(parent, "Invalid Ports - ports must be integers.");
                }
                /*catch (IOException ex) {
                    controllerLog.warning("Unable to connect to servers.");
                    JOptionPane.showMessageDialog(parent, "Unable to connect to servers.");
                }*/

            }
            case ("Stop") -> {
                controllerLog.info("Stop Pressed. Disconnecting.");
                parent.cleanUpThreads();
            }
        }
    }
}
