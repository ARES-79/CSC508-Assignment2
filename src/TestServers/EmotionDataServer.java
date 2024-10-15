package TestServers;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class EmotionDataServer {

    public static void main(String[] args){
        Random random = new Random();
        try(ServerSocket ss = new ServerSocket(6000);
            Socket connection = ss.accept();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())){

            int i = 0;
            while(true){
                float v1 = random.nextFloat();
                float v2 = random.nextFloat();
                float v3 = random.nextFloat();
                float v4 = random.nextFloat();
                float v5 = random.nextFloat();
                //outputStream.writeUTF("0.1023334, 0.321731984, 0.993213, 0.342352, 0.7851349");
                outputStream.writeUTF(v1 + ", " + v2 + ", " + v3 + ", " + v4 + ", " + v5);
                if (i % 5 == 0){
                    outputStream.flush();
                }
                Thread.sleep(600);
                i++;
            }

        } catch (Exception e){System.out.println(e.getMessage());}
    }
}
