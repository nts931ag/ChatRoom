import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 28/12/2021 - 3:08 CH
 * Description: ...
 */
public class ThreadSwing extends JFrame {
    ThreadSwing(){
        setLayout(null);
        Font font = new Font(Font.SERIF, Font.BOLD & Font.ITALIC, 50);
        JLabel lbNum = new JLabel("0");
        lbNum.setFont(font);
        lbNum.setForeground(new Color(255,0,0));
        lbNum.setBounds(50,50,100,100);
        add(lbNum);

        JButton btnRun = new JButton("Run");
        btnRun.setBounds(50,150,100,50);
        add(btnRun);
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyChangingLabelThread t = new MyChangingLabelThread(lbNum, btnRun);
                t.start();
            }
        });

        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setVisible(true);
    }

    public static void main(String[] args){
        new ThreadSwing();
    }
}

class MyChangingLabelThread extends Thread{
    JLabel lbNum;
    JButton btnRun;
    int i;

    MyChangingLabelThread(JLabel lbNum, JButton btnRun){
        this.lbNum = lbNum;
        this.btnRun = btnRun;
    }

    @Override
    public void run() {
        btnRun.setEnabled(false);
        synchronized (lbNum){
            for(i=1;i<=100;++i) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lbNum.setText("" + i);
                    }
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadSwing.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        btnRun.setEnabled(true);
    }
}