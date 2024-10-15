package TestServers;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class EyeTrackingServer {

    public static void main(String[] args){
        java.util.Random random = new Random();
        try(ServerSocket ss = new ServerSocket(6001);
            Socket connection = ss.accept();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());){
            int i = 0;
            while(true){
                int x_pos = random.nextInt(1000);
                int y_pos = random.nextInt(1000);
                outputStream.writeUTF(x_pos + ", " + y_pos);
                if (i % 5 == 0){
                    outputStream.flush();
                }
                Thread.sleep(500);
                i++;
            }
//            outputStream.close();
//            connection.close();
        } catch (Exception e){System.out.println(e.getMessage());}
    }
}
