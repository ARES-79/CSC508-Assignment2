package View;

import java.awt.*;
import javax.swing.*;

/**
 * The {@code ColorKeyPanel} class represents a panel that displays a key of emotion labels
 * and their corresponding colors. This panel provides a visual reference for users to understand
 * which colors correspond to specific emotions in the graphical interface.
 * Each label is paired with a colored square representing the emotion.
 */
public class ColorKeyPanel extends JPanel {

    /**
     * Constructs a {@code ColorKeyPanel} with a two-column grid layout. One column displays the emotion
     * labels, and the other column displays the corresponding color squares.
     */
    //Todo: change to use emotion enums
    public ColorKeyPanel() {
        setLayout(new GridLayout(0, 2));

        add(createCenteredLabel("None"));
        add(createColorLabel(Color.GRAY));
        
        add(createCenteredLabel("Focus"));
        add(createColorLabel(Color.YELLOW));
        
        add(createCenteredLabel("Stress"));
        add(createColorLabel(Color.RED));
        
        add(createCenteredLabel("Engagement"));
        add(createColorLabel(Color.BLUE));
        
        add(createCenteredLabel("Excitement"));
        add(createColorLabel(Color.GREEN));
        
        add(createCenteredLabel("Interest"));
        add(createColorLabel(Color.MAGENTA));
    }

    /**
     * Creates a centered label for an emotion with the specified text.
     *
     * @param text the text to display in the label
     * @return a {@link JLabel} containing the specified text, centered horizontally
     */
    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Creates a label representing the color for an emotion.
     * The label is a square of size 20x20 with the specified background color.
     *
     * @param color the background color of the label
     * @return a {@link JLabel} with the specified background color and fixed size
     */
    private JLabel createColorLabel(Color color) {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(color);
        label.setPreferredSize(new Dimension(20, 20)); // size of color square
        return label;
    }
}
