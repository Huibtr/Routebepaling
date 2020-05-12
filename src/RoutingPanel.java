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
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RoutingPanel extends JPanel {
    private Coordination coordination;
    private int bolGrootte = 5, x = 20, yStart = 20;
    private ArrayList<Hamiltonian> hamiltonian;

    public RoutingPanel(Coordination coordination, ArrayList<Hamiltonian> hamiltonian){
        this.coordination = coordination;
        this.setPreferredSize(new Dimension(500, 400));
        this.hamiltonian = hamiltonian;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);

            if(hamiltonian == null){
                System.out.println("leeg");
            } else {
                for (int i = 0; i < hamiltonian.size(); i++) {
                    System.out.println(hamiltonian);
           int beginX = hamiltonian.get(i).getBeginX();
           int beginY = hamiltonian.get(i).getBeginY();
           int endX = hamiltonian.get(i).getEindX();
           int endY = hamiltonian.get(i).getEindY();
           g.setColor(Color.black);
           g.fillOval(beginX,beginY,bolGrootte, bolGrootte);
           g.fillOval(endX, endY,bolGrootte, bolGrootte);
                }
            }
        }

    public void setHamiltonian(ArrayList<Hamiltonian> hamiltonian) {
        this.hamiltonian = hamiltonian;
    }
}

