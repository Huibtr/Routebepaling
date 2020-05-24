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
        //productID opslaan
        this.StockItemID = productID;

        //Lijst met producten vullen
        getQuantity(StockItemID);

        //Nieuwe databaseconnectie aanmaken
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = dbConnection.getQuantityInfo(StockItemID);

        //Variabelen vullen met de gegevens van de database
        while (resultSet.next()){
            StockItemName = resultSet.getString("StockItemName");
            StockItemID = resultSet.getInt("StockItemID");
            QuantityOnHand = resultSet.getInt("QuantityOnHand");
        }

        //Layout wijzigen
        setTitle("NerdyGadgets - Product " + StockItemID);
        setSize(500, 200);
        setLayout(new GridLayout(3,2));

        //Labels toevoegen aan het scherm
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
        //Lijst met producten aanmaken
        stockitems = new ArrayList<>();
        Stockitem stockitem;

        try {
            //Nieuwe databaseconnectie aanmaken
            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getQuantityInfo(this.StockItemID);

            //Lijst met producten vullen met gegevens uit de database
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
