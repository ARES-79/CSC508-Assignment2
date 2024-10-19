package TestServers;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class EyeTrackingServer implements Runnable {

    public static void main(String[] args) {
        Random random = new Random();
        try (ServerSocket ss = new ServerSocket(6001);
             Socket connection = ss.accept();
             DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {

            while (true) {
                long startTime = System.currentTimeMillis();
                int x_pos = random.nextInt(1000);
                int y_pos = random.nextInt(850-150); //adjust for vertical padding
                outputStream.writeUTF(x_pos + ", " + y_pos);
                outputStream.flush();  // Ensure each packet is sent immediately
                long endTime = System.currentTimeMillis();
                System.out.println("Sent eye tracking data: " + x_pos + ", " + y_pos + " in " + (endTime - startTime) + "ms");
                Thread.sleep(500);  // Send data every 0.5 seconds
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        main(new String[0]);
    }
}
