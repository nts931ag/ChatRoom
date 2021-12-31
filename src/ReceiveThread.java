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
    private String message;
    ReceiveThread(Socket s){
        this.sock = s;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public void run(){
        InputStream is = null;
        BufferedReader br = null;
        try {
            do {
                is = sock.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                this.message = br.readLine();
                System.out.println(message);
            }while(true);
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
