import javax.naming.Context;
import javax.print.DocFlavor.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class Panel extends JPanel implements Runnable, MouseListener {
    private String filename;
    private Player player;
    private Clip clip;

    GameKeyListener listener = new GameKeyListener(this);
    public static int xTiles = 22;
    public static int yTiles = 22;
    Tile[][] tiles = new Tile[xTiles][xTiles];
    Player dude = new Player();
    Point location = MouseInfo.getPointerInfo().getLocation();
    double mouseX = location.getX();
    double mouseY = location.getY();
    Class menuTile = FloorTile.class;
    ArrayList<Mob> mobs = new ArrayList<Mob>();
    int curLevel = 1;

    // here is a comment
    public void setMenuTile(Class c) {
        menuTile = c;
    }

    public void init() {
        play();
        for (int i = 0; i < xTiles; i++) {
            for (int j = 0; j < yTiles; j++) {
                tiles[i][j] = new FloorTile();
            }
        }
        addMouseListener(this);
        load("LevelOne.game");

    }

    public void save(String place) {
        try {
            FileOutputStream thing = new FileOutputStream(new File(place));
            ObjectOutputStream stream = new ObjectOutputStream(thing);
            stream.writeObject(tiles);

            stream.close();
            thing.close();
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        System.out.println("We are saving!!!!");
    }

    public void load(String place) {
        try {
            FileInputStream thing = new FileInputStream(new File(place));
            ObjectInputStream stream = new ObjectInputStream(thing);
            tiles = (Tile[][]) stream.readObject();

            stream.close();
            thing.close();
        } catch (Exception e) {
            System.out.println("EXception " + e);
        }
        System.out.println("We are loading!!!!");
        for (int i = 0; i < xTiles; i++) {
            for (int j = 0; j < yTiles; j++) {
                if (tiles[i][j].getClass() == SpawnTile.class) {
                    Guard guard = new Guard(i * 32, j * 32);
                    mobs.add(guard);
                }
                if (tiles[i][j].getClass() == PSpawnTile.class) {
                    Princess princess = new Princess(i * 32, j * 32);
                    mobs.add(princess);
                }

            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D context = (Graphics2D) g;

        tiles[0][0].drawTile(context, 0, 0);
        context.setColor(Color.BLACK);
        context.fillRect(5, 715, 215, 51);
        context.setColor(Color.GREEN);
        if (dude.hp >= 0) {
            context.fillRect(13, 723, dude.hp * 2, 37);
        }
    }

    double speed = 4;

    public void clearAll() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = null;
                tiles[i][j] = new FloorTile();
            }
        }
    }

    public boolean canMovePlayer(int X, int Y) {
        if (doesPointCollide(X, Y) && doesPointCollide(X + 40, Y) && doesPointCollide(X, Y + 63)
                && doesPointCollide(X + 40, Y + 64) && X > 0 && X < 672 && Y > 0 && Y < 672
                && doesPointCollide(X + 20, Y) && doesPointCollide(X, Y + 30) && doesPointCollide(X + 45, Y + 30)
                && doesPointCollide(X + 20, Y + 60)) {
            return true;
        } else {
            return false;
        }
    }

    public void loadNext(int level) {
        mobs.clear();
        switch (curLevel) {
            case 1:
                load("LevelTwo.game");
                break;
            case 2:
                load("LevelThree.game");
                break;
            case 3:
                load("LevelFour.game");
                break;
            case 4:
                load("LevelFive.game");
                break;
            case 5:
                load("LevelOne.game");
                break;
        }
        curLevel++;
        if (curLevel >= 6) {
            curLevel = 1;
        }

    }

    public boolean doesPointCollide(int X, int Y) {
        if (X < 0 || X > 703 || Y < 0 || Y > 703 || tiles[(int) Math.floor(X / 32)][(int) Math.floor(Y / 32)].solid) {
            return false;
        }
        return true;
    }

    public boolean isPointInPlayer(int x, int y) {
        if (x >= placeX - 2 && x <= placeX + 32 + 2 && y >= placeY - 3 && y <= placeY + 64) {
            return true;
        }

        return false;
    }

    public boolean playerTakeDamage(int x, int y) {
        if (isPointInPlayer(x, y) || isPointInPlayer(x + 32, y) || isPointInPlayer(x, y + 64)
                || isPointInPlayer(x + 32, y + 64)) {
            System.out.println("Player Hit!!!");
            return true;
        }
        return false;

    }

    int tickCount = 0;

    public void tick() {
        tickCount++;
        if (listener.attacking) {
            dude.attack(mobs, placeX, placeY);
        }

        for (int i = mobs.size() - 1; i >= 0; i--) {
            Mob curMob = mobs.get(i);
            if (playerTakeDamage(curMob.x, curMob.y)) {
                dude.hp -= 20;
            }
            if (playerTakeDamage(curMob.x, curMob.y)) {
                /*
                 * switch(curMob.monsterDirection){
                 * 
                 * case Up:
                 * placeY -= 8;
                 * curMob.y += 8;
                 * break;
                 * case Left:
                 * placeX -= 8;
                 * curMob.x += 8;
                 * break;
                 * case Down:
                 * placeY += 8;
                 * curMob.y -= 8;
                 * break;
                 * case Right:
                 * placeX += 8;
                 * curMob.x -= 8;
                 * break;
                 * }
                 */
            }
            curMob.tick(tiles, dude, mobs);
            if (curMob.isDead()) {
                mobs.remove(i);
            }
        }

        location = MouseInfo.getPointerInfo().getLocation();
        Point panelLocation = this.getLocation();
        mouseX = location.getX() - panelLocation.getX();
        mouseY = location.getY() - panelLocation.getY();
        dude.tick();
        for (int x = 0; x < xTiles; x++) {
            for (int y = 0; y < yTiles; y++) {
                tiles[x][y].tick();

                if ((isPointInPlayer(x * 32, y * 32) || isPointInPlayer(x * 32 + 32, y * 32)
                        || isPointInPlayer(x * 32, y * 32 + 32) || isPointInPlayer(x * 32 + 32, y * 32 + 32))
                        && tiles[x][y].getClass() == TransitionTile.class) {
                    loadNext(curLevel);
                }

            }
        }
        int targetX = placeX;
        int targetY = placeY;
        if (listener.uping) {
            targetY -= speed;
        }
        if (listener.lefting) {
            targetX -= speed;
        }
        if (listener.downing) {
            targetY += speed;
        }
        if (listener.righting) {
            targetX += speed;
        }
        if (canMovePlayer(targetX, targetY)) {
            placeX = targetX;
            placeY = targetY;
        }
        // System.out.println(placeX + ":" + placeY);
        if (listener.uping || listener.lefting || listener.downing || listener.righting) {
            dude.isMoving = true;
        } else {
            dude.isMoving = false;
        }
        if (listener.lefting) {
            dude.playerDirection = Direction.Left;
        }
        if (listener.righting) {
            dude.playerDirection = Direction.Right;
        }
        if (listener.uping) {
            dude.playerDirection = Direction.Up;
        }
        if (listener.downing) {
            dude.playerDirection = Direction.Down;
        }
        for(int i = 0; i < mobs.size(); i++){
            if(mobs.get(i).getClass() == Princess.class){
            if(tickCount % 100 == 0){
                  // finds distance between princess and player(to shoot a fireball)
                  double dx = placeX - mobs.get(i).x;
                  double dy = placeY-  mobs.get(i).y;
                  double hyp = Math.sqrt(dx*dx + dy*dy);
                  double angrad = Math.atan2(dy, dx);
               //   angrad += Math.PI/2.0;
                  double ang = Math.toDegrees(angrad);
                  System.out.println(ang);
                  if(ang < 0)
                  {   
                    ang += 360;
                  }
            Fireball fireball = new Fireball(1, Direction.Left, mobs, ang);
            fireball.x = mobs.get(i).x;
            fireball.y = mobs.get(i).y;

            mobs.add(fireball);
        } 
            }
        }
        
    }

    int placeX = 200;
    int placeY = 200;

    @Override
    public void run() {
        while (true) {
            tick();
            repaint();
            try {
                Thread.sleep(31);
            } catch (Exception e) {

            }
        }
    }

    public void keyDown(int code) {
        System.out.println(code);
    }

    public void keyUp(int code) {
        System.out.println(code);
    }

    public Panel(String filename) {
        this.addKeyListener(listener);
        this.setFocusable(true);
        this.init();
        this.filename = filename;
    }

    public void play() {
        try {
            if (clip == null) {
                java.net.URL url = this.getClass().getResource("images/dungeonMusic.wav");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                // Get a clip resource.
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            }

            clip.start();
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // Tile curTile = tiles[(int) (x / 32)][(int) (y / 32)];

        System.out.println("Placing " + menuTile.getName());

        Tile placeTile = null;
        switch (menuTile.getName()) {
            case "GrassTile":
                placeTile = new GrassTile();
                break;
            case "WallTile":
                placeTile = new WallTile();
                break;
            case "WoodTile":
                placeTile = new WoodTile();
                break;
            case "FloorTile":
                placeTile = new FloorTile();
                break;
            case "BarrelTile":
                placeTile = new BarrelTile();
                break;
            case "BarsTile":
                placeTile = new BarsTile();
                break;
            case "SBarTile":
                placeTile = new SBarTile();
                break;
            case "WoodBarsTile":
                placeTile = new WoodBarsTile();
                break;
            case "SpawnTile":
                placeTile = new SpawnTile();
                break;
            case "PSpawnTile":
                placeTile = new PSpawnTile();
                break;
            case "TransitionTile":
                placeTile = new TransitionTile();
                break;

        }
        if (placeTile != null) {
            tiles[(int) (x / 32)][(int) (y / 32)] = placeTile;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        int x = e.getX();
        int y = e.getY();
        // Tile curTile = tiles[(int) (x / 32)][(int) (y / 32)];

        System.out.println("Placing " + menuTile.getName());

        Tile placeTile = null;
        switch (menuTile.getName()) {
            case "GrassTile":
                placeTile = new GrassTile();
                break;
            case "WallTile":
                placeTile = new WallTile();
                break;
            case "WoodTile":
                placeTile = new WoodTile();
                break;
            case "FloorTile":
                placeTile = new FloorTile();
                break;
            case "BarrelTile":
                placeTile = new BarrelTile();
                break;
            case "BarsTile":
                placeTile = new BarsTile();
                break;
            case "SBarTile":
                placeTile = new SBarTile();
                break;
            case "WoodBarsTile":
                placeTile = new WoodBarsTile();
                break;
            case "SpawnTile":
                placeTile = new SpawnTile();
                break;
            case "PSpawnTile":
                placeTile = new PSpawnTile();
                break;
            case "TransitionTile":
                placeTile = new TransitionTile();
                break;

        }
        if (placeTile != null) {
            tiles[(int) (x / 32)][(int) (y / 32)] = placeTile;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        int x = e.getX();
        int y = e.getY();
        // ile curTile = tiles[(int) (x / 32)][(int) (y / 32)];

        System.out.println("Placing " + menuTile.getName());

        Tile placeTile = null;
        switch (menuTile.getName()) {
            case "GrassTile":
                placeTile = new GrassTile();
                break;
            case "WallTile":
                placeTile = new WallTile();
                break;
            case "WoodTile":
                placeTile = new WoodTile();
                break;
            case "FloorTile":
                placeTile = new FloorTile();
                break;
            case "BarrelTile":
                placeTile = new BarrelTile();
                break;
            case "BarsTile":
                placeTile = new BarsTile();
                break;
            case "SBarTile":
                placeTile = new SBarTile();
                break;
            case "WoodBarsTile":
                placeTile = new WoodBarsTile();
                break;
            case "SpawnTile":
                placeTile = new SpawnTile();
                break;
            case "PSpawnTile":
                placeTile = new PSpawnTile();
                break;
            case "TransitionTile":
                placeTile = new TransitionTile();
                break;

        }
        if (placeTile != null) {
            tiles[(int) (x / 32)][(int) (y / 32)] = placeTile;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // Tile curTile = tiles[(int) (x / 32)][(int) (y / 32)];

        System.out.println("Placing " + menuTile.getName());

        Tile placeTile = null;
        switch (menuTile.getName()) {
            case "GrassTile":
                placeTile = new GrassTile();
                break;
            case "WallTile":
                placeTile = new WallTile();
                break;
            case "WoodTile":
                placeTile = new WoodTile();
                break;
            case "FloorTile":
                placeTile = new FloorTile();
                break;
            case "BarrelTile":
                placeTile = new BarrelTile();
                break;
            case "BarsTile":
                placeTile = new BarsTile();
                break;
            case "SBarTile":
                placeTile = new SBarTile();
                break;
            case "WoodBarsTile":
                placeTile = new WoodBarsTile();
                break;
            case "SpawnTile":
                placeTile = new SpawnTile();
                break;
            case "PSpawnTile":
                placeTile = new PSpawnTile();
                break;
            case "TransitionTile":
                placeTile = new TransitionTile();
                break;

        }
        if (placeTile != null) {
            tiles[(int) (x / 32)][(int) (y / 32)] = placeTile;
        }
    }

}
