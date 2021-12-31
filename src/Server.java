import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 28/12/2021 - 4:48 CH
 * Description: ...
 */
public class Server {
    ServerSocket server;
    ArrayList<Socket> lstSocket;

    Server(){
        try {
            server = new ServerSocket(3200);
            lstSocket = new ArrayList<Socket>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer(){
        try{
            do {
                System.out.println("Waiting for a client");
                Socket ss = server.accept();
                lstSocket.add(ss);
                ClientConnectionThread cct=new ClientConnectionThread(ss, lstSocket);
                cct.start();
            }while(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Server s = new Server();
        s.startServer();
    }
}

