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
    private int bolGrootte = 10, x = 1, yStart = 1;
    private ArrayList<Hamiltonian> hamiltonian;

    public RoutingPanel(Coordination coordination, ArrayList<Hamiltonian> hamiltonian){
        this.coordination = coordination;
        this.setPreferredSize(new Dimension(500, 400));
        this.hamiltonian = hamiltonian;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);

        int endX = 0;
        int endY = 0;
        int beginX = 0;
        int beginY = 0;
            if(hamiltonian == null){
                System.out.println("leeg");
            } else {
                for (int i = 0; i < hamiltonian.size(); i++) {
                    System.out.println(hamiltonian);
                   beginX = hamiltonian.get(i).getBeginX();
                   beginY = hamiltonian.get(i).getBeginY();
                   endX = hamiltonian.get(i).getEindX();
                   endY = hamiltonian.get(i).getEindY();
                   g.setColor(Color.black);
                   g.fillOval(beginX,beginY,1, 1);
                   g.fillOval(endX, endY,1, 1);
                   g.setColor(Color.blue);
                   g.drawLine(beginX,beginY,endX,endY);


                }
                g.drawLine(0,0, beginX,beginY);
            }
        }

    public void setHamiltonian(ArrayList<Hamiltonian> hamiltonian) {
        this.hamiltonian = hamiltonian;
    }
}

