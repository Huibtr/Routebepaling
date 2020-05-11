import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuantityInfoScreen extends JFrame{
    private String StockItemName;
    private int StockItemID;
    private int QuantityOnHand;
    private ArrayList<Stockitem> stockitems;

    public QuantityInfoScreen (int productID) throws SQLException {
        this.StockItemID = productID;
        getQuantity(StockItemID);
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = dbConnection.getQuantityInfo(StockItemID);

        while (resultSet.next()){
            StockItemName = resultSet.getString("StockItemName");
            StockItemID = resultSet.getInt("StockItemID");
            QuantityOnHand = resultSet.getInt("QuantityOnHand");
        }
        setTitle("NerdyGadgets - Product " + StockItemID);
        setSize(500, 200);
        setLayout(new GridLayout(3,2));

        JLabel JLStockItemName = new JLabel ("Productnaam: ");
        add (JLStockItemName);

        JLabel JLgetStockItemName = new JLabel (StockItemName);
        add (JLgetStockItemName);

        JLabel JLStockItemID = new JLabel ("Productnummer: ");
        add (JLStockItemID);

        JLabel JLgetStockItemID = new JLabel ("" + StockItemID);
        add (JLgetStockItemID);

        JLabel JLQuantityOnHand = new JLabel ("Voorraad: ");
        add (JLQuantityOnHand);

        JLabel JLgetQuantityOnHand = new JLabel ("" + QuantityOnHand);
        add (JLgetQuantityOnHand);

        setVisible(true);
    }

    public void getQuantity(int stockItemID){
        stockitems = new ArrayList<>();
        Stockitem stockitem;
        try {

            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getQuantityInfo(this.StockItemID);

            while(rs.next()){

                stockitem = new Stockitem(
                        rs.getInt("StockItemID"),
                        rs.getString("StockItemName"),
                        rs.getInt("QuantityOnHand")
                );

                stockitems.add(stockitem);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
