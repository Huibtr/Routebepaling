import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DataScreen extends JFrame implements ActionListener {
    private JButton jbQuantity;
    private JButton jbOrders;
    private JButton jbCustomers;
    private JButton jbGoBack;
    private JLabel labelUsername = new JLabel("Terug");

    public DataScreen(){
        // Layout wijzigen
        setTitle("Nerdy Gadgets - Gegevens inzien");
        JPanel newPanel = new JPanel(new FlowLayout());
        setSize(500,100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jp = new JPanel();
        jp.setPreferredSize(new Dimension(300, 75));

        jp.setBorder(BorderFactory.createTitledBorder("gegevens inzien"));

        //Buttons toevoegen
        jbGoBack = new JButton("\uD83E\uDC80 uitloggen");
        jbGoBack.addActionListener(this);
        jp.add(jbGoBack);

        jbQuantity = new JButton("Voorraad");
        jbQuantity.addActionListener(this);
        jp.add(jbQuantity);


        jbOrders = new JButton("Bestellingen");
        jbOrders.addActionListener(this);
        jp.add(jbOrders);


        jbCustomers = new JButton("Klanten");
        jbCustomers.addActionListener(this);
        jp.add(jbCustomers);

        //Alle elementen zichtbaar maken
        add(jp);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Buttons werkend maken, zodat als je er op klikt dat er dan een ander scherm geopend word
        if(e.getSource() == jbQuantity){
            QuantityScreen quantityScreen = new QuantityScreen();
            dispose();
        } else if (e.getSource() == jbOrders){
            OrderScreen orderScreen = new OrderScreen();
            dispose();
        } else if(e.getSource() == jbCustomers){
            CustomerScreen customerScreen = new CustomerScreen();
            dispose();
        } else if(e.getSource() == jbGoBack) {
            dispose();
            LogInScreen logInScreen = new LogInScreen();
        }
    }
}
