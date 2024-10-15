package DataClients;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmotionDataClient implements Runnable {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 6000;
    private final BlockingQueue<String> emotionQueue;
    private static final Logger emotionClientLog = Logger.getLogger(EmotionDataClient.class.getName());

    public EmotionDataClient(BlockingQueue<String> queue) {
        this.emotionQueue = queue;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(IP_ADDRESS, PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())){

            while(true){
                // expecting 5 floats between 0 and 1
                String str = inputStream.readUTF();
                emotionQueue.put(str);
            }

        } catch (InterruptedException e) {
            emotionClientLog.log(Level.SEVERE, "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch(Exception e){
            emotionClientLog.warning(e.toString());
        }
    }
}
