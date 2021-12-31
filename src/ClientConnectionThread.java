import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 28/12/2021 - 4:48 CH
 * Description: ...
 */
public class ClientConnectionThread extends Thread {
    Socket s;
    ArrayList<Socket> lstSock;
    String sentMessage;
    String receivedMessage;

    ClientConnectionThread(Socket ss, ArrayList<Socket> lstSocket) {
        this.s = ss;
        this.lstSock = lstSocket;
    }

    @Override
    public void run() {
        System.out.println(s.getPort());
        try {

            /*SendThread st = new SendThread(s);
            st.start();*/
            ReceiveThread rt = new ReceiveThread(s);
            rt.start();

            SendBroadCast sbc = new SendBroadCast(s,lstSock);
            sbc.setSentMessage(rt.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
