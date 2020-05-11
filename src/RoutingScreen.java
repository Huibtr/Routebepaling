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
    ComboBoxProvincies provinciesBox;
    private String provincieNaam;
    private DefaultTableModel model;
    private JTable table;
    private JButton comboBox;
    private Object[] columnsName;
    private Object[] rowData;


    public RoutingScreen(){
        if(provincieNaam == null){
            provincieNaam = "Overijssel";
        }
        provinciesBox = new ComboBoxProvincies();
        add(provinciesBox, BorderLayout.PAGE_START);
        comboBox = new JButton("Refresh");
        comboBox.addActionListener(this);
        add(comboBox, BorderLayout.CENTER);

        setSize(530, 500);
        RoutingPanel panel = new RoutingPanel(coordination);
        add(panel, BorderLayout.LINE_START);

        getRouteInfo(provincieNaam);
        table = new JTable();
        model = new DefaultTableModel();
        columnsName = new Object[] {
                "Naam", "Adress", "Stad", "OrderID"
        };
        model.setColumnIdentifiers(columnsName);
        maakTabel();

        //add the table to the frame
        this.add(new JScrollPane(table), BorderLayout.PAGE_END);
        this.setTitle("NerdyGadgets - ordergegevens");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        setVisible(true);




        //add the table to the frame

    }

    public void getRouteInfo(String provincieNaam){
        System.out.println(provincieNaam);
        routelijst = new ArrayList<>();
        Route route;
        try {
            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getRouteInfo(provincieNaam);
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
        if(e.getSource() == comboBox){
            if (provincieNaam == null){
                this.provincieNaam = "Utrecht";
            }
            maakTabelLeeg();
            getRouteInfo(provinciesBox.getProvincieNaam());
            maakTabel();
            repaint();

        }




    }

    public void maakTabel() {
        try {
            rowData = new Object[4];

            for(int i = 0; i < routelijst.size(); i++){
                rowData[0] = routelijst.get(i).getName();
                rowData[1] = routelijst.get(i).getAdress();
                rowData[2] = routelijst.get(i).getStad();
                rowData[3] = routelijst.get(i).getOrderId();
                model.addRow(rowData);

            }
            table.setModel(model);


        } catch (NullPointerException e) {
            System.out.println("Nullpointerexeption");
        }


    }

    public void maakTabelLeeg(){
        routelijst.clear();
        model.setRowCount(0);







    }

    public void verwijderTabel(){

    }
}

