import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderInfoScreen extends JFrame {
    private int customerID;
    private int orderID;
    private int stockItemID;
    private int quantity;
    private ArrayList<Stockitem> stockitems;

    public OrderInfoScreen(int orderID) throws SQLException {
        this.orderID = orderID;
        getStockitem(orderID);
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = dbConnection.getOrdersInfo(orderID);

        while (resultSet.next()){
            customerID = resultSet.getInt("CustomerID");
            orderID = resultSet.getInt("OrderID");
            stockItemID = resultSet.getInt("StockItemID");
            quantity = resultSet.getInt("Quantity");
        }

        setTitle("NerdyGadgets - Bestelling " + orderID);
        setSize(500, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        JLabel JLbestelNummer = new JLabel("Bestelnummer:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(JLbestelNummer, c);

        JLabel JLgetBestelNummer = new JLabel("" + orderID);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        add(JLgetBestelNummer, c);

        JLabel JLklantID = new JLabel("KlantID:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        add(JLklantID, c);

        JLabel JLgetKlantID = new JLabel("" + customerID);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 3;
        c.gridy = 0;
        add(JLgetKlantID, c);

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel();

        Object[] columnsName = new Object[] {
                "ProductID", "Beschrijving", "Aantal"
        };

        model.setColumnIdentifiers(columnsName);

        Object[] rowData = new Object[3];

        for(int i = 0; i < stockitems.size(); i++){

            rowData[0] = stockitems.get(i).getStockItemID();
            rowData[1] = stockitems.get(i).getDescription();
            rowData[2] = stockitems.get(i).getPickedQuantity();
            model.addRow(rowData);
        }

        table.setModel(model);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 200;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        add(new JScrollPane(table), c);

//        JLabel JLbestelNummer = new JLabel("Bestelnummer:");
//        add(JLbestelNummer);
//        JLabel JLgetBestelNummer = new JLabel("" + orderID);
//        add(JLgetBestelNummer);
//        JLabel JLklantID = new JLabel("KlantID:");
//        add(JLklantID);
//        JLabel JLgetKlantID = new JLabel("" + customerID);
//        add(JLgetKlantID);


        setVisible(true);
    }

    public void getStockitem(int orderID){
        stockitems = new ArrayList<>();
        Stockitem stockitem;
        try {

            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getStockitem(this.orderID);

            while(rs.next()){

                stockitem = new Stockitem(
                        rs.getInt("StockItemID"),
                        rs.getString("Description"),
                        rs.getInt("PickedQuantity")
                );

                stockitems.add(stockitem);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
