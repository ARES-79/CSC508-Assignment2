package TestServers;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class EyeTrackingServer implements Runnable {

    public static void main(String[] args) {
        System.out.println("Eye Tracking Server Main");
        Random random = new Random();
        try (ServerSocket ss = new ServerSocket(6001);
             Socket connection = ss.accept();
             DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());) {
            System.out.println("Eye Tracking Connection Made");
            while (true) {
                long startTime = System.currentTimeMillis();
                int x_pos = random.nextInt(1000);
                int y_pos = random.nextInt(1000);
                outputStream.writeUTF(x_pos + ", " + y_pos);
                outputStream.flush();  // Ensure each packet is sent immediately
                long endTime = System.currentTimeMillis();
                System.out.println("Sent eye tracking data: " + x_pos + ", " + y_pos + " in " + (endTime - startTime) + "ms");
                Thread.sleep(500);  // Send data every 0.5 seconds
            }

        } catch (Exception e) {
            System.out.println("Eye Tracking Sever: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        main(new String[0]);
    }
}
