package View;

import Controller.MainController;
import Model.Blackboard;
import java.awt.*;
import javax.swing.*;

public class PreferencePanel extends JPanel {

    private final JTextField emotionIpField;
    private final JTextField emotionPortField;
    private final JTextField eyeTrackingIpField;
    private final JTextField eyeTrackingPortField;
    private final JTextField maxCirclesField;
    private final JTextField thresholdRadiusField;

    public PreferencePanel() {
        setPreferredSize(new Dimension(1000, 150));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2); // Set smaller padding

        // Initialize fields with existing values from Blackboard
        Blackboard blackboard = Blackboard.getInstance();

        // Emotion Server IP
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Emotion Server IP:"), gbc);
        emotionIpField = new JTextField(blackboard.getEmotionSocket_Host(), 10); // Autofill with current value
        gbc.gridx = 1;
        add(emotionIpField, gbc);

        // Emotion Server Port
        gbc.gridx = 2;
        add(new JLabel("Emotion Server Port:"), gbc);
        emotionPortField = new JTextField(String.valueOf(blackboard.getEmotionSocket_Port()), 10); // Autofill with current value
        gbc.gridx = 3;
        add(emotionPortField, gbc);

        // Eye Tracking Server IP
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Eye Tracking Server IP:"), gbc);
        eyeTrackingIpField = new JTextField(blackboard.getEyeTrackingSocket_Host(), 10); // Autofill with current value
        gbc.gridx = 1;
        add(eyeTrackingIpField, gbc);

        // Eye Tracking Server Port
        gbc.gridx = 2;
        add(new JLabel("Eye Tracking Server Port:"), gbc);
        eyeTrackingPortField = new JTextField(String.valueOf(blackboard.getEyeTrackingSocket_Port()), 10); // Autofill with current value
        gbc.gridx = 3;
        add(eyeTrackingPortField, gbc);

        // Max Circles
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Max Circles:"), gbc);
        maxCirclesField = new JTextField(String.valueOf(blackboard.getMaxCircles()), 10); // Autofill with current value
        gbc.gridx = 1;
        add(maxCirclesField, gbc);

        // Threshold Radius
        gbc.gridx = 2;
        add(new JLabel("Threshold Radius:"), gbc);
        thresholdRadiusField = new JTextField(String.valueOf(blackboard.getThresholdRadius()), 10); // Autofill with current value
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

    public void applyChanges(){
        try {
            // Update Blackboard with new values
            Blackboard blackboard = Blackboard.getInstance();

            blackboard.setEmotionSocket_Host(emotionIpField.getText());
            blackboard.setEmotionSocket_Port(Integer.parseInt(emotionPortField.getText()));
            blackboard.setEyeTrackingSocket_Host(eyeTrackingIpField.getText());
            blackboard.setEyeTrackingSocket_Port(Integer.parseInt(eyeTrackingPortField.getText()));

            int maxCircles = Integer.parseInt(maxCirclesField.getText());
            int thresholdRadius = Integer.parseInt(thresholdRadiusField.getText());

            blackboard.setMaxCircles(maxCircles);
            blackboard.setThresholdRadius(thresholdRadius);


            System.out.println("Settings applied.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid ints for ports, Max Circles, and Threshold Radius.");
        }
    }
}
