
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Panel extends JPanel implements Runnable, MouseListener {
    private String filename;
    private Player player;
    private Clip clip;

    //CutSceneModal modal = new CutSceneModal();

    GameKeyListener listener = new GameKeyListener(this);
    public static int xTiles = 22;
    public static int yTiles = 22;
    Tile[][] tiles = new Tile[xTiles][yTiles];
    Item[][] items = new Item[xTiles][yTiles];
    
    Player dude = new Player();
    Point location = MouseInfo.getPointerInfo().getLocation();
    double mouseX = location.getX();
    double mouseY = location.getY();
    Class menuTile = FloorTile.class;
    ArrayList<Mob> mobs = new ArrayList<Mob>();
    int curLevel = 1;
    int placeX = 200;
    int placeY = 200;
    int curWeapon = 1;
    Color lowHpColor = new Color(250,5,5,50);
    // here is a comment
    public void setMenuTile(Class c) {
        menuTile = c;
    }

    public void giveKey() {
        dude.hasKey = true;
    }

    public void init() {
        play("images/dungeonMusic.wav", true);
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
                if(tiles[i][j].getClass() == StartTile.class){
                    placeX= i*32 - 5;
                    placeY = j*32 - 20;
                }
                items[i][j] = null;
                if(tiles[i][j].getClass() == HealthTile.class){
                    items[i][j] = new HealthPotion();
                }

            }
        }
    }

    public void pickUpItem(int X, int Y){
        boolean collide = false;
        if(isPointInPlayer(X * 32, Y * 32) || isPointInPlayer(X * 32 + 32, Y * 32) || isPointInPlayer(X * 32, Y * 32 + 32) || isPointInPlayer(X * 32 + 32, Y * 32 + 32)){
            collide = true;
        }
        if(collide){
            if(items[X][Y] != null){
                if(items[X][Y].getClass() == HealthPotion.class && dude.hp < 100){
                    dude.hp += 25;
                    items[X][Y] = null;
                }
                
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D context = (Graphics2D) g;

        if (dude.hp > 0) {
            tiles[0][0].drawTile(context, 0, 0);
        }
        context.setColor(Color.BLACK);
        context.fillRect(708, 8, 51, 215);
        context.setColor(Color.GREEN);
        if (dude.hp <= 45) {
            context.setColor(Color.YELLOW);
        }
        if (curLevel == 6 || dude.hp <= 20) {
            context.setColor(Color.RED);
        }
        if (dude.hp >= 0) {
            context.fillRect(715, 215, 37, dude.hp * -2);
        }
       context.setColor(Color.GREEN);
       context.fillRoundRect(715, 250, 70, 70, 25, 25);
       context.setColor(Color.BLACK);
       context.fillRoundRect(717, 252, 66, 66, 25, 25);
    }

    double speed = 4;

    public void clearAll() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                
                    tiles[i][j] = new FloorTile();
                
            }
            
        }
        mobs.clear();
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
        //modal.showModal(null);
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
            case 6:
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

    private boolean isPointInTile(int x, int y, int tileLeft, int tileTop){

        return false;
    }

    public void tick() {
        for(int i = 0; i < tiles.length; i++){
            int tileLeft = i *32;
            for(int j = 0; j < tiles[i].length; j++){
                Tile currentTile= tiles[i][j];
                int tileTop = j * 32;
                if (isPointInPlayer(i * 32, j * 32) || isPointInPlayer(i * 32 + 32, j * 32) || isPointInPlayer(i * 32, j * 32 + 32)
                || isPointInPlayer(i * 32 + 32, j * 32 + 32)){


                    if(tiles[i][j].getClass() == SwordChestTile.class){
                        curWeapon++;
                        tiles[i][j] = new FloorTile();

                    }
                    if(tiles[i][j].getClass() == TransitionTile.class && dude.hasKey){
                        loadNext(curLevel);
                    }

                    int weaponTipX = 0;
                    int weaponTipY = 0;

                    //See If the player is attacking, and the tile we're checking is wood
                    if(dude.isAttacking && currentTile.getClass() == WoodTile.class){
                        //See if the sword of the player is inside the wood tile
                       if(isPointInTile(weaponTipX, weaponTipY, tileLeft, tileTop))   {
                            // the player has stabbed a wood tile, let's turn it into a floor tile
                            tiles[i][j] = new FloorTile();
                      }
                    }

                    if((tileLeft >= placeX - 36 && tileLeft <= placeX + 68 && tileTop >= placeY - 35 && tileTop <= placeY + 99) 
                    || (tileLeft + 35 >= placeX - 37 && tileLeft + 37 <= placeX + 67 && tileTop >= placeY - 37 && tileTop <= placeY + 98) 
                    || (tileLeft >= placeX - 35 && tileLeft <= placeX + 67 && tileTop + 67 >= placeY - 36 && tileTop + 64 <= placeY + 96) 
                    || (tileLeft + 32 >= placeX - 32 && tileLeft + 32<= placeX + 64 && tileTop + 67 >= placeY - 38 && tileTop + 7 <= placeY + 99)){
                        if(tiles[i][j].getClass() == WoodTile.class && dude.isAttacking)
                        tiles[i][j] = new FloorTile();
                    }

                }
            }
        }
        
        if(listener.healing && dude.hp < 100){
            dude.hp += 2;
        }
        if (dude.hp <= 0) {
            load("LevelSix.game ");
            dude.hasKey = true;
            mobs.clear();
            dude.hp = 100;
            curLevel = 6;
        }
        tickCount++;
        if (listener.attacking) {
            if (Math.abs(dude.tickCount) - dude.lastAttackTick > 10) {
                play("images/swordSwoosh.wav", false);
            }
            dude.attack(mobs, placeX, placeY);
        }

        for (int i = mobs.size() - 1; i >= 0; i--) {
            Mob curMob = mobs.get(i);
            if(curMob.getClass() == Fireball.class){
            }
            if (curMob.getClass() == Fireball.class && playerTakeDamage(curMob.x, curMob.y)) {

                mobs.remove(i);
                if(curMob.isFast){
                    dude.hp -= 60;
                }
                else{
                    dude.hp /=2;
                }

            }
            if (playerTakeDamage(curMob.x, curMob.y)
                    && (curMob.getClass() == Guard.class || curMob.getClass() == Princess.class)
                    && tickCount % 12 == 0) {
                dude.hp -= 20;
                if(curMob.getClass() == Princess.class){
                    dude.hp -= 10;
                }

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
                pickUpItem(x, y);

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
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).getClass() == Princess.class) {
                if (tickCount % 100 == 0) {
                    // finds distance between princess and player(to shoot a fireball)
                    double dx = placeX + 15 - mobs.get(i).x;
                    double dy = placeY + 30 - mobs.get(i).y;
                    double hyp = Math.sqrt(dx * dx + dy * dy);
                    double angrad = Math.atan2(dy, dx);
                    // angrad += Math.PI/2.0;
                    double ang = Math.toDegrees(angrad);
                    System.out.println(ang);
                    if (ang < 0) {
                        ang += 360;
                    }
                    Fireball fireball = new Fireball(1, Direction.Left, mobs, ang);
                    fireball.x = mobs.get(i).x;
                    fireball.y = mobs.get(i).y;

                    if (hyp > 150) {
                        mobs.add(fireball);
                    }
                }
            }
        }
        if(dude.hp > 100){
            dude.hp = 100;
        }
    }



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

    public void play(String file, boolean loop) {
        try {
            java.net.URL url = this.getClass().getResource(file);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if(loop){
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{
                clip.start();
            }
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
            case "StartTile":
                placeTile = new StartTile();
                break;
            case "SwordChestTile":
                placeTile = new SwordChestTile();
                break;
            case "HealthTile":
                placeTile = new HealthTile();
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
            case "StartTile":
                placeTile = new StartTile();
                break;
            case "SwordChestTile":
                placeTile = new SwordChestTile();
                break;
                case "HealthTile":
                    placeTile = new HealthTile();
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
            case "StartTile":
                placeTile = new StartTile();
                break;
                case "SwordChestTile":
                    placeTile = new SwordChestTile();
                    break;
                    case "HealthTile":
                        placeTile = new HealthTile();
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
            case "StartTile":
                placeTile = new StartTile();
                break;
                case "SwordChestTile":
                    placeTile = new SwordChestTile();
                    break;
                    case "HealthTile":
                        placeTile = new HealthTile();
                        break;

        }
        if (placeTile != null) {
            tiles[(int) (x / 32)][(int) (y / 32)] = placeTile;
        }
    }
}
