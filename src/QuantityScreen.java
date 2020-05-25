import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class QuantityScreen extends JFrame implements ActionListener{
    private ArrayList<Quantity> quantities;
    private JButton JBterug;
    private JTextField JTinputSearchCustomerID;
    private JButton JBSearch;

    public QuantityScreen() {
        //Lijst met producten vullen
        getQuantity();

        //Layout wijzigen
        this.setTitle("NerdyGadgets - Producten");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Buttons, labels en een tekstveld toevoegen aan het scherm
        JBterug = new JButton("\uD83E\uDC80 terug");
        JBterug.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(JBterug, c);

        JLabel JLSearch = new JLabel("Zoeken op productID: ");
        add(JLSearch);

        JTinputSearchCustomerID = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 1;
        c.gridx = 3;
        c.gridy = 0;
        add(JTinputSearchCustomerID, c);


        JBSearch = new JButton("Zoek product");
        JBSearch.addActionListener(this);
        add(JBSearch);

        //Nieuwe tabel aanmaken
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        Object[] columnsName = new Object[] {
                "ProductID", "Productnaam"
        };
        model.setColumnIdentifiers(columnsName);
        Object[] rowData = new Object[2];
        for(int i = 0; i < quantities.size(); i++){
            rowData[0] = quantities.get(i).getStockItemID();
            rowData[1] = quantities.get(i).getStockItemName();
            model.addRow(rowData);
        }
        table.setModel(model);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 200;
        c.weightx = 0.0;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        //Tabel toevoegen aan het scherm
        add(new JScrollPane(table), c);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        //Ervoor zorgen dat je op de rijen in de tabel kunt klikken en dan naar een specifieke pagina gaat
        ListSelectionModel modelclick = table.getSelectionModel();
            modelclick.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    if (!modelclick.isSelectionEmpty()){
                        int productID = 0;
                        int selectrow = modelclick.getMinSelectionIndex();
                        for (int i = 0; i < quantities.size(); i++){
                            if(i == selectrow){
                                productID = quantities.get(i).getStockItemID();
                            }
                        }
                        try {
                            if (listSelectionEvent.getValueIsAdjusting()) {
                                //Nieuwe specifieke pagina openen
                                QuantityInfoScreen QuantityInfoScreen = new QuantityInfoScreen(productID);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


        }

    public void getQuantity(){
        //Lijst met producten aanmaken
        quantities = new ArrayList<>();
        Quantity quantity;

        try {
            //Nieuwe databaseconnectie aanmaken
            DBConnection dbConnection = new DBConnection();
            ResultSet rs = dbConnection.getQuantity();

            //Lijst met producten vullen met gegevens uit de database
            while(rs.next()){
                quantity = new Quantity(
                        rs.getInt("StockItemID"),
                        rs.getString("StockItemName")
                );
                quantities.add(quantity);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Knoppen werkend maken
        if (e.getSource() == JBterug) {
            dispose();
            DataScreen dataScreen = new DataScreen();
        }
        if (e.getSource() == JBSearch) {
            boolean heeftSchermGeopend = false;
            boolean foutmelding = false;
            try {
                for (Quantity q : quantities) {
                    if (q.getStockItemID() == Integer.parseInt(JTinputSearchCustomerID.getText())) {
                        try {
                            //Nieuwe specifieke pagina openen
                            QuantityInfoScreen quantityInfoScreen = new QuantityInfoScreen(Integer.parseInt(JTinputSearchCustomerID.getText()));
                            heeftSchermGeopend = true;
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }catch (NumberFormatException nfe) {
                //Foutmelding teruggeven
                JOptionPane.showMessageDialog(null, "Getal invoeren!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                foutmelding = true;
            }
            if (heeftSchermGeopend == false && foutmelding == false) {
                //Foutmelding teruggeven
                JOptionPane.showMessageDialog(null, "Verkeerde invoer!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
