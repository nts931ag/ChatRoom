import java.io.*;
import java.net.Socket;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 30/12/2021 - 11:22 CH
 * Description: ...
 */
public class ReceiveThread extends Thread{
    private Socket sock;
    ReceiveThread(Socket s){
        this.sock = s;
    }

    @Override
    public void run(){
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = sock.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String receiveMessage = br.readLine();
            System.out.println(receiveMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
