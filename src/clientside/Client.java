package clientside;

import account.Account;

import java.io.*;
import java.net.Socket;
import java.nio.channels.AcceptPendingException;
import java.util.Scanner;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 05/01/2022 - 12:29 CH
 * Description: ...
 */
public class Client {
    private Socket socket;

    private DataOutputStream dos;
    private DataInputStream dis;
//    private String username;
    private Account account;

    public Client(Socket socket){
        try{
            this.socket = socket;
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
        }catch (IOException e){
            closeEverything(socket,dos,dis);
        }
    }

    public void reqAuthenticate(String type, String Username, String Password){
        String messageToSend = new String("{" + type +"}" + "`{" + Username +"}" +"`{" + Password +"}");
        account = new Account(Username,Password);
        try{
            dos.writeUTF(messageToSend);
        }catch(IOException ioe){
            closeEverything(socket,dos,dis);
        }
    }

    public boolean authenticateRes(){
        try {
            String messageFromServer = dis.readUTF();
            String[] tokens;
            if(messageFromServer != null){
                tokens = messageFromServer.split("`");

                if(tokens[2].equals("{success}")){
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendMessage(String type, String msg){
        String messageToSend = new String("{" + type +"}" + "`{" + account.getUsername() +"}" +"`{" + msg +"}");

        try{
            dos.writeUTF(messageToSend);
        }catch(IOException ioe){
            closeEverything(socket,dos,dis);
        }
    }

    public void sendFile(File file){
        try{
            FileInputStream fileInputStream = new FileInputStream(file);

            String fileName = file.getName();
            byte[] fileNameBytes = fileName.getBytes();

            byte[] fileContentBytes = new byte[(int) file.length()];
            fileInputStream.read(fileContentBytes);

            dos.writeInt(fileNameBytes.length);
            dos.write(fileNameBytes);
            dos.writeInt(fileContentBytes.length);
            dos.write(fileContentBytes);
            fileInputStream.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void receiveFile(String address){
        try{
            int fileNameLength = dis.readInt();
            if(fileNameLength > 0){
                byte[] fileNameBytes = new byte[fileNameLength];
                dis.readFully(fileNameBytes, 0, fileNameLength);
                String fileName = new String(fileNameBytes);
                int fileContentLength = dis.readInt();
                if(fileContentLength > 0){
                    byte[] fileContentBytes = new byte[fileContentLength];
                    dis.readFully(fileContentBytes,0,fileContentLength);
                    FileOutputStream fileOutputStream = new FileOutputStream(address + "\\" + fileName);
                    fileOutputStream.write(fileContentBytes);
                    fileOutputStream.close();
                }
            }

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void closeEverything(Socket socket, DataOutputStream dos, DataInputStream dis) {
        try{
            if(socket !=null){
                socket.close();
            }
            if(dos != null){
                dos.close();
            }
            if(dis != null){
                dis.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getDos(){
        return dos;
    }

    public DataInputStream getDis(){
        return dis;
    }

    public String getUsername(){
        return account.getUsername();
    }
}
