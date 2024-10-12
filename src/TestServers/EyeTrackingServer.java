package TestServers;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EyeTrackingServer {

    public static void main(String[] args){
        try(ServerSocket ss = new ServerSocket(6001);
            Socket connection = ss.accept();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());){
            int i = 0;
            while(true){
                outputStream.writeUTF("50, 50");
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
