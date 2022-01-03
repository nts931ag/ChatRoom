import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 03/01/2022 - 7:37 CH
 * Description: ...
 */
public class Server {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket = this.serverSocket.accept();
                System.out.println("A new client has connected!");
                HandleConnection connection = new HandleConnection(socket);
                Thread thread = new Thread(connection);
                thread.start();
            }
        }catch(IOException e){
            closeServer();
        }
    }

    public void closeServer(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(5555);
            Server server = new Server(ss);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
