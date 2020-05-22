import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class OrderScreen extends JFrame implements ActionListener {
    private ArrayList<Order> orders;
    private JButton JBterug;
    private JTextField JTinputSearchCustomerID;
    private JButton JBSearch;

    public OrderScreen() {
        getOrder();
        this.setTitle("NerdyGadgets - Bestellingen");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JBterug = new JButton("\uD83E\uDC80 terug");
        JBterug.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(JBterug, c);

        JLabel JLSearch = new JLabel("Zoeken op bestellingID: ");
        add(JLSearch);

        JTinputSearchCustomerID = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 1;
        c.gridx = 3;
        c.gridy = 0;
        add(JTinputSearchCustomerID, c);


        JBSearch = new JButton("Zoek bestelling");
        JBSearch.addActionListener(this);
        add(JBSearch);

        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        Object[] columnsName = new Object[] {
                "KlantID", "BestellingID"
        };
        model.setColumnIdentifiers(columnsName);
        Object[] rowData = new Object[2];
        for(int i = 0; i < orders.size(); i++){

            rowData[0] = orders.get(i).getCustomerID();
            rowData[1] = orders.get(i).getOrderID();
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
                    int orderID = 0;
                    int selectrow = modelclick.getMinSelectionIndex();
                    for (int i = 0; i < orders.size(); i++){
                        if(i == selectrow){
                            orderID = orders.get(i).getOrderID();
                        }
                    }
                    try {
                        if (listSelectionEvent.getValueIsAdjusting()) {
                            OrderInfoScreen orderInfoScreen = new OrderInfoScreen(orderID);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void getOrder(){

        orders = new ArrayList<>();
        Order order;
        try {

            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getOrderlines();

            while(rs.next()){

                order = new Order(
                        rs.getInt("CustomerID"),
                        rs.getInt("OrderID")
                );

                orders.add(order);
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
        if (e.getSource() == JBSearch) {
            boolean heeftSchermGeopend = false;
            boolean foutmelding = false;
            try {
                for (Order o : orders) {
                    if (o.getOrderID() == Integer.parseInt(JTinputSearchCustomerID.getText())) {
                        try {
                            OrderInfoScreen orderInfoScreen = new OrderInfoScreen(Integer.parseInt(JTinputSearchCustomerID.getText()));
                            heeftSchermGeopend = true;
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Getal invoeren!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                foutmelding = true;
            }
            if (heeftSchermGeopend == false && foutmelding == false) {
                JOptionPane.showMessageDialog(null, "Verkeerde invoer!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
