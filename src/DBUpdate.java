import javax.xml.crypto.Data;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DBUpdate {

    public void InsertRouteArchive(double afstand, ArrayList<Hamiltonian> hamiltonian) throws SQLException, ClassNotFoundException {
        DBConnection connection = new DBConnection();
        Connection con = connection.getConnection();
        try
        {
            if(hamiltonian != null) {
                String query = "insert into route_archive (Datum, Afstand, EmployeeID) values(?, ?, ?);";

                PreparedStatement preparedStmt = con.prepareStatement(query);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                preparedStmt.setDate(1, Date.valueOf(formatter.format(date)));
                preparedStmt.setDouble(2, afstand);
                preparedStmt.setInt(3, 1);

                preparedStmt.executeUpdate();

                int routeID = 0;
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select RouteID from route_archive ORDER BY RouteID DESC LIMIT 1 ");
                while (rs.next()) {
                    routeID = rs.getInt("RouteID");
                }
                InsertRouteLine(routeID, con, hamiltonian);
                con.close();
            }
        }
        catch (Exception e)
        {
            System.err.println("exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void InsertRouteLine(int routeID, Connection connection, ArrayList<Hamiltonian> hamiltonian) throws SQLException, ClassNotFoundException {
        for(int setRouteLine = 0; setRouteLine < hamiltonian.size(); setRouteLine ++){
            int customerID  = 0;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select CustomerID from address_coordinate where X = "+ hamiltonian.get(setRouteLine).getEindX() + " and Y =" + hamiltonian.get(setRouteLine).getEindY()  + ";");
            while (rs.next()){
                customerID = rs.getInt("CustomerID");
            }

            DBConnection con = new DBConnection();
            ResultSet rsOrders = con.getOrdersFromCustomer(customerID);
            while (rsOrders.next()){
                try
                {
                    int orderID = rsOrders.getInt("OrderID");
                        String query = "insert into routeline (RouteID, CustomerID, OrderID, X, Y) values(?, ?, ?, ?,?);";

                        PreparedStatement preparedStmt = connection.prepareStatement(query);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date(System.currentTimeMillis());
                        preparedStmt.setInt(1, routeID);
                        preparedStmt.setInt(2, customerID);
                        preparedStmt.setInt(3, orderID);
                        preparedStmt.setInt(4, hamiltonian.get(setRouteLine).getEindX());
                        preparedStmt.setInt(5, hamiltonian.get(setRouteLine).getEindY());


                        preparedStmt.executeUpdate();

                        UpdateOrders(orderID,connection);

                }
                catch (Exception e)
                {
                    System.err.println("exception! ");
                    System.err.println(e.getMessage());
                }
            }

            System.out.println(" klant " + setRouteLine + " : " + customerID);
        }
    }

    public void UpdateOrders(int orderID, Connection conn) throws SQLException {
        try
        {
            // create our java preparedstatement using a sql update query
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Orders SET IsPicked = ? WHERE OrderID = ?");

            // set the preparedstatement parameters
            ps.setInt(1,1);
            ps.setInt(2,orderID);

            // call executeUpdate to execute our sql update statement
            ps.executeUpdate();
            ps.close();
        }
        catch (SQLException se)
        {
            // log the exception
            throw se;
        }
    }
}
