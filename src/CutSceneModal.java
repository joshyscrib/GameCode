import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class CutSceneModal {
    Image fireballImg;

    public void showModal(JFrame parent, String imagePath){

        com.sun.javafx.application.PlatformImpl.startup(()->{});
        JDialog modelDialog = new JDialog(MainThing.gameFrame, "Horrificus", true);

        JPanel panel = new JPanel();
        panel.setSize(800,600);
        Platform.setImplicitExit(false);
        Platform.runLater(()-> {
            File videoFile = new File(imagePath);
            String uri = videoFile.toURI().toString();
            Media m = new Media(uri);
            MediaPlayer player = new MediaPlayer(m);
            player.setAutoPlay(true);
            MediaView viewer = new MediaView(player);
            viewer.setFitWidth(600);
            viewer.setFitHeight(400);
    
            viewer.setPreserveRatio(true);;
            player.setOnEndOfMedia(() -> {
               modelDialog.dispose();
            });
 
            JFXPanel jfxPanel = new JFXPanel();
            
            Group box = new Group();
            Scene s = new Scene(box, 380,275);
            jfxPanel.setScene(s);
    
        
            
            ((Group) s.getRoot()).getChildren().add(viewer);
            modelDialog.add(jfxPanel);


            modelDialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                  player.stop();
                }
              });
 
        });
      
       
     
        modelDialog.setPreferredSize(new Dimension(525, 425));

        modelDialog.setLocation(300,300);
        modelDialog.pack();
        modelDialog.setVisible(true);
        modelDialog.setResizable(false);
        
    }
}
