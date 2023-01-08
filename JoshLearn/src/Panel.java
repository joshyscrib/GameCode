import javax.swing.JPanel;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Panel extends JPanel implements Runnable, MouseListener {
    GameKeyListener listener = new GameKeyListener(this);
    public static int xTiles = 22;
    public static int yTiles = 22;
    Tile[][] tiles = new Tile[xTiles][xTiles];
    Player dude = new Player();
    Point location = MouseInfo.getPointerInfo().getLocation();
    double mouseX = location.getX();
    double mouseY = location.getY();
    Class menuTile = GrassTile.class;

    // here is a comment
    public void setMenuTile(Class c) {
        menuTile = c;
    }

    public void init() {

        for (int i = 0; i < xTiles; i++) {
            for (int j = 0; j < yTiles; j++) {
                tiles[i][j] = new GrassTile();
            }
        }

        addMouseListener(this);

    }

    public void save() {
        try {
            FileOutputStream thing = new FileOutputStream(new File("LevelOne.game"));
            ObjectOutputStream stream = new ObjectOutputStream(thing);
            stream.writeObject(tiles);

            stream.close();
            thing.close();
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        System.out.println("We are saving!!!!");
    }

    public void load() {
        try {
            FileInputStream thing = new FileInputStream(new File("LevelOne.game"));
            ObjectInputStream stream = new ObjectInputStream(thing);
            tiles = (Tile[][]) stream.readObject();

            stream.close();
            thing.close();
        } catch (Exception e) {
            System.out.println("EXception " + e);
        }
        System.out.println("We are loading!!!!");
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D context = (Graphics2D) g;
        tiles[0][0].drawTile(context, 0, 0);
    }

    double speed = 5.5;

    public boolean canMovePlayer(int X, int Y) {
        if (doesPointCollide(X, Y) && doesPointCollide(X + 40, Y) && doesPointCollide(X, Y + 63)
                && doesPointCollide(X + 40, Y + 64) && X > 0 && X < 672 && Y > 0 && Y < 672) {
            return true;
        } else {
            return false;
        }
    }

    public boolean doesPointCollide(int X, int Y) {
        if (X < 0 || X > 703 || Y < 0 || Y > 703 || tiles[(int) Math.floor(X / 32)][(int) Math.floor(Y / 32)].solid) {
            return false;
        } else {
            return true;
        }
    }

    public void tick() {
        location = MouseInfo.getPointerInfo().getLocation();
        Point panelLocation = this.getLocation();
        mouseX = location.getX() - panelLocation.getX();
        mouseY = location.getY() - panelLocation.getY();
        dude.tick();
        for (int x = 0; x < xTiles; x++) {
            for (int y = 0; y < yTiles; y++) {
                tiles[x][y].tick();
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
    }

    int placeX = 10;
    int placeY = 10;

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

    public Panel() {
        this.addKeyListener(listener);
        this.setFocusable(true);
        this.init();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Tile curTile = tiles[(int) (x / 32)][(int) (y / 32)];

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

        }
        if (placeTile != null) {
            tiles[(int) (x / 32)][(int) (y / 32)] = placeTile;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
