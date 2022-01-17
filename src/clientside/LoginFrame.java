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
 * Date 08/01/2022 - 12:39 CH
 * Description: ...
 */
public class LoginFrame extends JFrame implements ActionListener {
    JLabel lbHeader, lbUsername,lbPassword;
    JTextField tfUsername;
    JPasswordField pfPassword;
    Client client;
    JButton btnLogin, btnSignup;

    LoginFrame(Client client){
        super();
        this.client = client;
        setTitle("Login");
        setLayout(new BorderLayout());
        initComponent();
        setDefaultLookAndFeelDecorated(true);
        setVisible(true);
        setMinimumSize(new Dimension(300,200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponent(){
        lbHeader = new JLabel("LOG IN");
        Font font = new Font(Font.SERIF, Font.BOLD, 25);
        lbHeader.setFont(font);
        lbHeader.setForeground(Color.BLUE);
        lbHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lbHeader.setSize(100,100);
        add(lbHeader, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new GridBagLayout());

        lbUsername = new JLabel("Username:");
        tfUsername = new JTextField();
        tfUsername.setPreferredSize(new Dimension(100,25));
        lbPassword = new JLabel("Password:");
        pfPassword = new JPasswordField();
        pfPassword.setPreferredSize(new Dimension(100,25));
        btnLogin = new JButton("Login");
        btnSignup = new JButton("Signup");
        btnSignup.addActionListener(this);
        btnLogin.addActionListener(this);
        addConstrainToComponent(contentPanel,lbUsername,0,0,1,1);
        addConstrainToComponent(contentPanel,tfUsername,1,0,2,1);
        addConstrainToComponent(contentPanel,lbPassword,0,1,1,1);
        addConstrainToComponent(contentPanel,pfPassword,1,1,2,1);
        addConstrainToComponent(contentPanel,btnLogin,0,2,1,1);
        addConstrainToComponent(contentPanel,btnSignup,1,2,1,1);
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
        if(e.getSource() == btnLogin){
            String username = tfUsername.getText();
            String password = pfPassword.getText();
            if(username.isEmpty() == true || password.isEmpty() == true){
                JOptionPane.showMessageDialog(this, "username or password can't be empty");
            }else{
                client.reqAuthenticate("login",username,password);
                boolean check = client.authenticateRes();
                if(check == true){
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new ChatFrame(client);
                        }
                    });
                    this.dispose();
                }else{
                    JOptionPane.showMessageDialog(this,"Sign in fail");
                }
            }
        }else if(e.getSource() == btnSignup){
            new SignupFrame(client);
        }
    }

}
