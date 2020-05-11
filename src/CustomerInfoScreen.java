import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerInfoScreen extends JFrame {
    private int customerID;
    private String customerName;
    private String deliveryAddressLine2;
    private String cityName;
    private String deliveryPostalCode;
    private String phoneNumber;

    public CustomerInfoScreen(int customerID) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = dbConnection.getCustomersInfo(customerID);

        while (resultSet.next()){
            customerID = resultSet.getInt("CustomerID");
            customerName = resultSet.getString("CustomerName");
            cityName = resultSet.getString("CityName");
            deliveryAddressLine2 = resultSet.getString("DeliveryAddressLine2");
            deliveryPostalCode = resultSet.getString("DeliveryPostalCode");
            phoneNumber = resultSet.getString("PhoneNumber");
        }
        setTitle("NerdyGadgets - " + customerName);
        setSize(500, 300);
        setLayout(new GridLayout(8,2));

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

        setVisible(true);
    }
}
