package serverside;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import account.Account;
/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 05/01/2022 - 12:13 CH
 * Description: ...
 */
public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> lstClientHandler = new ArrayList<>();
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    public static  ArrayList<Account> lstAccount = new ArrayList<Account>();
    private Account account;
    private static String fileAreaPath;
    private boolean isLogin = false;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
            fileAreaPath = new java.io.File(".").getCanonicalPath();
            fileAreaPath += "\\src\\serverside\\filearea";
        }catch (IOException e){
            closeEverything(socket,dos,dis);
        }
    }

    private void broadcastMessage(String messageToSend) {

        for(ClientHandler clientHandler : lstClientHandler){
            try{
                if(clientHandler.account.compareTo(account) == -1){
                    clientHandler.dos.writeUTF(messageToSend);
                    clientHandler.dos.flush();
                }
            }catch(IOException e){
                closeEverything(socket,dos,dis);

            }
        }
    }

    public void removeClientHandler(){
        lstClientHandler.remove(this);
    }

    private void closeEverything(Socket socket, DataOutputStream dos, DataInputStream dis) {
        removeClientHandler();
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

    public void sendFile(File file){
        try{

            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());

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

    public static Account processReqAuthenticate(String msgFromClient){
        StringBuilder data = new StringBuilder(msgFromClient);
        int index = data.indexOf("}");
        String username = data.substring(1,index);
        String password = data.substring(index+2,data.length()-1);
        Account tempAccount = new Account(username,password);
        return tempAccount;
    }

    @Override
    public void run() {
        String[] tokens;
        while(socket.isConnected()){
            try{
                String msgFromClient = dis.readUTF();
                System.out.println(msgFromClient);
                if(msgFromClient != null) {
                    tokens = msgFromClient.split("`");
                    if (tokens[0].equals("{login}")) {
                        account = processReqAuthenticate(tokens[1]+tokens[2]);
                        if (lstAccount.contains(account)) {
                            isLogin = true;
                            lstClientHandler.add(this);
                            dos.writeUTF("{login}`{server}`{success}");
                        } else {
                            dos.writeUTF("{login}`{server}`{fail}");

                        }
                    } else if (tokens[0].equals("{signup}")) {
                        account = processReqAuthenticate(tokens[1]+tokens[2]);
                        if (lstAccount.contains(account)) {
                            dos.writeUTF("{signup}`{server}`{fail}");
                        } else {
                            lstAccount.add(account);
                            dos.writeUTF("{signup}`{server}`{success}");
                        }
                    } else if (tokens[0].equals("{logout}")) {
                        isLogin=false;
                        dos.writeUTF("{logout}`{server}`{success}");
                        dos.flush();
                    } else if (tokens[0].equals("{msgBroadcast}")) {

                        broadcastMessage(msgFromClient);
                    } else if (tokens[0].equals("{uploadfile}")) {
                        receiveFile(fileAreaPath);
                        broadcastMessage(msgFromClient);
                    } else if (tokens[0].equals("{downloadfile}")) {
                        String fileName = new String(tokens[2].substring(1,tokens[2].length()-1));
                        File f = new File(fileAreaPath + "\\" + fileName);
                        if(f.exists() && !f.isDirectory()){
                            dos.writeUTF("{downloadfile}`{server}`{file exist}");
                            dos.flush();
                            sendFile(f);
                        }
                    }
                }
            }catch (IOException e){
                closeEverything(socket,dos,dis);
                break;
            }
        }
    }

}
