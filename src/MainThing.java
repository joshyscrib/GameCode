import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.text.GapContent;

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
    public static JFrame gameFrame;

    public MainThing(String fileName){
        this.filename = filename;
    }


    public static void main(String[] args){
        
        // Creates frame and panel
        JFrame myFrame = new JFrame("game");
        gameFrame = myFrame;
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
       
        // Creates 7 seperate save files for maps and adds to menu
        MenuItem SaveItem1 = new MenuItem("Save 1");
        file.add(SaveItem1);
        MenuItem SaveItem2 = new MenuItem("Save 2");
        file.add(SaveItem2);
        MenuItem SaveItem3 = new MenuItem("Save 3");
        file.add(SaveItem3);
        MenuItem SaveItem4 = new MenuItem("Save 4");
        file.add(SaveItem4);
        MenuItem SaveItem5 = new MenuItem("Save 5");
        file.add(SaveItem5);
        MenuItem SaveItem6 = new MenuItem("Save 6");
        file.add(SaveItem6);
        MenuItem SaveItem7 = new MenuItem("Save G.O.");
        file.add(SaveItem7);
        // Creates 5 ways to load and adds to menu
        MenuItem LoadItem1 = new MenuItem("Load 1");
        file.add(LoadItem1);
        MenuItem LoadItem2 = new MenuItem("Load 2");
        file.add(LoadItem2);
        MenuItem LoadItem3 = new MenuItem("Load 3");
        file.add(LoadItem3);
        MenuItem LoadItem4 = new MenuItem("Load 4");
        file.add(LoadItem4);
        MenuItem LoadItem5 = new MenuItem("Load 5");
        file.add(LoadItem5);
        MenuItem LoadItem6 = new MenuItem("Load 6");
        file.add(LoadItem6);
        MenuItem LoadItem7 = new MenuItem("Load G.O.");
        file.add(LoadItem7);
        // Adds function to the menu items
        SaveItem1.addActionListener(action -> myPanel.save("LevelOne.game"));
        SaveItem2.addActionListener(action -> myPanel.save("LevelTwo.game"));
        SaveItem3.addActionListener(action -> myPanel.save("LevelThree.game"));
        SaveItem4.addActionListener(action -> myPanel.save("LevelFour.game"));
        SaveItem5.addActionListener(action -> myPanel.save("LevelFive.game"));
        SaveItem6.addActionListener(action -> myPanel.save("LevelSix.game"));
        SaveItem7.addActionListener(action -> myPanel.save("LevelSeven.game"));

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
        LoadItem6.addActionListener(action -> myPanel.load("LevelSix.game"));
        LoadItem6.addActionListener(action -> myPanel.curLevel = 6);
        LoadItem7.addActionListener(action -> myPanel.load("LevelSeven.game"));
        LoadItem7.addActionListener(action -> myPanel.curLevel = 7);
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
        MenuItem PSpawnItem = new MenuItem("Princess");
        main.add(PSpawnItem);
        PSpawnItem.addActionListener(action -> myPanel.setMenuTile(PSpawnTile.class));
        MenuItem StartItem = new MenuItem("Start");
        main.add(StartItem);
        StartItem.addActionListener(action -> myPanel.setMenuTile(StartTile.class));
        MenuItem SwordChestItem = new MenuItem("Sword Chest");
        main.add(SwordChestItem);
        SwordChestItem.addActionListener(action -> myPanel.setMenuTile(SwordChestTile.class));
        MenuItem HealthTileItem = new MenuItem("Health Potion");
        main.add(HealthTileItem);
        HealthTileItem.addActionListener(action -> myPanel.setMenuTile(HealthTile.class));
        MenuItem CroissantTileItem = new MenuItem("Croissant");
        main.add(CroissantTileItem);
        CroissantTileItem.addActionListener(action -> myPanel.setMenuTile(CroissantTile.class));
        MenuItem PomegranateTileItem = new MenuItem("Pomegranate");
        main.add(PomegranateTileItem);
        PomegranateTileItem.addActionListener(action -> myPanel.setMenuTile(PomegranateTile.class));
        MenuItem IceeTileItem = new MenuItem("Icee");
        main.add(IceeTileItem);
        IceeTileItem.addActionListener(action -> myPanel.setMenuTile(IceeTile.class));

        // add an about menu
        Menu about = new Menu("About");
        bar.add(about);
        MenuItem aboutGame = new MenuItem("About Game");
        about.add(aboutGame);
        aboutGame.addActionListener(action -> {
            CutSceneModal m = new CutSceneModal();
            m.showModal(gameFrame, "src/images/openingscene.mp4");
        });
        myFrame.setMenuBar(bar);

        // Makes continuous game loop that is constantly calling the "run" function on the panel
        Thread myThread = new Thread(myPanel);
        myThread.start();
    }

 
}