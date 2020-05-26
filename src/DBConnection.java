import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private String databaseName = "wideworldimporters";
    private String url = "jdbc:mysql://localhost:3306/" + databaseName;
    private String username = "root";
    private String password = "";
    private ResultSet result;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public ResultSet getPassword(String hashPassword){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("select Password, department, Username from employee where Password = \"" + hashPassword + "\" ;");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }



    public ResultSet getCustomers(){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

             result = query.executeQuery("select CustomerID, CustomerName, CityName, DeliveryAddressLine2, DeliveryPostalCode, PhoneNumber from customers inner join cities where customers.DeliveryCityID =  cities.CityID;");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

    public ResultSet getCustomersInfo(int customerID){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("select CustomerID, CustomerName, CityName, DeliveryAddressLine2, DeliveryPostalCode, PhoneNumber from customers inner join cities where customers.DeliveryCityID =  cities.CityID and customers.CustomerID ="+ customerID +";");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

    public ResultSet getOrderlines(){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("SELECT distinct orders.CustomerID, orders.OrderID FROM orders Where CustomerID < 4;");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

    public ResultSet getOrdersInfo(int orderID){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("SELECT orders.CustomerID, orders.OrderID, orderlines.StockitemID, orderlines.Quantity FROM orderlines LEFT JOIN orders ON orders.OrderID=orderlines.OrderID Where CustomerID < 4 and orders.OrderID =" + orderID + ";");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

    public ResultSet getStockitem(int orderID){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("SELECT StockItemID, Description, PickedQuantity FROM orderlines where OrderID = " + orderID + ";");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

    public ResultSet getQuantity(){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("SELECT StockItemID, StockItemName FROM stockitems ORDER BY StockItemID;");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

    public ResultSet getQuantityInfo(int StockItemID){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("SELECT stockitems.StockItemName, stockitems.StockItemID, stockitemholdings.QuantityOnHand FROM stockitems JOIN stockitemholdings ON stockitems.StockItemID = stockitemholdings.StockItemID WHERE stockitems.StockItemID = " + StockItemID + ";");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

    public ResultSet getCoordinates(String provicie){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("SELECT X,Y FROM address_coordinate\n" +
                    "JOIN orders ON orders.CustomerID = address_coordinate.CustomerID\n" +
                    "WHERE Provincie = \"" + provicie + "\" and IsPicked = " + 0);

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

    public ResultSet getRouteInfo(int x, int y){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("SELECT DISTINCT customers.CustomerName, customers.DeliveryAddressLine2, cities.CityName, customers.customerID\n" +
                    "FROM customers \n" +
                    "JOIN orders ON customers.CustomerID = orders.CustomerID\n" +
                    "JOIN cities ON customers.DeliveryCityID = cities.CityID\n" +
                    "JOIN address_coordinate ON customers.CustomerID = address_coordinate.CustomerID\n" +
                    "WHERE X =" + x + " AND Y =" + y );

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }


    public ResultSet getOrdersFromCustomer(int customerID){
        try {
            Connection connection = getConnection();
            Statement query = connection.createStatement();

            result = query.executeQuery("SELECT OrderID FROM orders WHERE CustomerID = " + customerID + " ORDER BY OrderID");

        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
        catch (SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null, ex);
            ex.printStackTrace();
        }
        return result;
    }

}

