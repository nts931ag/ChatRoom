import java.io.*;
import java.net.Socket;
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
                if(isLogin == false){
                    this.username = messageToSend.split(" ")[1];
                    bufferedWriter.write(messageToSend);
                }else{
                    bufferedWriter.write(username+ ": " + messageToSend);
                }
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] msgFormGroupChat;

                while(socket.isConnected()){
                    try{
                        String ttt = bufferedReader.readLine();
                        msgFormGroupChat = ttt.split(" ");
                        if(msgFormGroupChat != null) {
                            if (isLogin == false) {
                                if(msgFormGroupChat[1].equals("valid")){
                                    isLogin = true;
                                }
                                System.out.println(ttt);
                            } else {
                                System.out.println(ttt);
                            }
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
