package DataClients;

import Model.Blackboard;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code EmotionDataClient} class is responsible for connecting to the emotion data server,
 * receiving emotion data, and adding it to the {@link Blackboard} for further processing.
 * 
 * This class implements {@link Runnable} and is designed to run as a separate thread.
 * It continuously receives emotion data while the system is running and stores the data in a queue
 * for processing.
 */
public class EmotionDataClient implements Runnable {
    private static final Logger emotionClientLog = Logger.getLogger(EmotionDataClient.class.getName());

    /**
     * Connects to the emotion server, continuously reads emotion data, and stores it in the
     * {@link Blackboard} emotion queue.
     * 
     * This method runs in an infinite loop, only processing data when the {@code startFlag} 
     * in the {@link Blackboard} is set to {@code true}. The emotion data is read from the server 
     * via a {@link Socket}, then added to the {@code Blackboard}'s emotion queue for processing.
     * 
     * If the thread is interrupted or an exception occurs while receiving data, appropriate 
     * error handling and logging will take place.
     */
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
