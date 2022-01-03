import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 03/01/2022 - 7:45 CH
 * Description: ...
 */
public class HandleConnection implements Runnable {

    public static ArrayList<HandleConnection> listHandleConnection = new ArrayList<>();
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String clientUsername;

    public HandleConnection(Socket socket){
        try{
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = br.readLine();
            listHandleConnection.add(this);
            broadcastMessage("Server: " + clientUsername + " has joined the chat!");
        }catch(IOException e){
            closeEverything(socket, bw, br);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isClosed()){
            try{
                messageFromClient = br.readLine();
                broadcastMessage(messageFromClient);
            }catch (IOException e){
                closeEverything(socket, bw, br);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend){
        for(HandleConnection conn : listHandleConnection){
            try{
                if(!conn.clientUsername.equals(clientUsername)){
                    conn.bw.write(messageToSend);
                    conn.bw.newLine();
                    conn.bw.flush();
                }
            }catch(IOException e){
                closeEverything(socket, bw, br);
            }
        }
    }

    public void removeClient(){
        listHandleConnection.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    public void closeEverything(Socket s, BufferedWriter bw, BufferedReader br){
        removeClient();
        try{
           if(br != null){
               br.close();
           }
           if(bw != null){
               bw.close();
           }
           if(s != null){
               s.close();
           }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
