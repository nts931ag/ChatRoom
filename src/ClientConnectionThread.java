import java.io.*;
import java.net.Socket;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 28/12/2021 - 4:48 CH
 * Description: ...
 */
public class ClientConnectionThread extends Thread {
    Socket s;


    ClientConnectionThread(Socket ss) {
        this.s = ss;
    }

    @Override
    public void run() {
        System.out.println(s.getPort());
        try {
            InputStream is = s.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            OutputStream os = s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            String sentMessage = "";
            String receiveMessage;

            do {
                /*receiveMessage = br.readLine();
                System.out.println("Receive: " + receiveMessage);
                if (receiveMessage.equalsIgnoreCase("quit")) {
                    System.out.println("Client has left: ");
                    break;
                } else {
                    DataInputStream dis = new DataInputStream(System.in);
                    sentMessage = dis.readLine();
                    bw.write(sentMessage);
                    bw.newLine();
                    bw.flush();
                }*/
                SendThread st = new SendThread(s);
                st.start();
                ReceiveThread rt = new ReceiveThread(s);
                rt.start();
            } while (true);
            /*br.close();
            bw.close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
