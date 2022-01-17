package clientside;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

/**
 * clientside
 * Created by Thai Son
 * Date 08/01/2022 - 12:26 CH
 * Description: ...
 */
public class ConnectFrame extends JFrame implements ActionListener {
    JLabel lbHeader, lbHost,lbPort;
    JTextField tfHost, tfPort;
    JButton btnConnect, btnExit;
    Client client;
    ConnectFrame(){
        super();
        setTitle("Connect to server");
        setLayout(new BorderLayout());
        initComponent();
        setDefaultLookAndFeelDecorated(true);
        setVisible(true);
        setMinimumSize(new Dimension(300,200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponent(){
        lbHeader = new JLabel("CONNECT TO SERVER");
        Font font = new Font(Font.SERIF, Font.BOLD, 25);
        lbHeader.setFont(font);
        lbHeader.setForeground(Color.BLUE);
        lbHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lbHeader.setSize(100,100);
        add(lbHeader, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new GridBagLayout());

        lbHost = new JLabel("Host");
        tfHost = new JTextField();
        tfHost.setPreferredSize(new Dimension(100,25));
        lbPort = new JLabel("Port");
        tfPort = new JTextField();
        tfPort.setPreferredSize(new Dimension(100,25));
        btnConnect = new JButton("Connect");
        btnExit = new JButton("Exit");
        btnExit.addActionListener(this);
        btnConnect.addActionListener(this);
        addConstrainToComponent(contentPanel,lbHost,0,0,1,1);
        addConstrainToComponent(contentPanel,tfHost,1,0,2,1);
        addConstrainToComponent(contentPanel,lbPort,0,1,1,1);
        addConstrainToComponent(contentPanel,tfPort,1,1,2,1);
        addConstrainToComponent(contentPanel,btnConnect,0,2,1,1);
        addConstrainToComponent(contentPanel,btnExit,1,2,1,1);
        //        contentPanel.setPreferredSize(new Dimension(new Dime)nsion(200,200));
        add(contentPanel);
    }

    private void addConstrainToComponent(JComponent jc, JComponent c, int x, int y, int width, int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jc.add(c, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnConnect){
            String port = tfPort.getText();
            String host = tfHost.getText();
            if(port.isEmpty() == true || host.isEmpty() == true){
                JOptionPane.showMessageDialog(this, "Host or Port can't be empty");
            }else{
                Socket socket=null;
                try {
                    socket = new Socket(host,Integer.parseInt(port));
                    if(socket.isConnected()) {
                        Client client = new Client(socket);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                LoginFrame lF = new LoginFrame(client);
                            }
                        });
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if(socket.isConnected()){
                        this.dispose();
                    }
                }
            }
        }else if(e.getSource() == btnExit){
            this.dispose();
        }
    }

    public static void main(String[] args){

        ConnectFrame cF = new ConnectFrame();
    }
}
