package DataClients;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EyeTrackingClient implements Runnable {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 6001;
    private final BlockingQueue<String> eyeTrackingQueue;
    private static final Logger eyeTrackingClientLog = Logger.getLogger(EyeTrackingClient.class.getName());

    public EyeTrackingClient(BlockingQueue<String> queue){
        this.eyeTrackingQueue = queue;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(IP_ADDRESS, PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())){

            while(true){
                // expecting 2 integers
                String str = inputStream.readUTF();
                eyeTrackingQueue.put(str);
            }

        } catch (InterruptedException e) {
            eyeTrackingClientLog.log(Level.SEVERE, "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch(Exception e){
            eyeTrackingClientLog.warning(e.toString());
        }
    }
}
