import javax.swing.*;
import java.sql.SQLException;

public class Program {
    public static void main(String[] args) throws SQLException {
        LogInScreen loginscherm = new LogInScreen();

            TSP tsp = new TSP();
            tsp.addcordinaten();
            tsp.berekenAfstand();
    }
}
