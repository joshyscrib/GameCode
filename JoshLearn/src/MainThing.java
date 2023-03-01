import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.sound.sampled.*;
import java.net.URL;
// hello test yay

public class MainThing {

    private String filename;
    private Player player; 
    private Clip clip;

    public MainThing(String fileName){
        this.filename = filename;
    }

    public void play() {
        try {   
            if(clip == null){
                URL url = this.getClass().getResource("images/dungeonMusic.mp3");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                // Get a clip resource.
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(5);
            }

          //  clip.loop(1);
        }
        catch (Exception e) {
       
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        
        // Creates frame and panel
        JFrame myFrame = new JFrame("game");
        Panel myPanel = new LevelOne("images/dungeonMusic.mp3");
        myFrame.add(myPanel);
        myFrame.setVisible(true);
        myFrame.setSize(900,900);
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon("images/wolf-2.png");
        myFrame.setIconImage(img.getImage());
        


        // Makes menu
        MenuBar bar = new MenuBar();
        Menu file = new Menu("File");
        // Creates 5 seperate save files for maps and adds to menu
        MenuItem SaveItem1 = new MenuItem("Save1");
        file.add(SaveItem1);
        MenuItem SaveItem2 = new MenuItem("Save2");
        file.add(SaveItem2);
        MenuItem SaveItem3 = new MenuItem("Save3");
        file.add(SaveItem3);
        MenuItem SaveItem4 = new MenuItem("Save4");
        file.add(SaveItem4);
        MenuItem SaveItem5 = new MenuItem("Save5");
        file.add(SaveItem5);
        // Creates 5 ways to load and adds to menu
        MenuItem LoadItem1 = new MenuItem("Load1");
        file.add(LoadItem1);
        MenuItem LoadItem2 = new MenuItem("Load2");
        file.add(LoadItem2);
        MenuItem LoadItem3 = new MenuItem("Load3");
        file.add(LoadItem3);
        MenuItem LoadItem4 = new MenuItem("Load4");
        file.add(LoadItem4);
        MenuItem LoadItem5 = new MenuItem("Load5");
        file.add(LoadItem5);
        // Adds function to the menu items
        SaveItem1.addActionListener(action -> myPanel.save("LevelOne.game"));
        SaveItem2.addActionListener(action -> myPanel.save("LevelTwo.game"));
        SaveItem3.addActionListener(action -> myPanel.save("LevelThree.game"));
        SaveItem4.addActionListener(action -> myPanel.save("LevelFour.game"));
        SaveItem5.addActionListener(action -> myPanel.save("LevelFive.game"));

        LoadItem1.addActionListener(action -> myPanel.load("LevelOne.game"));
        LoadItem1.addActionListener(action -> myPanel.curLevel = 1);
        LoadItem2.addActionListener(action -> myPanel.load("LevelTwo.game"));
        LoadItem2.addActionListener(action -> myPanel.curLevel = 2);
        LoadItem3.addActionListener(action -> myPanel.load("LevelThree.game"));
        LoadItem3.addActionListener(action -> myPanel.curLevel = 3);
        LoadItem4.addActionListener(action -> myPanel.load("LevelFour.game"));
        LoadItem4.addActionListener(action -> myPanel.curLevel = 4);
        LoadItem5.addActionListener(action -> myPanel.load("LevelFive.game"));
        LoadItem5.addActionListener(action -> myPanel.curLevel = 5);
        Menu main = new Menu("Tiles");
        bar.add(file);
        
        Menu edit = new Menu("Edit");
        MenuItem clearItem = new MenuItem("Clear");
        edit.add(clearItem);
        clearItem.addActionListener(action -> myPanel.clearAll());
        bar.add(edit);
        bar.add(main);
        // Adds game tiles to menu
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
        MenuItem BarrelItem = new MenuItem("Barrel");
        main.add(BarrelItem);
        BarrelItem.addActionListener(action -> myPanel.setMenuTile(BarrelTile.class));
        MenuItem BarsItem = new MenuItem("Floor Bars");
        main.add(BarsItem);
        BarsItem.addActionListener(action -> myPanel.setMenuTile(BarsTile.class));
        MenuItem SBarsItem = new MenuItem("Solid Floor Bars");
        main.add(SBarsItem);
        SBarsItem.addActionListener(action -> myPanel.setMenuTile(SBarTile.class));
        MenuItem WoodBarsItem = new MenuItem("Wood Bars");
        main.add(WoodBarsItem);
        WoodBarsItem.addActionListener(action -> myPanel.setMenuTile(WoodBarsTile.class));
        MenuItem SpawnItem = new MenuItem("Spawner");
        main.add(SpawnItem);
        SpawnItem.addActionListener(action -> myPanel.setMenuTile(SpawnTile.class));
        MenuItem TransItem = new MenuItem("Transition");
        main.add(TransItem);
        TransItem.addActionListener(action -> myPanel.setMenuTile(TransitionTile.class));

        

        myFrame.setMenuBar(bar);

        // Makes continuous game loop that is constantly calling the "run" function on the panel
        Thread myThread = new Thread(myPanel);
        myThread.start();
    }

 
}