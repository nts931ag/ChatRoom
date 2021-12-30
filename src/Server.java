import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 28/12/2021 - 4:48 CH
 * Description: ...
 */
public class Server {
    Server(){
        try{
            ServerSocket s = new ServerSocket(3200);
            do {
                System.out.println("Waiting for a client");
                Socket ss = s.accept();
                ClientConnectionThread cct=new ClientConnectionThread(ss);
                cct.start();
            }while(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new Server();
    }
}

