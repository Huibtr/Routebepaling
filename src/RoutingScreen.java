import javax.swing.*;
import javax.swing.border.Border;
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
    private JLabel afstand;
    private int totalDistance;
    private String[] distanceOption = {"25", "50", "75", "100", "125", "150", "175", "200", "225", "250" };
    private JComboBox cmbDistance;
    private JButton jbUpdateRoute;

    public RoutingScreen(){
        if(provincieNaam == null){
            provincieNaam = "Overijssel";
        }
        JPanel userPanel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        userPanel.setBorder(padding);

        userPanel.setLayout(new GridLayout(1,6));

        jbBack = new JButton("\uD83E\uDC80 Uitloggen");
        jbBack.addActionListener(this);
        userPanel.add(jbBack);

        JLabel jlNep = new JLabel(" ");
        userPanel.add(jlNep);

        afstand = new JLabel("0 km");
        userPanel.add(afstand);

        cmbDistance = new JComboBox(distanceOption);
        cmbDistance.setSelectedIndex(3);
        cmbDistance.addActionListener(this);
        userPanel.add(cmbDistance);

        provinciesBox = new ComboBoxProvincies();
        userPanel.add(provinciesBox);

        //add(provinciesBox, BorderLayout.PAGE_START);
        comboBox = new JButton("Bereken afstand");
        comboBox.addActionListener(this);



        //add(comboBox, BorderLayout.CENTER);
        userPanel.add(comboBox);


        jbUpdateRoute = new JButton("bevestig route");
        jbUpdateRoute.addActionListener(this);
        userPanel.add(jbUpdateRoute);

        add(userPanel, BorderLayout.PAGE_START);

        setSize(530, 500);
        panel = new RoutingPanel(coordination, hamiltonians);
        add(panel);

        getRouteInfo(provincieNaam);
        table = new JTable();
        model = new DefaultTableModel();
        columnsName = new Object[] {
                "Volgorde", "Naam", "Adress", "Stad"
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
                    int selectrow = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()) -1;
                    System.out.println(routelijst.size());
                    for (int i = 0; i < routelijst.size(); i++) {
                        if (i == selectrow) {
                            customerID = routelijst.get(i).getCustomerID();
                        }
                    }
                    try {
                        if (listSelectionEvent.getValueIsAdjusting()) {
                            CustomerInfoScreen customerInfoScreen = new CustomerInfoScreen(customerID, "RoutingScreen");
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
                                rs.getString("DeliveryAddressLine2"),
                                rs.getString("CityName"),
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
                hamiltonians = tsp.berekenAfstand(provincie, totalDistance);
            } else {
                hamiltonians.clear();
                hamiltonians = tsp.berekenAfstand(provincie,totalDistance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbBack){
            LogInScreen logInScreen = new LogInScreen();
            dispose();
        }
        if(e.getSource() == comboBox){
            String selected = cmbDistance.getSelectedItem().toString();
            totalDistance = Integer.parseInt(selected);
            maakTabelLeeg();
            hamiltonian(provinciesBox.getProvincieNaam());
            panel.setHamiltonian(hamiltonians);
            if(hamiltonians.size() != 0 ){
                afstand.setText(String.format("%.2f", hamiltonians.get((hamiltonians.size() - 1)).getTotaalAfstand()) + " km");
            }else {
                afstand.setText(Double.toString(0.00) + " km");
            }


            getRouteInfo(provinciesBox.getProvincieNaam());
            maakTabel();
            repaint();
        }

        if(e.getSource() == jbUpdateRoute){
            DBUpdate dbUpdate = new DBUpdate();
            try {
                dbUpdate.InsertRouteArchive(hamiltonians.get((hamiltonians.size() - 1)).getTotaalAfstand(), hamiltonians);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void maakTabel() {
        try {
            rowData = new Object[4];

            int volgNummer = 1;

            for(int i = 0; i < routelijst.size(); i++){
                    rowData[0] = volgNummer;
                    rowData[1] = routelijst.get(i).getName();
                    rowData[2] = routelijst.get(i).getAdress();
                    rowData[3] = routelijst.get(i).getStad();
                    model.addRow(rowData);
                    volgNummer++;
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

