import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;

public class MainThing {
    public static void main(String[] args){
        JFrame myFrame = new JFrame("game");
        Panel myPanel = new LevelOne();
        myFrame.add(myPanel);
        myFrame.setVisible(true);
        myFrame.setSize(500,500);
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



        MenuBar bar = new MenuBar();

        Menu main = new Menu("Tiles");
        bar.add(main);

        MenuItem GrassItem = new MenuItem("Grass");
        main.add(GrassItem);
        GrassItem.addActionListener(action -> myPanel.setMenuTile(GrassTile.class));
        MenuItem WallItem = new MenuItem("Wall");
        main.add(WallItem);
        WallItem.addActionListener(action -> myPanel.setMenuTile(WallTile.class));
        MenuItem WoodItem = new MenuItem("Wood");
        main.add(WoodItem);
        WoodItem.addActionListener(action -> myPanel.setMenuTile(WoodTile.class));
        

        Menu file = new Menu("File");
        bar.add(file);
        MenuItem SaveItem = new MenuItem("Save");
        file.add(SaveItem);
        MenuItem LoadItem = new MenuItem("Load");
        file.add(LoadItem);
        SaveItem.addActionListener(action -> myPanel.save());
        LoadItem.addActionListener(action -> myPanel.load());

        myFrame.setMenuBar(bar);

     
        Thread myThread = new Thread(myPanel);
        myThread.start();
    }

 
}