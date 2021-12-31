import java.io.*;
import java.net.Socket;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 30/12/2021 - 11:11 CH
 * Description: ...
 */
public class SendThread extends Thread{
    private Socket sock;

    private String message;

    SendThread(Socket s){
        this.sock = s;

    }

    @Override
    public void run(){
        OutputStream os = null;
        BufferedWriter bw = null;
        DataInputStream dis = null;
        try {
            do {
                dis = new DataInputStream(System.in);
                os = sock.getOutputStream();
                bw = new BufferedWriter(new OutputStreamWriter(os));
                message = dis.readLine();
                bw.write(sock.getLocalPort() + ": " + message);
                bw.newLine();
                bw.flush();
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bw != null && dis != null){
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
