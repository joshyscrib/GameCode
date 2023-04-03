import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class CutSceneModal {
    Image fireballImg;

    public void showModal(JFrame parent){

        
        JDialog modelDialog = new JDialog(MainThing.gameFrame, "do you want to have a bad time?", true);
        JPanel panel = new JPanel();
        panel.setSize(600,400);
        JLabel lbl = new JLabel("Hey");
        lbl.setSize(400,300);
        panel.add(lbl);
        File videoFile = new File("");
        Media m = new Media(videoFile.toURI().toString());
        MediaPlayer player = new MediaPlayer(m);
        MediaView viewer = new MediaView(player);

        JFXPanel jfxPanel = new JFXPanel();
        panel.add(jfxPanel);
     
        //modelDialog.add(panel);
        modelDialog.setSize(600,400);
        modelDialog.setLocation(300,300);
        modelDialog.pack();
        modelDialog.setVisible(true);
        modelDialog.setResizable(false);
        
    }
}
