import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RoutingPanel extends JPanel {
    private Coordination coordination;
    private int bolGrootte = 10, x = 20, yStart = 20;

    public RoutingPanel(Coordination coordination){
        this.coordination = coordination;
        this.setPreferredSize(new Dimension(500, 400));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        DBConnection dbConnection = new DBConnection();
        ResultSet result = dbConnection.getCoordinates();

        while (true){
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                int x = result.getInt("x");
                int y = result.getInt("Y");
                g.setColor(Color.blue);
                g.fillOval(x, y, bolGrootte, bolGrootte);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

