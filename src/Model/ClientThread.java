package Model;

import Model.DataClients.EmotionDataClient;
import Model.DataClients.EyeTrackingClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Establishes data and functions necessary for client threads.
 */
public abstract class ClientThread extends CustomThread {

    private final String IP_host;
    private final int IP_port;
    private Socket connection;
    private DataInputStream inputStream;

    public ClientThread(String IP_host, int IP_port) {
        this.IP_host = IP_host;
        this.IP_port = IP_port;
    }

    /**
     * Connect socket and perform work
     */
    @Override
    public void run(){
        try(Socket connection = new Socket(IP_host, IP_port);
            DataInputStream inputStream= new DataInputStream(connection.getInputStream())){
            this.connection = connection;
            this.inputStream = inputStream;
            super.run();
        } catch (IOException ex){
            switch (super.getThreadName()){
                case EmotionDataClient.THREAD_NAME -> Blackboard.getInstance().reportEmotionThreadError(ex.getMessage());
                case EyeTrackingClient.THREAD_NAME -> Blackboard.getInstance().reportEyeThreadError(ex.getMessage());
            }
            super.getLog().log(Level.SEVERE, super.getThreadName() + ": Unable to connect to server.");
        }

    }

    /**
     * Clean up all resources
     */
    @Override
    public void cleanUpThread(){
        try{ // Try to clean up everything outstanding
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }
}
