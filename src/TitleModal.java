import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class TitleModal implements MouseListener{
   boolean clicked = false;

   JDialog modelDialog;

    public void showModal(){
         modelDialog = new JDialog(MainThing.gameFrame, "Horrificus", true);

        JPanel panel = new JPanel();
        panel.setSize(800,600);
        modelDialog.addMouseListener(this);
        modelDialog.add(panel);

        URL img = getClass().getClassLoader().getResource("images/titleScreen.png");
        try{
        Image image = ImageIO.read(img);
        JLabel picLabel = new JLabel(new ImageIcon(image));
        panel.add(picLabel);
        }
        catch(IOException ex){
            System.out.println("EXCEPTION ):  " + ex);
        }

        modelDialog.setPreferredSize(new Dimension(1536, 1086));

        modelDialog.setLocation(800,400);
        modelDialog.pack();
        modelDialog.setVisible(true);
        modelDialog.setResizable(false);
   

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        modelDialog.dispose();
        clicked = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clicked = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clicked = true;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        clicked = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
        clicked = true;
    }
    
}