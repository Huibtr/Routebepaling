import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class CustomerScreen extends JFrame implements ActionListener {
    private  ArrayList<Customer> customers;
    private JButton JBterug;
    private JTextField JTinputSearchCustomerID;
    private JButton JBSearch;

    public CustomerScreen() {
        //Lijst met klanten vullen
        getCustomer();

        //Layout wijzigen
        this.setTitle("NerdyGadgets - Klanten");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Buttons, labels en een tekstvak toevoegen aan het scherm
        JBterug = new JButton("\uD83E\uDC80 terug");
        JBterug.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(JBterug, c);

        JLabel JLSearch = new JLabel("Zoeken op klantID: ");
        add(JLSearch);

        JTinputSearchCustomerID = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 1;
        c.gridx = 3;
        c.gridy = 0;
        add(JTinputSearchCustomerID, c);


        JBSearch = new JButton("Zoek klant");
        JBSearch.addActionListener(this);
        add(JBSearch);

        //Nieuwe tabel aanmaken
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        Object[] columnsName = new Object[] {
                "KlantID", "Klantnaam"
        };
        model.setColumnIdentifiers(columnsName);
        Object[] rowData = new Object[2];
        for(int i = 0; i < customers.size(); i++){
            rowData[0] = customers.get(i).getCustomerID();
            rowData[1] = customers.get(i).getCustomerName();
            model.addRow(rowData);
        }
        table.setModel(model);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 200;
        c.weightx = 0.0;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        //Tabel toevoegen aan het scherm
        add(new JScrollPane(table), c);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        //Ervoor zorgen dat je op de rijen in de tabel kunt klikken en dan naar een specifieke pagina gaat
        ListSelectionModel modelclick = table.getSelectionModel();
        modelclick.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!modelclick.isSelectionEmpty()){
                    int customerID = 0;
                    int selectrow = modelclick.getMinSelectionIndex();
                    for (int i = 0; i < customers.size(); i++){
                        if(i == selectrow){
                            customerID = customers.get(i).getCustomerID();
                        }
                    }
                    try {
                        if (listSelectionEvent.getValueIsAdjusting()) {
                            //Nieuwe specifieke pagina openen
                            CustomerInfoScreen customerInfoScreen = new CustomerInfoScreen(customerID, "CustomerScreen");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void getCustomer(){
        //Lijst met klanten aanmaken
        customers = new ArrayList<>();
        Customer customer;

        try {
            //Nieuwe databaseconnectie aanmaken
            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getCustomers();

            //Lijst met klanten vullen met gegevens uit de database
            while(rs.next()){
                customer = new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getString("CityName"),
                        rs.getString("DeliveryAddressLine2"),
                        rs.getString("DeliveryPostalCode"),
                        rs.getString("PhoneNumber")
                );
                customers.add(customer);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Knoppen werkend maken
        if (e.getSource() == JBterug) {
            dispose();
            DataScreen dataScreen = new DataScreen();
        }
        if (e.getSource() == JBSearch) {
            boolean heeftSchermGeopend = false;
            boolean foutmelding = false;
            try {
                    for (Customer c : customers) {
                            if (c.getCustomerID() == Integer.parseInt(JTinputSearchCustomerID.getText())) {
                                try {
                                    //Nieuwe specifieke pagina openen
                                    CustomerInfoScreen customerInfoScreen = new CustomerInfoScreen(Integer.parseInt(JTinputSearchCustomerID.getText()), "CustomerScreen");
                                    heeftSchermGeopend = true;
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                }
            }catch (NumberFormatException nfe) {
                //Foutmelding teruggeven
                JOptionPane.showMessageDialog(null, "Getal invoeren!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                foutmelding = true;
            }
                    if (heeftSchermGeopend == false && foutmelding == false) {
                        //Foutmelding teruggeven
                        JOptionPane.showMessageDialog(null, "Verkeerde invoer!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                    }
        }
    }
}


