import javax.swing.*;
import java.sql.SQLException;

public class Program {
    public static void main(String[] args) throws SQLException {
//        HomeScreen homeScreen = new HomeScreen();test
//        homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TSP tsp = new TSP();
        tsp.addcordinaten();
        tsp.berekenAfstand();
    }
}
