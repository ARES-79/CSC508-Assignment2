package Model.DataClients;


import Model.Blackboard;
import Model.ClientThread;

import java.io.IOException;
import java.util.logging.Logger;

public class EmotionDataClient extends ClientThread {

    public static final String THREAD_NAME = "EmotionDataClient";
    public EmotionDataClient(String host, int port){
        super(host, port);
        super.setLog(Logger.getLogger(EmotionDataClient.class.getName()));
        super.setThreadName(THREAD_NAME);
    }
    @Override
    public void doYourWork() throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis();
        String str = super.getInputStream().readUTF();
        Blackboard.getInstance().addToEmotionQueue(str);
        long endTime = System.currentTimeMillis();
        super.getLog().info("Received emotion data: " + str + " in " + (endTime - startTime) + "ms");
    }
}
