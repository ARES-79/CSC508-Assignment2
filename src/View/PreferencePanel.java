package View;

import Model.Blackboard;
import java.awt.*;
import javax.swing.*;

/**
 * The {@code PreferencePanel} class represents a configuration panel that allows users to modify 
 * server connection settings, display parameters, and system behavior. This panel provides fields 
 * for setting IP addresses, ports, the maximum number of circles displayed, and the threshold radius 
 * for circle consolidation.
 * 
 * When the "Apply" button is pressed, the updated values are applied to the {@link Blackboard},
 * and the display is refreshed. If the system is actively running, it will restart data retrieval
 * based on the new settings.
 */
public class PreferencePanel extends JPanel {

    private JTextField emotionIpField;
    private JTextField emotionPortField;
    private JTextField eyeTrackingIpField;
    private JTextField eyeTrackingPortField;
    private JTextField maxCirclesField;
    private JTextField thresholdRadiusField;

    /**
     * Constructs a {@code PreferencePanel} and initializes it with the current configuration values
     * from the {@link Blackboard}. It creates fields for modifying emotion server and eye-tracking server
     * connection details, along with parameters for visual display settings.
     */
    public PreferencePanel() {
        setPreferredSize(new Dimension(1000, 150));  // Set the size of the panel
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);  // Set smaller padding between components

        // Initialize fields with existing values from Blackboard
        Blackboard blackboard = Blackboard.getInstance();

        // Emotion Server IP
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Emotion Server IP:"), gbc);
        emotionIpField = new JTextField(blackboard.getEmotionServerIp(), 10);  // Autofill with current value
        gbc.gridx = 1;
        add(emotionIpField, gbc);

        // Emotion Server Port
        gbc.gridx = 2;
        add(new JLabel("Emotion Server Port:"), gbc);
        emotionPortField = new JTextField(String.valueOf(blackboard.getEmotionServerPort()), 10);  // Autofill with current value
        gbc.gridx = 3;
        add(emotionPortField, gbc);

        // Eye Tracking Server IP
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Eye Tracking Server IP:"), gbc);
        eyeTrackingIpField = new JTextField(blackboard.getEyeTrackingServerIp(), 10);  // Autofill with current value
        gbc.gridx = 1;
        add(eyeTrackingIpField, gbc);

        // Eye Tracking Server Port
        gbc.gridx = 2;
        add(new JLabel("Eye Tracking Server Port:"), gbc);
        eyeTrackingPortField = new JTextField(String.valueOf(blackboard.getEyeTrackingServerPort()), 10);  // Autofill with current value
        gbc.gridx = 3;
        add(eyeTrackingPortField, gbc);

        // Max Circles
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Max Circles:"), gbc);
        maxCirclesField = new JTextField(String.valueOf(blackboard.getMaxCircles()), 10);  // Autofill with current value
        gbc.gridx = 1;
        add(maxCirclesField, gbc);

        // Threshold Radius
        gbc.gridx = 2;
        add(new JLabel("Threshold Radius:"), gbc);
        thresholdRadiusField = new JTextField(String.valueOf(blackboard.getThresholdRadius()), 10);  // Autofill with current value
        gbc.gridx = 3;
        add(thresholdRadiusField, gbc);

        // Apply Button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applyChanges());
        add(applyButton, gbc);
    }

    /**
     * Applies the changes entered in the preference fields to the {@link Blackboard}.
     * 
     * This method retrieves values from the input fields, validates them, and updates the corresponding
     * settings in the {@code Blackboard}. It clears existing circles on the {@link DrawPanel}, repaints the
     * display, and, if the system is running, restarts data retrieval with the updated settings.
     */
    private void applyChanges() {
        try {
            // Update Blackboard with new values
            Blackboard blackboard = Blackboard.getInstance();

            blackboard.setEmotionServerIp(emotionIpField.getText());
            blackboard.setEmotionServerPort(Integer.parseInt(emotionPortField.getText()));
            blackboard.setEyeTrackingServerIp(eyeTrackingIpField.getText());
            blackboard.setEyeTrackingServerPort(Integer.parseInt(eyeTrackingPortField.getText()));

            int maxCircles = Integer.parseInt(maxCirclesField.getText());
            int thresholdRadius = Integer.parseInt(thresholdRadiusField.getText());

            blackboard.setMaxCircles(maxCircles);
            blackboard.setThresholdRadius(thresholdRadius);

            blackboard.getCircleList().clear();  // Clear existing circles
            blackboard.getDrawPanel().repaint();  // Repaint the draw panel

            // If running, restart data retrieval
            if (blackboard.getStartFlag()) {
                try {
                    System.out.println("Restarting data retrieval...");
                    blackboard.stopDataRetrieval();
                    Thread.sleep(500);
                    blackboard.startDataRetrieval();
                } catch (Exception e) {
                    System.out.println("Error restarting data retrieval: " + e);
                }
            }

            System.out.println("Settings applied.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ports, Max Circles, and Threshold Radius.");
        }
    }
}
