package serverside;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * serverside
 * Created by Thai Son
 * Date 08/01/2022 - 11:32 SA
 * Description: ...
 */
public class ServerFrame extends JFrame implements ActionListener {
    Server server;
    JLabel headerLb, portLb, statusLb, status;
    JTextField portTf;
    JButton btnStart, btnStop;
    ServerFrame(){
        super();
        setTitle("Server");
        setLayout(new BorderLayout());
        initComponent();
        setDefaultLookAndFeelDecorated(true);
        setVisible(true);
        setMinimumSize(new Dimension(300,200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    private void initComponent(){
        headerLb = new JLabel("SERVER");
        Font font = new Font(Font.SERIF, Font.BOLD, 30);
        headerLb.setFont(font);
        headerLb.setForeground(Color.BLUE);
        headerLb.setHorizontalAlignment(SwingConstants.CENTER);
        headerLb.setSize(100,100);
        add(headerLb, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new GridBagLayout());
        portLb = new JLabel("Port:");
        statusLb = new JLabel("status:");
        status = new JLabel("OFF");
        status.setForeground(Color.RED);
        portTf = new JTextField();
        portTf.setPreferredSize(new Dimension(100,25));
        btnStart = new JButton("Start");
        btnStop = new JButton("Exit");
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        addConstrainToComponent(contentPanel, portLb,0,0,1,1);
        addConstrainToComponent(contentPanel, portTf,1,0,2,1);
        addConstrainToComponent(contentPanel, statusLb,0,1,1,1);
        addConstrainToComponent(contentPanel, status, 1,1,2,1);
        addConstrainToComponent(contentPanel, btnStart,0,2,1,1);
        addConstrainToComponent(contentPanel, btnStop,1,2,1,1);
        //        contentPanel.setPreferredSize(new Dimension(200,200));
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

    public static void main(String args[]){
        new ServerFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnStart){
            String port = portTf.getText();
            if(port.isEmpty()){
               JOptionPane.showMessageDialog(this,"Enter port to start server");
            }else {
                try {
                    ServerSocket serverSocket= new ServerSocket(Integer.getInteger(port));
                    Server server = new Server(serverSocket);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                portTf.setEditable(false);
                status.setText("ON");
                status.setForeground(Color.GREEN);
            }
        }
        else if(e.getSource() == btnStop){
            portTf.setEditable(true);
            status.setText("OFF");
            status.setForeground(Color.RED);
        }
    }
}
