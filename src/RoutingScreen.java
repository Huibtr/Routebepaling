import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoutingScreen extends JFrame implements ActionListener {
    private Coordination coordination;
    private JTable gegevens;
    private JButton jbBack;
    private ArrayList<Route> routelijst;
    private String[] provincies;
    private JComboBox provincielijst;
    ComboBoxProvincies provinciesBox;

    public RoutingScreen(){
        DBConnection dbConnection = new DBConnection();
        setLayout(new BorderLayout());
        provinciesBox = new ComboBoxProvincies();
       add(provinciesBox, BorderLayout.PAGE_START);
       dbConnection.getRouteInfo(provinciesBox.getProvincieNaam());





        setSize(530, 500);
        RoutingPanel panel = new RoutingPanel(coordination);
        add(panel, BorderLayout.LINE_START);
//        jbBack = new JButton("terug");
//        jbBack.addActionListener(this);
//        add(jbBack, BorderLayout.PAGE_START);



        getRouteInfo();
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        Object[] columnsName = new Object[] {
                "Naam", "Adress", "Stad", "OrderID"
        };
        model.setColumnIdentifiers(columnsName);
        Object[] rowData = new Object[4];
        for(int i = 0; i < routelijst.size(); i++){
            rowData[0] = routelijst.get(i).getName();
            rowData[1] = routelijst.get(i).getAdress();
            rowData[2] = routelijst.get(i).getStad();
            rowData[3] = routelijst.get(i).getOrderId();
            model.addRow(rowData);
        }
        table.setModel(model);
        //add the table to the frame
        this.add(new JScrollPane(table), BorderLayout.PAGE_END);
        this.setTitle("NerdyGadgets - Klantgegevens");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        ListSelectionModel modelclick = table.getSelectionModel();
    }

    public void getRouteInfo(){
        routelijst = new ArrayList<>();
        Route route;
        try {
            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getRouteInfo(provinciesBox.getProvincieNaam());
            while(rs.next()){
                route = new Route(
                        rs.getString("CustomerName"),
                        rs.getString("DeliveryAddressLine1"),
                        rs.getString("CityName"),
                        rs.getInt("OrderID")
                );
                routelijst.add(route);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbBack) {
            LogInScreen LoginScreen = new LogInScreen();
            dispose();

        }

    }

}
