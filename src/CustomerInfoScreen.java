import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerInfoScreen extends JFrame {
    private int customerID;
    private String customerName;
    private String deliveryAddressLine2;
    private String cityName;
    private String deliveryPostalCode;
    private String phoneNumber;
    private String previousScreen;
    private DefaultTableModel model;
    private JTable table;
    private Object[] columnsName;
    private Object[] rowData;
    private ArrayList<Integer> openstaandeBestellingen;

    public CustomerInfoScreen(int customerID, String previousScreen) throws SQLException {
        this.customerID = customerID;
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = dbConnection.getCustomersInfo(customerID);

        while (resultSet.next()) {
            customerID = resultSet.getInt("CustomerID");
            customerName = resultSet.getString("CustomerName");
            cityName = resultSet.getString("CityName");
            deliveryAddressLine2 = resultSet.getString("DeliveryAddressLine2");
            deliveryPostalCode = resultSet.getString("DeliveryPostalCode");
            phoneNumber = resultSet.getString("PhoneNumber");
        }
        setTitle("NerdyGadgets - " + customerName);
        setSize(500, 500);
        setLayout(new GridLayout(7, 2));

        JLabel JLcustomerID = new JLabel("Klantnummer:");
        add(JLcustomerID);

        JLabel JLgetCustomerID = new JLabel("" + customerID + "");
        add(JLgetCustomerID);

        JLabel JLcustomerName = new JLabel("Naam:");
        add(JLcustomerName);

        JLabel JLgetCustomerName = new JLabel(customerName);
        add(JLgetCustomerName);

        JLabel JLdeliveryAddressLine2 = new JLabel("Adres:");
        add(JLdeliveryAddressLine2);

        JLabel JLgetDeliveryAddressLine2 = new JLabel(deliveryAddressLine2);
        add(JLgetDeliveryAddressLine2);

        JLabel JLcityName = new JLabel("Plaats:");
        add(JLcityName);

        JLabel JLgetCityName = new JLabel(cityName);
        add(JLgetCityName);

        JLabel JLdeliveryPostalCode = new JLabel("Postcode:");
        add(JLdeliveryPostalCode);

        JLabel JLgetDeliveryPostalCode = new JLabel(deliveryPostalCode);
        add(JLgetDeliveryPostalCode);

        JLabel JLphoneNumber = new JLabel("Telefoonnummer:");
        add(JLphoneNumber);

        JLabel JLgetPhoneNumber = new JLabel(phoneNumber);
        add(JLgetPhoneNumber);

        if (previousScreen.equals("RoutingScreen")) {
            vulOpenstaandeBestellingenLijst(this.customerID);
            table = new JTable();
            model = new DefaultTableModel();
            columnsName = new Object[]{
                    "Openstaande bestellingen"
            };
            model.setColumnIdentifiers(columnsName);
            maakTabel();

            //add the table to the frame
            this.add(new JScrollPane(table), BorderLayout.PAGE_END);
        }

        setVisible(true);
    }

    public void maakTabel() {
        try {
            rowData = new Object[1];

            for (int i = 0; i < openstaandeBestellingen.size(); i++) {
                rowData[0] = openstaandeBestellingen.get(i);
                model.addRow(rowData);
            }
            table.setModel(model);

        } catch (NullPointerException e) {
            System.out.println("Nullpointerexeption");
        }
    }

    public void vulOpenstaandeBestellingenLijst(int customer_ID) throws SQLException {
        openstaandeBestellingen = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        ResultSet rs = dbConnection.getOrdersFromCustomer(customer_ID);
        while (rs.next()) {
            openstaandeBestellingen.add(rs.getInt("OrderID"));
        }
    }
}

