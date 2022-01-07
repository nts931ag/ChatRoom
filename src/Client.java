import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 05/01/2022 - 12:29 CH
 * Description: ...
 */
public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;
    private boolean isLogin = false;

    public Client(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void sendMessage(){
        try {
            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = sc.nextLine();
                String[] tokens = messageToSend.split(": ");
                if(tokens[0].equals("login")){
                    username = tokens[1];
                    bufferedWriter.write("guess: " + tokens[1] + "-login request");
                }else if(tokens[0].equals("signup")){
                    bufferedWriter.write("guess: " + tokens[1] + "-signup request");
                }else if(tokens[0].equals("logout")){
                    bufferedWriter.write(tokens[0] + ": " + username);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    break;
                }
                else if(tokens[0].equals("send")){
                    bufferedWriter.write( "send: " + tokens[1]);
                }else if(tokens[0].equals("send file")){

                    //
                }
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    public void sendFile(){

    }

    public void receiveFile(){

    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] tokens;
                while(socket.isConnected()){
                    try{
                        String msgFromServer = bufferedReader.readLine();
                        if(msgFromServer != null){
                            tokens = msgFromServer.split(": ");
                            if(tokens[0].equals("Server")){
                                if(tokens[1].equals("login-valid")){
                                    isLogin = true;
                                }else if(tokens[1].equals("logout-success")){
                                    isLogin = false;
                                    closeEverything(socket,bufferedWriter,bufferedReader);
                                    break;
                                }
                            }else{

                            }
                            System.out.println(msgFromServer);
                        }
                    }catch (IOException e){
                        closeEverything(socket,bufferedWriter,bufferedReader);
                    }
                }
            }
        }).start();
    }

    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
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

    public static void main(String[] args) throws Exception{

        Socket socket = new Socket("localhost", 5555);
        Client client = new Client(socket);
        client.listenForMessage();
        client.sendMessage();
    }

}
