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
        Menu file = new Menu("File");
        
        MenuItem SaveItem = new MenuItem("Save");
        file.add(SaveItem);
        MenuItem LoadItem = new MenuItem("Load");
        file.add(LoadItem);
        SaveItem.addActionListener(action -> myPanel.save());
        LoadItem.addActionListener(action -> myPanel.load());
        Menu main = new Menu("Tiles");
        bar.add(file);
        
        Menu edit = new Menu("Edit");
        MenuItem clearItem = new MenuItem("Clear");
        edit.add(clearItem);
        clearItem.addActionListener(action -> myPanel.clearAll());
        bar.add(edit);
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
        MenuItem FloorItem = new MenuItem("Floor");
        main.add(FloorItem);
        FloorItem.addActionListener(action -> myPanel.setMenuTile(FloorTile.class));

        

        myFrame.setMenuBar(bar);

     
        Thread myThread = new Thread(myPanel);
        myThread.start();
    }

 
}