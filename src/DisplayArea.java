import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class DisplayArea extends JFrame implements Runnable {

    private DrawPanel drawPanel;

    public DisplayArea() {
        setTitle("Eye Tracking & Emotion Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenuItem startMenuItem = new JMenuItem("Start");
        startMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Start pressed");
            }
        });
        startMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        menuBar.add(startMenuItem);

        menuBar.add(Box.createHorizontalStrut(10));

        JMenuItem stopMenuItem = new JMenuItem("Stop");
        stopMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        private int x, y, width, height;
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
                width = random.nextInt(100) + 1;
                height = random.nextInt(100) + 1;

                x = random.nextInt(panelWidth - width);
                y = random.nextInt(panelHeight - height);

                color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

                System.out.println("Repainting..."); 

                // usually do not need the entire procedure below but for some reason it is not repainting without
                invalidate();
                revalidate();
                repaint();
            }
        }
        
        public void generateCircle() {
             // take bundle from DataProcessor and draw circle
             
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            System.out.println("paintComponent called");
            if (color != null) {
                g.setColor(color);
                g.fillOval(x, y, width, height);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new DisplayArea());
    }
}
