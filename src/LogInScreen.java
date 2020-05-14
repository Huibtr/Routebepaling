import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class LogInScreen extends JFrame implements ActionListener {

    private JLabel labelUsername = new JLabel("Gebruikersnaam: ");
    private JLabel labelPassword = new JLabel("Wachtwoord: ");
    private JTextField textUsername = new JTextField(20);
    private JPasswordField fieldPassword = new JPasswordField(20);
    private JButton buttonLogin;

    public LogInScreen() {
        super("Nerdy Gadgets");

        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(labelUsername, constraints);

        constraints.gridx = 1;
        newPanel.add(textUsername, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(labelPassword, constraints);

        constraints.gridx = 1;
        newPanel.add(fieldPassword, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        buttonLogin = new JButton("Login");
        buttonLogin.addActionListener(this);
        newPanel.add(buttonLogin, constraints);

        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Login scherm"));

        // add the panel to this frame
        add(newPanel);

        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String wachtwoordBezorging = "Bezorging2020";
        String wachtwoordLogistiek = "Logistiek2020";

        if(actionEvent.getSource() == buttonLogin){
            if(fieldPassword.getText().equals(wachtwoordBezorging)){
                RoutingScreen routingScreen= new RoutingScreen();
                dispose();
            } else if (fieldPassword.getText().equals(wachtwoordLogistiek)){
                DataScreen dataScreen = new DataScreen();
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Wachtwoord incorrect");
            }
        }

    }
}