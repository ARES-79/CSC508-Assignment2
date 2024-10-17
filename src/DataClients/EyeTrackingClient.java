package DataClients;

import Model.Blackboard;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EyeTrackingClient implements Runnable {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 6001;
    private static final Logger eyeTrackingClientLog = Logger.getLogger(EyeTrackingClient.class.getName());

    @Override
    public void run() {
        try (Socket socket = new Socket(IP_ADDRESS, PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())){

            while(true){
                while(Blackboard.getInstance().getStartFlag()){
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
        } catch(Exception e){
            eyeTrackingClientLog.warning(e.toString());
        }
    }
}
