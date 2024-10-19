package DataClients;

import Model.Blackboard;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmotionDataClient implements Runnable {
    private static final Logger emotionClientLog = Logger.getLogger(EmotionDataClient.class.getName());

    @Override
    public void run() {
        Blackboard blackboard = Blackboard.getInstance();
        String ipAddress = blackboard.getEmotionServerIp();
        int port = blackboard.getEmotionServerPort();

        try (Socket socket = new Socket(ipAddress, port);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

            while (true) {
                while (blackboard.getStartFlag()) {
                    long startTime = System.currentTimeMillis();
                    String str = inputStream.readUTF();
                    blackboard.addToEmotionQueue(str);
                    long endTime = System.currentTimeMillis();
                    emotionClientLog.info("Received emotion data: " + str + " in " + (endTime - startTime) + "ms");
                }
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            emotionClientLog.log(Level.SEVERE, "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            emotionClientLog.warning(e.toString());
        }
    }
}
