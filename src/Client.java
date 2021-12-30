import java.io.*;
import java.net.Socket;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 28/12/2021 - 9:44 CH
 * Description: ...
 */
public class Client {
    public Client(){
        try{
            Socket s = new Socket("localhost",3200);
            System.out.println(s.getPort());

            InputStream is = s.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            OutputStream os = s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            String sentMessage = "";
            String receiveMessage;
            do{
                DataInputStream dis=new DataInputStream(System.in);
                sentMessage = dis.readLine();
                bw.write(sentMessage);
                bw.newLine();
                bw.flush();

                if(sentMessage.equalsIgnoreCase("quit"))
                    break;
                else{
                    receiveMessage = br.readLine();
                    System.out.println("Receive: "+receiveMessage);
                }
            }while(true);
            br.close();
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new Client();
    }
}
