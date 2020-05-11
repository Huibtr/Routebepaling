import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class  LogInScreen extends JFrame implements ActionListener {

    //Attributes
    private JButton jbLogin;
    private JPasswordField JPassword;

    //Constructor
    public LogInScreen(){

        setTitle("Nerdy Gadgets | Log-in scherm");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,2));

        JLabel lblPassword = new JLabel("Wachtwoord");
        add(lblPassword);

        JPassword = new JPasswordField();
        add(JPassword);

        jbLogin = new JButton("Log in");
        jbLogin.addActionListener(this);
        add(jbLogin);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        String wachtwoordBezorging = "Bezorging2020";
        String wachtwoordLogistiek = "Logistiek2020";

        if(actionEvent.getSource() == jbLogin){
            if(JPassword.getText().equals(wachtwoordBezorging)){
                RoutingScreen routingScreen= new RoutingScreen();
            } else if (JPassword.getText().equals(wachtwoordLogistiek)){
               DataScreen dataScreen = new DataScreen();
            } else {
                JOptionPane.showMessageDialog(null, "Wachtwoord incorrect");
            }
        }
    }
}