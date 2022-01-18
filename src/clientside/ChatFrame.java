package clientside;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * clientside
 * Created by Thai Son
 * Date 16/01/2022 - 9:08 CH
 * Description: ...
 */
public class ChatFrame extends JFrame implements ActionListener {
    private JPanel fileArea, eastPanel, centerPanel;
    private JTextArea receiveArea;
    private DefaultTableModel tableModel;
    private JTable tbFile;
    private JScrollPane scrollPane;
    private Client client;
    private JTextField tfMsg;
    private String pathFileDownload;
    JButton btnUpFile, btnDownFile, btnSend, btnLogout;
    public ChatFrame(Client client){
        super();
        this.client = client;
        setTitle("Chat Room");
        setLayout(new BorderLayout());
        initComponent();
        setDefaultLookAndFeelDecorated(true);
        setVisible(true);
        setMinimumSize(new Dimension(500,400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponent() {
        Font font = new Font(Font.SERIF,Font.BOLD,25);
        JLabel header = new JLabel("Chat Room");
        header.setFont(font);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header,BorderLayout.NORTH);

        //center
        centerPanel = new JPanel(new BorderLayout());
        receiveArea = new JTextArea(10,1);
        receiveArea.setEditable(false);
        font = new Font(Font.SERIF,Font.BOLD,13);
        receiveArea.setFont(font);
        receiveArea.setPreferredSize(new Dimension(300,150));
        receiveArea.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.BLACK));
        centerPanel.add(receiveArea,BorderLayout.CENTER);
        JPanel sendArea = new JPanel(new GridBagLayout());
        tfMsg = new JTextField();
        btnSend = new JButton("send");
        tfMsg.setPreferredSize(new Dimension(200,50));
        tfMsg.setMinimumSize(new Dimension(200,50));
        addConstrainToComponent(sendArea,tfMsg,0,0,1,1);
        addConstrainToComponent(sendArea,btnSend,1,0,1,1);
        centerPanel.add(sendArea,BorderLayout.SOUTH);
        centerPanel.setBorder(new EmptyBorder(1,1,1,1));
        centerPanel.setSize(new Dimension(300,200));
        add(centerPanel,BorderLayout.CENTER);

        //east
        eastPanel = new JPanel(new BorderLayout());
        String[] tbHeader = new String[]{"name","posted by"};
        tableModel = new DefaultTableModel(null, tbHeader);
        tbFile = new JTable(tableModel);
        scrollPane = new JScrollPane(tbFile);
        btnUpFile = new JButton("Upload");
        btnDownFile = new JButton("Download");
        fileArea = new JPanel(new GridBagLayout());
        scrollPane.setPreferredSize(new Dimension(200,200));
        addConstrainToComponent(fileArea,scrollPane,0,0,2,1);
        addConstrainToComponent(fileArea,btnUpFile,0,1,1,1);
        addConstrainToComponent(fileArea,btnDownFile,1,1,1,1);
        eastPanel.add(fileArea,BorderLayout.CENTER);
        btnLogout = new JButton("Logout");
        eastPanel.add(btnLogout, BorderLayout.SOUTH);
        eastPanel.setBorder(new EmptyBorder(2,2,2,2));
        add(eastPanel,BorderLayout.EAST);

        btnSend.addActionListener(this);
        btnLogout.addActionListener(this);
        btnDownFile.addActionListener(this);
        btnUpFile.addActionListener(this);
        this.listenForMessage();
    }

    private void updateRowData(String filename, String postedby){
        String fileName = new String(filename.substring(1,filename.length()-1));
        String postedBy = new String(postedby.substring(1,postedby.length()-1));
        tableModel.addRow(new Object[]{fileName, postedBy});
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] tokens;
                Socket socket = client.getSocket();
                DataInputStream dis = client.getDis();
                DataOutputStream dos = client.getDos();
                while(socket.isConnected()){
                    try{
                        String msgFromServer = dis.readUTF();
                        if(msgFromServer != null){
                            tokens = msgFromServer.split("`");
                            if(tokens[0].equals("{logout}")){
                                if(tokens[2].equals("sucess")){
                                    client.closeEverything(socket,dos,dis);

                                    break;
                                }
                            }else if(tokens[0].equals("{notiLogout}")){
                                String username = new String(tokens[1].substring(1,tokens[1].length()-1));
                                String message = new String(tokens[2].substring(1,tokens[2].length()-1));
                                receiveArea.append(username+": "+message+"\n");
                            }else if(tokens[0].equals("{downloadfile}")){
                                if(tokens[2].equals("{file exist}")){
                                    client.receiveFile(pathFileDownload);
                                }else{
                                    JOptionPane.showMessageDialog(null, "file not exist");
                                }
                            }else if(tokens[0].equals("{uploadfile}")){
                                updateRowData(tokens[2], tokens[1]);
                            }else if(tokens[0].equals("{msgBroadcast}")){
                                String username = new String(tokens[1].substring(1,tokens[1].length()-1));
                                String message = new String(tokens[2].substring(1,tokens[2].length()-1));
                                receiveArea.append(username+": "+message+"\n");
                            }
                            System.out.println(msgFromServer);
                        }
                    }catch (IOException e){
                        client.closeEverything(socket,dos,dis);
                    }
                }
            }
        }).start();
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
        if(e.getSource() == btnSend){
            String msg = tfMsg.getText();
            if(!msg.isEmpty()) {
                client.sendMessage("msgBroadcast", msg);
                receiveArea.append("You: " + msg + "\n");
            }
            tfMsg.setText("");
        }else if (e.getSource() == btnLogout){
            client.sendMessage("logout","has been left the chat room");
            System.exit(0);
            this.dispose();

        }else if (e.getSource() == btnDownFile){
            int row = tbFile.getSelectedRow();
            int col = tbFile.getSelectedColumn();
            if(row < 0 || col < 0){
                JOptionPane.showMessageDialog(this,"Please choose file to download");
            }else {
                final JFileChooser fileDialog = new JFileChooser();
                fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileDialog.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileDialog.getSelectedFile();
                    String fileName = String.valueOf(tbFile.getValueAt(row, 0));
                    pathFileDownload = file.getAbsolutePath();
                    client.sendMessage("downloadfile", fileName);
                }
            }
        }else if(e.getSource() == btnUpFile){
            final JFileChooser fileDialog = new JFileChooser();
            int returnVal = fileDialog.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File file = fileDialog.getSelectedFile();
                updateRowData("{"+file.getName()+"}","{"+client.getUsername()+"}");
                client.sendMessage("uploadfile",file.getName());
                client.sendFile(file);
            }
        }
    }
}
