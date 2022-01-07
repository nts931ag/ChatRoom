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
    private static ArrayList<String> lstClientUsername = new ArrayList<>();
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
                String messageFromClient = bufferedReader.readLine();
                System.out.println(messageFromClient);
                if(messageFromClient != null) {
                    tokens = messageFromClient.split(": ");
                    if(tokens[0].equals("guess")){
                        String[] temp = tokens[1].split("\\-");
                        if(temp[1].equals("login request")){
                            if(lstClientUsername.contains(temp[0])){
                                isLogin = true;
                                clientUsername = temp[0];
                                broadcastMessage("Server: " + clientUsername + " has joined the chat");
                                lstClientHandler.add(this);
                                bufferedWriter.write("Server: login-valid");
                            }else{
                                bufferedWriter.write("Server: login-invalid");
                            }
                        }else if(temp[1].equals("signup request")){
                            if(lstClientUsername.contains(temp[0])){
                                bufferedWriter.write("Server: signup-invalid");
                            }else{
                                lstClientUsername.add(temp[0]);
                                bufferedWriter.write("Server: signup-valid");
                            }
                        }
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        //bufferedWriter.write("guess: " + tokens[1] + " login request");
                    }else if(tokens[0].equals("send")){
                        broadcastMessage(this.clientUsername +": " + tokens[1]);
                    }else if(tokens[0].equals("send file")){

                    }else if(tokens[0].equals("logout")){
                        bufferedWriter.write("Server: logout-success");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        closeEverything(socket,bufferedWriter,bufferedReader);
                    }
                }
            }catch (IOException e){
                closeEverything(socket,bufferedWriter,bufferedReader);
                break;
            }
        }
    }
}
