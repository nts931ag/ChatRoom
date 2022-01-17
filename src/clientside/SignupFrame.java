package clientside;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * clientside
 * Created by Thai Son
 * Date 08/01/2022 - 12:47 CH
 * Description: ...
 */
public class SignupFrame extends JFrame implements ActionListener {
    JLabel lbHeader, lbUsername,lbPassword, lbConfirmPassword;
    JTextField tfUsername;
    JPasswordField pfPassword, pfConfirmPassword;
    Client client;
    JButton btnSignup, btnBack;

    SignupFrame(Client client){
        super();
        this.client = client;
        setTitle("Signup");
        setLayout(new BorderLayout());
        initComponent();
        setDefaultLookAndFeelDecorated(true);
        setVisible(true);
        setMinimumSize(new Dimension(300,200));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
    }

    private void initComponent(){
        lbHeader = new JLabel("SIGN UP");
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
        lbConfirmPassword = new JLabel("Confirm Password:");
        pfConfirmPassword = new JPasswordField();
        pfConfirmPassword.setPreferredSize(new Dimension(100,25));
        btnSignup = new JButton("Signup");
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        btnSignup.addActionListener(this);
        addConstrainToComponent(contentPanel,lbUsername,0,0,1,1);
        addConstrainToComponent(contentPanel,tfUsername,1,0,2,1);
        addConstrainToComponent(contentPanel,lbPassword,0,1,1,1);
        addConstrainToComponent(contentPanel,pfPassword,1,1,2,1);
        addConstrainToComponent(contentPanel,lbConfirmPassword,0,2,1,1);
        addConstrainToComponent(contentPanel,pfConfirmPassword,1,2,2,1);
        addConstrainToComponent(contentPanel,btnSignup,0,3,1,1);
        addConstrainToComponent(contentPanel,btnBack,1,3,1,1);
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
        if(e.getSource() == btnSignup){
            String username = tfUsername.getText();
            String password = pfPassword.getText();
            String cfmPassword = pfConfirmPassword.getText();
            if(username.isEmpty() == true || password.isEmpty() == true || cfmPassword.isEmpty()){
                JOptionPane.showMessageDialog(this, "username or password can't be empty");
            }else if(!password.equals(cfmPassword)){
                JOptionPane.showMessageDialog(this, "confirm password incorrect");
            }else{
                client.reqAuthenticate("signup",username,password);
                boolean check = client.authenticateRes();
                if(check == true){
                    JOptionPane.showMessageDialog(this,"Signup success");
                }else{
                    JOptionPane.showMessageDialog(this,"Signup fail");
                }
            }
        }else if(e.getSource() == btnBack){
            this.dispose();
        }
    }

}
