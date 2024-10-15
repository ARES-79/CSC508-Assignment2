import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class DisplayArea extends JFrame implements Runnable {

    private DrawPanel drawPanel;
    //keep tracking radius the same
    private static final int TRACKING_RADIUS = 50;
    private static final int CIRCLE_RADIUS = 50;
    private int occupied_x = 0;
    private int occupied_y = 0;

    public DisplayArea() {
        setTitle("Eye Tracking & Emotion Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenuItem startMenuItem = new JMenuItem("Start");
        startMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //flip running flag
                System.out.println("Start pressed");
            }
        });
        startMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        menuBar.add(startMenuItem);

        menuBar.add(Box.createHorizontalStrut(10));

        JMenuItem stopMenuItem = new JMenuItem("Stop");
        stopMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //flip running flag
                System.out.println("Stop pressed");
            }
        });
        stopMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        menuBar.add(stopMenuItem);

        menuBar.add(Box.createHorizontalStrut(10));

        JMenuItem generateMenuItem = new JMenuItem("Generate");
        generateMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Generate pressed");
                drawPanel.generateRandomCircle();
            }
        });
        generateMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        menuBar.add(generateMenuItem);

        setJMenuBar(menuBar);

        drawPanel = new DrawPanel();
        drawPanel.setBackground(Color.WHITE);
        //drawPanel.setPreferredSize(new Dimension(900, 900));
        drawPanel.setSize(new Dimension(1000,1000)); // do not prefer size as it will never be respected

        getContentPane().add(drawPanel, BorderLayout.CENTER);
        getContentPane().setLayout(new BorderLayout());

        setSize(1000, 1000);  
        setLocationRelativeTo(null); // center on screen
        setVisible(true); 
    }

    @Override
    public void run() {
        setVisible(true);
    }

    // Circle drawing panel
    class DrawPanel extends JPanel {
        private int x, y, radius;
        private Color color;
        private Random random = new Random();

        public DrawPanel() {
            System.out.println("DrawPanel created");
        }

        public void generateRandomCircle() {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            if (panelWidth > 0 && panelHeight > 0) {
                // Random size between 1 and 100
                radius = random.nextInt(100) + 1;

                x = random.nextInt(panelWidth - radius);
                y = random.nextInt(panelHeight - radius);

                color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

                System.out.println("Repainting..."); 

                // usually do not need the entire procedure below but for some reason it is not repainting without
                invalidate();
                revalidate();
                repaint();
            }
        }
        // takes in Emotion, x/y
        // NEUTRAL, FOCUS, STRESS, ENGAGEMENT, EXCITEMENT, INTEREST
        // GRAY, YELLOW, RED, BLUE, GREEN, PURPLE
        public void generateCircle(Emotion e, int x, int y) {
            // take bundle from DataProcessor and draw circle
            // determine if circle already exists at x/y
            // if already in occupied_x +-50 and occupied_y +-50,
            // expand visual radius by 50
            if (occupied_x - TRACKING_RADIUS <= x && x <= occupied_x + TRACKING_RADIUS &&
                occupied_y - TRACKING_RADIUS <= y && y <= occupied_y + TRACKING_RADIUS) {
                // expand visual radius by 50
                radius += 50;
            } else {
                // set new occupied x and y
                occupied_x = x;
                occupied_y = y;
                radius = CIRCLE_RADIUS;
            }
            switch(e) {
                case NEUTRAL:
                    color = Color.GRAY;
                    break;
                case FOCUS:
                    color = Color.YELLOW;
                    break;
                case STRESS:
                    color = Color.RED;
                    break;
                case ENGAGEMENT:
                    color = Color.BLUE;
                    break;
                case EXCITEMENT:
                    color = Color.GREEN;
                    break;
                case INTEREST:
                    color = Color.MAGENTA;
                    break;
                default:
                    // default to black
                    color = Color.BLACK;
            }
            System.out.println("Repainting...");
            invalidate();
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            System.out.println("paintComponent called");
            if (color != null) {
                g.setColor(color);
                g.fillOval(x, y, radius, radius);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new DisplayArea());
    }
}
