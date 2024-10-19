package TestServers;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class EmotionDataServer implements Runnable{

    public static void main(String[] args) {
        System.out.println("Emotion Server Main");
        Random random = new Random();
        try (ServerSocket ss = new ServerSocket(6000);
             Socket connection = ss.accept();
             DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());) {
            System.out.println("Emotion Connection Made");

            while (true) {
                long startTime = System.currentTimeMillis();
                float v1 = random.nextFloat();
                float v2 = random.nextFloat();
                float v3 = random.nextFloat();
                float v4 = random.nextFloat();
                float v5 = random.nextFloat();
                outputStream.writeUTF(v1 + ", " + v2 + ", " + v3 + ", " + v4 + ", " + v5);
                outputStream.flush();  // Ensure each packet is sent immediately
                long endTime = System.currentTimeMillis();
                System.out.println("Sent emotion data: " + v1 + ", " + v2 + ", " + v3 + ", " + v4 + ", " + v5 + " in " + (endTime - startTime) + "ms");
                Thread.sleep(500);  // Send data every 0.5 seconds
            }

        } catch (Exception e) {
            System.out.println("Emotion Sever: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        main(new String[0]);
    }
}
