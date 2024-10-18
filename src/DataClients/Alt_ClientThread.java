package DataClients;

import Model.Blackboard;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

public abstract class Alt_ClientThread extends CustomThread {

    private final String IP_host;
    private final int IP_port;
    private Socket connection;
    private DataInputStream inputStream;

    public Alt_ClientThread(String IP_host, int IP_port) {
        this.IP_host = IP_host;
        this.IP_port = IP_port;
    }

    @Override
    public void run(){
        try(Socket connection = new Socket(IP_host, IP_port);
            DataInputStream inputStream= new DataInputStream(connection.getInputStream())){
            this.connection = connection;
            this.inputStream = inputStream;
            super.run();
        } catch (IOException ex){
            switch (super.getThreadName()){
                case Alt_EmotionDataClient.THREAD_NAME -> Blackboard.getInstance().reportEmotionThreadError(ex.getMessage());
                case Alt_EyeTrackingClient.THREAD_NAME -> Blackboard.getInstance().reportEyeThreadError(ex.getMessage());
            }
            super.getLog().log(Level.SEVERE, super.getThreadName() + ": Unable to connect to server.");
        }

    }

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
