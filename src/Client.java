import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 03/01/2022 - 8:13 CH
 * Description: ...
 */
public class Client {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String username;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        }catch(IOException e){
            closeEverything(socket, bw, br);
        }
    }

    public void sendMessage(){
        try{
            bw.write(username);
            bw.newLine();
            bw.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bw.write(username + ": " + messageToSend);
                bw.newLine();
                bw.flush();
            }
        }catch (IOException e){
            closeEverything(socket, bw, br);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFormGroupChat;

                while(socket.isConnected()){
                    try{
                        msgFormGroupChat = br.readLine();
                        System.out.println(msgFormGroupChat);
                    }catch(IOException e){
                        closeEverything(socket, bw, br);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket s, BufferedWriter bw, BufferedReader br){
        try{
            if(bw != null){
                bw.close();
            }

            if(br != null){
                br.close();
            }

            if(s != null){
                s.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your username for the group chat: ");
        String username = sc.nextLine();

        Socket s = new Socket("localhost", 5555);
        Client client = new Client(s,username);
        client.listenForMessage();
        client.sendMessage();
    }
}
