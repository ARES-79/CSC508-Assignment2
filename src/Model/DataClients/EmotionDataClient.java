package Model.DataClients;


import Model.Blackboard;
import Model.ClientThread;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * The {@code EmotionDataClient} class is responsible for connecting to the emotion data server,
 * receiving emotion data, and adding it to the {@link Blackboard} for further processing.
 * <p>
 * This class extends {@link ClientThread} and is designed to run as a separate thread.
 * It continuously receives emotion data while the system is running and stores the data in a queue
 * for processing.
 */
public class EmotionDataClient extends ClientThread {

    public static final String THREAD_NAME = "EmotionDataClient";
    public EmotionDataClient(String host, int port){
        super(host, port);
        super.setLog(Logger.getLogger(EmotionDataClient.class.getName()));
        super.setThreadName(THREAD_NAME);
    }

    /**
     * The emotion data from the server via the {@link java.io.DataInputStream},
     * is added to the {@code Blackboard}'s emotion queue for processing.
     *
     */
    @Override
    public void doYourWork() throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis();
        String str = super.getInputStream().readUTF();
        Blackboard.getInstance().addToEmotionQueue(str);
        long endTime = System.currentTimeMillis();
        super.getLog().info("Received emotion data: " + str + " in " + (endTime - startTime) + "ms");
    }
}
