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

    public CustomerScreen() {
        getCustomer();
        this.setTitle("NerdyGadgets - Klanten");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JBterug = new JButton("\uD83E\uDC80 terug");
        JBterug.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(JBterug, c);

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
        //add the table to the frame
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 200;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        add(new JScrollPane(table), c);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

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
        customers = new ArrayList<>();
        Customer customer;
        try {
            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getCustomers();
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
        if (e.getSource() == JBterug) {
            dispose();
            DataScreen dataScreen = new DataScreen();
        }
    }
}


