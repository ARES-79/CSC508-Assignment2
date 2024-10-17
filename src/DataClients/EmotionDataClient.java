package DataClients;

import Model.Blackboard;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmotionDataClient implements Runnable {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 6000;
    private static final Logger emotionClientLog = Logger.getLogger(EmotionDataClient.class.getName());

    @Override
    public void run() {
        try (Socket socket = new Socket(IP_ADDRESS, PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())){

            while(true){
                while (Blackboard.getInstance().getStartFlag()){
                    long startTime = System.currentTimeMillis();
                    String str = inputStream.readUTF();
                    Blackboard.getInstance().addToEmotionQueue(str);
                    long endTime = System.currentTimeMillis();
                    emotionClientLog.info("Received emotion data: " + str + " in " + (endTime - startTime) + "ms");
                }
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            emotionClientLog.log(Level.SEVERE, "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch(Exception e){
            emotionClientLog.warning(e.toString());
        }
    }
}
