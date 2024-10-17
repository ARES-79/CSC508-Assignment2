package TestServers;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class EyeTrackingServer {

    public static void main(String[] args) {
        Random random = new Random();
        try (ServerSocket ss = new ServerSocket(6001);
             Socket connection = ss.accept();
             DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {

            while (true) {
                int x_pos = random.nextInt(1000);
                int y_pos = random.nextInt(1000);
                outputStream.writeUTF(x_pos + ", " + y_pos);
                outputStream.flush();  // Ensure each packet is sent immediately
                Thread.sleep(500);  // Send data every 0.5 seconds
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
