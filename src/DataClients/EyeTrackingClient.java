package DataClients;

import java.io.DataInputStream;
import java.net.Socket;

public class EyeTrackingClient implements Runnable {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 6001;
    public static void main (String[] args){
        try (Socket socket = new Socket(IP_ADDRESS, PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())){

            while(true){
                // expecting 2 integers
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
