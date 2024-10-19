package DataClients;

import Model.Blackboard;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code EyeTrackingClient} class is responsible for connecting to the eye-tracking data server,
 * receiving eye-tracking data, and adding it to the {@link Blackboard} for further processing.
 * 
 * This class implements {@link Runnable} and is designed to run as a separate thread.
 * It continuously receives eye-tracking data while the system is running and stores the data in a queue
 * for processing.
 */
public class EyeTrackingClient implements Runnable {
    private static final Logger eyeTrackingClientLog = Logger.getLogger(EyeTrackingClient.class.getName());

    /**
     * Connects to the eye-tracking server, continuously reads eye-tracking data, and stores it in the
     * {@link Blackboard} eye-tracking queue.
     * 
     * This method runs in an infinite loop, only processing data when the {@code startFlag} 
     * in the {@link Blackboard} is set to {@code true}. The eye-tracking data is read from the server 
     * via a {@link Socket}, then added to the {@code Blackboard}'s eye-tracking queue for processing.
     * 
     * If the thread is interrupted or an exception occurs while receiving data, appropriate 
     * error handling and logging will take place.
     */
    @Override
    public void run() {
        Blackboard blackboard = Blackboard.getInstance();
        String ipAddress = blackboard.getEyeTrackingServerIp();
        int port = blackboard.getEyeTrackingServerPort();

        try (Socket socket = new Socket(ipAddress, port);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

            while (true) {
                while (blackboard.getStartFlag()) {
                    long startTime = System.currentTimeMillis();
                    String str = inputStream.readUTF();
                    Blackboard.getInstance().addToEyeTrackingQueue(str);
                    long endTime = System.currentTimeMillis();
                    eyeTrackingClientLog.info("Received eye tracking data: " + str + " in " + (endTime - startTime) + "ms");
                }
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            eyeTrackingClientLog.log(Level.SEVERE, "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            eyeTrackingClientLog.warning(e.toString());
        }
    }
}
