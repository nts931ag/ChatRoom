import javax.swing.plaf.basic.BasicButtonUI;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 05/01/2022 - 12:13 CH
 * Description: ...
 */
public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> lstClientHandler = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientUsername;
    private boolean isLogin = false;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.clientUsername = bufferedReader.readLine();
//            lstClientHandler.add(this);
//            broadcastMessage("Server: " + this.clientUsername +" has joined the chat!");
        }catch (IOException e){
            closeEverything(socket, bufferedWriter ,bufferedReader);
        }
    }

    private void broadcastMessage(String messageToSend) {
        for(ClientHandler clientHandler : lstClientHandler){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch(IOException e){
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    public void removeClientHandler(){
        lstClientHandler.remove(this);
        broadcastMessage("Server: " + clientUsername +" has left the chat!");
    }

    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClientHandler();
        try{
            if(socket !=null){
                socket.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        String[] tokens;

        while(socket.isConnected()){
            try{
                String ttt = bufferedReader.readLine();
                tokens = ttt.split(" ");
                if(tokens != null) {
                    if (this.isLogin == false) {
                        if (tokens[0].equals("login")) {
                            String temp = "invalid";
                            if (tokens[1].equals("thaison") || tokens[1].equals("thaiduy")) {
                                this.clientUsername = tokens[1];
                                lstClientHandler.add(this);
                                broadcastMessage("Server: " + this.clientUsername + " has joined the chat!");
                                temp = "valid";
                                isLogin = true;
                            }
                            bufferedWriter.write("Server: " + temp);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        } else if (tokens[0].equals("signup")) {
                            String temp = "invalid";

                        }
                    } else {
                        broadcastMessage(ttt);
                    }
                }
            }catch (IOException e){
                closeEverything(socket,bufferedWriter,bufferedReader);
                break;
            }
        }
    }
}
