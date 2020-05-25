import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreen extends JFrame implements ActionListener {
    private JButton jbData;
    private JButton jbRoute;

    public HomeScreen() {
        //Layout wijzigen
        setTitle("Nerdy Gadgets");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,1));

        //Buttons toevoegen
        jbData = new JButton("Gegevens inzien");
        jbData.addActionListener(this);
        add(jbData);

        jbRoute = new JButton("Route bepaling");
        jbRoute.addActionListener(this);
        add(jbRoute);

        //Scherm zichtbaar maken
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //Knoppen werkend maken
        if(e.getSource() == jbData){
            DataScreen dataScreen = new DataScreen();
            dispose();
        }
        else if(e.getSource() == jbRoute){
            RoutingScreen routingscreen = new RoutingScreen();
            dispose();
        }
    }
}
