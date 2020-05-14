import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private TSP tsp;
    private ArrayList<Hamiltonian> hamiltonians;
    private RoutingPanel panel;


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
        panel = new RoutingPanel(coordination, hamiltonians);
        add(panel, BorderLayout.LINE_START);

        getRouteInfo(provincieNaam);
        table = new JTable();
        model = new DefaultTableModel();
        columnsName = new Object[] {
                "Naam", "Adress", "Stad", "OrderID"
        };
        model.setColumnIdentifiers(columnsName);

        tsp = new TSP();
        tsp.leegcoordinaten();
        hamiltonian(provincieNaam);
        maakTabel();

        //add the table to the frame
        this.add(new JScrollPane(table), BorderLayout.PAGE_END);
        this.setTitle("NerdyGadgets - ordergegevens");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        setVisible(true);
        ListSelectionModel modelclick = table.getSelectionModel();
        modelclick.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!modelclick.isSelectionEmpty()) {
                    int customerID = 0;
                    int selectrow = modelclick.getMinSelectionIndex();
                    for (int i = 0; i < routelijst.size(); i++) {
                        if (i == selectrow) {
                            customerID = routelijst.get(i).getCustomerID();
                        }
                    }
                    try {
                        if (listSelectionEvent.getValueIsAdjusting()) {
                            CustomerInfoScreen customerInfoScreen = new CustomerInfoScreen(customerID);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }






        //add the table to the frame



    public void getRouteInfo(String provincieNaam){
        System.out.println(provincieNaam);
        routelijst = new ArrayList<>();
        Route route;
        try {
            DBConnection dbConnection = new DBConnection();
            if(hamiltonians != null){
            for (int i = 0; i < hamiltonians.size(); i++){
                ResultSet rs = dbConnection.getRouteInfo(hamiltonians.get(i).getEindX(), hamiltonians.get(i).getEindY());
                while(rs.next()) {
                    route = new Route(
                            rs.getString("CustomerName"),
                            rs.getString("DeliveryAddressLine1"),
                            rs.getString("CityName"),
                            rs.getInt("OrderID"),
                            rs.getInt("CustomerID")
                    );
                    routelijst.add(route);
                }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void hamiltonian(String provincie){
        try {
            tsp = new TSP();
            if (hamiltonians == null){
                hamiltonians = tsp.berekenAfstand(provincie);
            } else {
                hamiltonians.clear();
                hamiltonians = tsp.berekenAfstand(provincie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == comboBox){
            maakTabelLeeg();
            hamiltonian(provinciesBox.getProvincieNaam());
            panel.setHamiltonian(hamiltonians);
            repaint();
            getRouteInfo(provinciesBox.getProvincieNaam());
            maakTabel();




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

