package DataClients;

import Model.Blackboard;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class Alt_EyeTrackingClient extends Alt_ClientThread {

    public static final String THREAD_NAME = "EyeTrackingClient";
    public Alt_EyeTrackingClient(String host, int port){
        super(host, port);
        super.setLog(Logger.getLogger(Alt_EmotionDataClient.class.getName()));
        super.setThreadName(THREAD_NAME);
    }

    @Override
    public void doYourWork() throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis();
        String str = super.getInputStream().readUTF();
        Blackboard.getInstance().addToEyeTrackingQueue(str);
        long endTime = System.currentTimeMillis();
        super.getLog().info("Received eye tracking data: " + str + " in " + (endTime - startTime) + "ms");
    }

}
