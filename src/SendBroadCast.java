import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 31/12/2021 - 10:05 SA
 * Description: ...
 */
public class SendBroadCast extends Thread{
    private Socket s;
    private ArrayList<Socket> lstSocket;
    private String sentMessage;

    public SendBroadCast(Socket socket, ArrayList<Socket> lstSock){
        this.lstSocket = new ArrayList<>(lstSock);
        this.s=socket;
        this.lstSocket.remove(s);
    }
    public void setSentMessage(String msg){
        sentMessage = msg;
    }

    @Override
    public void run(){
        this.sendBroadMessage();
    }

    public void sendBroadMessage(){
        OutputStream os = null;
        BufferedWriter bw = null;
        DataInputStream dis = null;
        for(Socket sock:lstSocket) {
            try {
                os = sock.getOutputStream();
                bw = new BufferedWriter(new OutputStreamWriter(os));
                bw.write(sentMessage);
                bw.newLine();
                bw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bw != null && dis != null) {
                    try {
                        bw.close();
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
