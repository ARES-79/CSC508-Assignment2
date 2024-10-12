package DataClients;

import java.io.DataInputStream;
import java.net.Socket;

public class EmotionDataClient implements Runnable {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 6000;
    public static void main (String[] args){
        try (Socket socket = new Socket(IP_ADDRESS, PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())){

            while(true){
                // expecting 5 floats between 0 and 1
                String str = inputStream.readUTF();
                System.out.println(str);
            }

        } catch(Exception e){System.out.println(e.getMessage());}
    }

    @Override
    public void run() {
        main(new String[0]);
    }
}
