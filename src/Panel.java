
import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JPanel;
import javax.swing.SpinnerDateModel;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//test old
public class Panel extends JPanel implements Runnable, MouseListener {
    private String filename;
    private Player player;
    private Clip clip;

    private static int PLAYER_HEIGHT = 64;
    private static int PLAYER_WIDTH = 64;
    private static int TILE_WIDTH = 32;
    private static int TILE_HEIGHT = 32;
    static boolean hasPomegranate = false;
    static boolean hasCroissant = false;
    static boolean hasIcee = false;
    GameKeyListener listener = new GameKeyListener(this);
    public static int xTiles = 22;
    public static int yTiles = 22;  
    Tile[][] tiles = new Tile[xTiles][yTiles];
    Item[][] items = new Item[xTiles][yTiles];
    int iceeProtection;
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
    boolean hasPlayedCredits = false;
    Color lowHpColor = new Color(250, 5, 5, 50);
    int princessDeathTick = Integer.MAX_VALUE;
    int pomEndTime;
    int spdEndTime;
    int score = 0;
    // here is a comment
    public void setMenuTile(Class c) {
        menuTile = c;
    }

    public void giveKey() {
        dude.hasKey = true;
    }

    public void init() {
        System.out.println(this);
        for (int i = 0; i < xTiles; i++) {
            for (int j = 0; j < yTiles; j++) {
                tiles[i][j] = new FloorTile();
            }
        }
        addMouseListener(this);
        try {
            TitleModal titleScreen = new TitleModal();
            titleScreen.showModal();
            CutSceneModal m = new CutSceneModal();
        m.showModal(MainThing.gameFrame, "src/images/startingCutscene.mp4");
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        
        load("LevelOne.game");
        play("images/dungeonMusic.wav", true);
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
                if (tiles[i][j].getClass() == StartTile.class) {
                    placeX = i * 32 - 5;
                    placeY = j * 32 - 20;
                }
                items[i][j] = null;
                if (tiles[i][j].getClass() == HealthTile.class) {
                    items[i][j] = new HealthPotion();
                }
                if (tiles[i][j].getClass() == CroissantTile.class) {
                    items[i][j] = new Croissant();
                }
                if (tiles[i][j].getClass() == PomegranateTile.class) {
                    items[i][j] = new Pomegranate();
                }
                if (tiles[i][j].getClass() == IceeTile.class) {
                    items[i][j] = new Icee();
                }

            }
        }
    }

    public void pickUpItem(int X, int Y) {
        boolean collide = false;
        if (isPointInPlayer(X * 32, Y * 32) || isPointInPlayer(X * 32 + 32, Y * 32)
                || isPointInPlayer(X * 32, Y * 32 + 32) || isPointInPlayer(X * 32 + 32, Y * 32 + 32)) {
            collide = true;
        }
        if (collide) {
            if (items[X][Y] != null) {
                if (items[X][Y].getClass() == HealthPotion.class && dude.hp < 100) {
                    dude.hp += 25;
                    items[X][Y] = null;
                }
                if (items[X][Y] != null && items[X][Y].getClass() == Croissant.class) {
                    hasCroissant = true;
                    speed = 6;
                    spdEndTime = tickCount + 400;
                    items[X][Y] = null;
                }
                if (items[X][Y] != null && items[X][Y].getClass() == Pomegranate.class) {
                    hasPomegranate = true;
                    pomEndTime = tickCount + 200;
                    items[X][Y] = null;
                }
                if (items[X][Y] != null && items[X][Y].getClass() == Icee.class  ) {
                    hasIcee = true;
                    items[X][Y] = null;
                    iceeProtection = 75;
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
        if (curLevel == 7 || dude.hp <= 20) {
            context.setColor(Color.RED);
        }
        if (dude.hp >= 0) {
            context.fillRect(715, 215, 37, dude.hp * -2);
        }
        // indicates weapon
        context.setColor(Color.GREEN);
        context.fillRect(715, 250, 70, 70);
        context.setColor(Color.BLACK);
        context.fillRect(717, 252, 66, 66);
        // indicates power ups
        context.setColor(Color.GREEN);
        context.fillRect(715, 350, 70, 70);
        context.setColor(Color.BLACK);
        context.fillRect(717, 352, 66, 66);

        context.setColor(Color.GREEN);
        context.fillRect(715, 450, 70, 70);
        context.setColor(Color.BLACK);
        context.fillRect(717, 452, 66, 66);

        context.setColor(Color.GREEN);
        context.fillRect(715, 550, 70, 70);
        context.setColor(Color.BLACK);
        context.fillRect(717, 552, 66, 66);
        context.setColor(Color.WHITE);
        if(hasIcee){
            if(iceeProtection < 0){
                iceeProtection = 0;
            }
            // indicates shield health
            context.drawString(iceeProtection + " / 75", 735, 640);
        }
        Image image1 = null;
        Image image2 = null;
        Image image3 = null;
        Image image4 = null;
        switch (curWeapon) {
            case 1:
                if (image1 == null) {
                    java.net.URL img = getClass().getClassLoader().getResource("images/dagger.png");
                    try {
                        image1 = ImageIO.read(img);

                    } catch (IOException ex) {
                        System.out.println("EXCEPTION ):  " + ex);
                    }
                }
                context.drawImage(image1, 717, 252, null);
                break;
            case 2:
                if (image2 == null) {
                    java.net.URL img = getClass().getClassLoader().getResource("images/claymore.png");
                    try {
                        image2 = ImageIO.read(img);

                    } catch (IOException ex) {
                        System.out.println("EXCEPTION ):  " + ex);
                    }
                }
                context.drawImage(image2, 717, 252, null);
                break;
            case 3:
                if (image3 == null) {
                    java.net.URL img = getClass().getClassLoader().getResource("images/mace.png");
                    try {
                        image3 = ImageIO.read(img);

                    } catch (IOException ex) {
                        System.out.println("EXCEPTION ):  " + ex);
                    }
                }
                context.drawImage(image3, 717, 252, null);
                break;
            case 4:
                if (image4 == null) {
                    java.net.URL img = getClass().getClassLoader().getResource("images/darkSword.png");
                    try {
                        image4 = ImageIO.read(img);
                    } catch (IOException ex) {
                        System.out.println("EXCEPTION ):  " + ex);
                    }
                }
                context.drawImage(image4, 717, 252, null);
                break;
            default:
                if (image4 == null) {
                    java.net.URL img = getClass().getClassLoader().getResource("images/darkSword.png");
                    try {
                        image4 = ImageIO.read(img);

                    } catch (IOException ex) {
                        System.out.println("EXCEPTION ):  " + ex);
                    }
                }
                context.drawImage(image4, 717, 252, null);
                break;
        }

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
                load("LevelSix.game");
                break;
            case 6:
                load("LevelOne.game");
                break;
            case 7:
                if (curWeapon >= 4) {
                    load("LevelSix.game");
                } else {
                    load("LevelOne.game");
                }
                break;
        }
        curLevel++;
        if (curLevel >= 8) {
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

    public boolean isPointInSprite(int x, int y, int sX, int sY) {
        if (x >= sX - 2 && x <= sX + 32 + 2 && y >= sX - 3 && y <= sX + 64) {
            return true;
        }

        return false;
    }

    int SPRITE_WIDTH = 32;
    int SPRITE_HEIGHT = 64;
    public void revertSpeed(){
        speed = 4;
        hasCroissant = false;
    }
    public void revertStrength(){
        dude.atk = 8;
        hasPomegranate = false;
    }
    public void revertShield(){
        hasIcee = false;
    }
    public boolean isPointInSprite2(int x, int y, int spriteX, int spriteY){

        if(x >= spriteX && x < spriteX + SPRITE_WIDTH && y >= spriteY && y < spriteY + SPRITE_HEIGHT){
            return true;
        }

        return false;
    }

    public boolean doSpritesCollide(int cX, int cY, int sCx, int sCy) {
        // see if each corner of sprite 1 is within the rectangle of sprite2, then check the opposite
        if(isPointInSprite2(cX, cY, sCx, sCy) || // top left
        isPointInSprite2(cX + SPRITE_WIDTH, cY, sCx, sCy) || // top right
        isPointInSprite2(cX, cY + SPRITE_HEIGHT, sCx, sCy) || // bottom left
        isPointInSprite2(cX + SPRITE_WIDTH, cY + SPRITE_HEIGHT, sCx, sCy)){ // bottom right
            return true;
        }

        // check the other sprite
        if(isPointInSprite2(sCx, sCy, cX, cY) || // top left
        isPointInSprite2(sCx + SPRITE_WIDTH, sCy, cX, cY) || // top right
        isPointInSprite2(sCx, sCy + SPRITE_HEIGHT, cX, cY) || // bottom left
        isPointInSprite2(sCx + SPRITE_WIDTH, sCy + SPRITE_HEIGHT, cX, cY)){ // bottom right
            return true;
        }



        /* 

        if (isPointInSprite(cX, cY, sCx, sCy)
                || isPointInSprite(cX + 32, cY, sCx, sCy)
                || isPointInSprite(cX, cY + 64, sCx, sCy)
                || isPointInSprite(cX + 32, cY + 34, sCx, sCy)
                || isPointInSprite(cX, cY, sCx + 32, sCy)
                || isPointInSprite(cX + 32, cY, sCx + 32, sCy)
                || isPointInSprite(cX, cY + 64, sCx + 32, sCy)
                || isPointInSprite(cX + 32, cY + 64, sCx + 32, sCy)
                || isPointInSprite(cX, cY, sCx, sCy + 64)
                || isPointInSprite(cX + 32, cY, sCx, sCy + 64)
                || isPointInSprite(cX, cY + 64, sCx, sCy + 64)
                || isPointInSprite(cX + 32, cY + 64, sCx, sCy + 64)
                || isPointInSprite(cX, cY, sCx + 32, sCy + 64)
                || isPointInSprite(cX + 32, cY, sCx + 32, sCy + 64)
                || isPointInSprite(cX, cY + 64, sCx + 32, sCy + 64)
                || isPointInSprite(cX + 32, cY + 64, sCx + 32, sCy + 64)) {
            return true;
        }
        */
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

    private boolean isPointInTile(int x, int y, int tileLeft, int tileTop) {

        return false;
    }

    public void tick() {
        // checks to remove power-ups
        if(hasIcee){
            if(iceeProtection <= 0){
                revertShield();
            }
        }
        if(tickCount >= pomEndTime){
            revertStrength();
        }
        if(tickCount >= spdEndTime){
            revertSpeed();
        }
        for (int i = 0; i < tiles.length; i++) {
            int tileLeft = i * 32;
            for (int j = 0; j < tiles[i].length; j++) {
                Tile currentTile = tiles[i][j];
                int tileTop = j * 32;
                if (isPointInPlayer(i * 32, j * 32) || isPointInPlayer(i * 32 + 32, j * 32)
                        || isPointInPlayer(i * 32, j * 32 + 32)
                        || isPointInPlayer(i * 32 + 32, j * 32 + 32)) {

                    if (tiles[i][j].getClass() == SwordChestTile.class) {
                        curWeapon++;
                        tiles[i][j] = new FloorTile();

                    }
                    if (tiles[i][j].getClass() == TransitionTile.class && dude.hasKey) {
                        loadNext(curLevel);
                    }


                }
            }
        }

        int weaponTipX = 0;
        int weaponTipY = 0;
        int swordLen = 15;
        switch (dude.playerDirection) {
            case Left:
                // left tip should be player x - swordLen
                // player y = should be halfway between top and bottom of player
                weaponTipX = placeX - swordLen;
                weaponTipY = placeY + (PLAYER_HEIGHT / 2);
                break;
            case Right:
                weaponTipX = placeX + PLAYER_WIDTH + swordLen;
                weaponTipY = placeY + (PLAYER_HEIGHT / 2);
                break;
            case Up:
                weaponTipX = placeX + (PLAYER_WIDTH / 2);
                weaponTipY = placeY - swordLen;
                break;
            case Down:
                weaponTipX = placeX + (PLAYER_WIDTH / 2);
                weaponTipY = placeY + PLAYER_HEIGHT + swordLen;
                break;
        }

        // what tile is the sword in?
        int tileX = weaponTipX / TILE_WIDTH;
        int tileY = weaponTipY / TILE_HEIGHT;
        if (dude.isAttacking) {
            if (tileX - 1 >= 0 && tileX + 1 < tiles.length && tileY - 1 >= 0 && tileY + 1 < tiles.length) {
                Tile currentTile = tiles[tileX][tileY];
                // See If the player is attacking, and the tile we're checking is wood
                if (currentTile != null && currentTile.getClass() == WoodTile.class) {
                    tiles[tileX][tileY] = new FloorTile();
                }
                if (tiles[tileX-1][tileY-1] != null && tiles[tileX-1][tileY-1].getClass() == WoodTile.class) {
                    tiles[tileX-1][tileY-1] = new FloorTile();
                }
                if (tiles[tileX][tileY-1] != null && tiles[tileX][tileY-1].getClass() == WoodTile.class) {
                    tiles[tileX][tileY-1] = new FloorTile();
                }
                if (tiles[tileX+1][tileY-1] != null && tiles[tileX+1][tileY-1].getClass() == WoodTile.class) {
                    tiles[tileX+1][tileY-1] = new FloorTile();
                }
                if (tiles[tileX-1][tileY] != null && tiles[tileX-1][tileY].getClass() == WoodTile.class) {
                    tiles[tileX-1][tileY] = new FloorTile();
                }
                if (tiles[tileX+1][tileY] != null && tiles[tileX+1][tileY].getClass() == WoodTile.class) {
                    tiles[tileX+1][tileY] = new FloorTile();
                }
                if (tiles[tileX-1][tileY+1]!= null && tiles[tileX-1][tileY+1].getClass() == WoodTile.class) {
                    tiles[tileX-1][tileY+1] = new FloorTile();
                }
                if (tiles[tileX][tileY+1] != null && tiles[tileX][tileY+1].getClass() == WoodTile.class) {
                    tiles[tileX][tileY+1] = new FloorTile();
                }
                if (tiles[tileX+1][tileY+1] != null && tiles[tileX+1][tileY+1].getClass() == WoodTile.class) {
                    tiles[tileX+1][tileY+1] = new FloorTile();
                }

            }
        }

        if (dude.hp <= 0) {
            load("LevelSeven.game ");
            dude.hasKey = true;
            mobs.clear();
            dude.hp = 100;
            curLevel = 7;
            revertShield();
            revertSpeed();
            revertStrength();
            curWeapon = 1;
        }
        tickCount++;
        if (listener.attacking) {
            if (Math.abs(dude.tickCount) - dude.lastAttackTick > 10) {
                play("images/swordSwoosh.wav", false);
            }
            dude.attack(mobs, placeX, placeY, curWeapon);
        }

        for (int i = mobs.size() - 1; i >= 0; i--) {
            Mob curMob = mobs.get(i);
            if (curMob.getClass() == Fireball.class) {
            }
            if (curMob.getClass() == Fireball.class && playerTakeDamage(curMob.x, curMob.y)) {

                mobs.remove(i);
                if (curMob.isFast) {
                    if(hasIcee){
                        iceeProtection -= 60;
                    }
                    else{
                        dude.hp -= 60;
                    }
                } else {
                    if(hasIcee){
                        iceeProtection -= 20;
                    }
                    else{
                        dude.hp -= 20;
                    }
                }

            }
            if (playerTakeDamage(curMob.x, curMob.y)
                    && (curMob.getClass() == Guard.class || curMob.getClass() == Princess.class || curMob.getClass() == Guard2.class || curMob.getClass() == Guard3.class)
                    && tickCount % 12 == 0) {
                        if(hasIcee){
                            iceeProtection -= 20;
                        }
                        else{
                            dude.hp -= 20;
                        }
                
                if (curMob.getClass() == Princess.class) {
                    if(hasIcee){
                        iceeProtection -= 10;
                    }
                    else{
                        dude.hp -= 10;
                    }
                }
                if(curMob.getClass() == Guard2.class){
                    if(hasIcee){
                        iceeProtection -= 5;
                    }
                    else{
                        dude.hp -= 5;
                    }
                }
                if(curMob.getClass() == Guard3.class){
                    if(hasIcee){
                        iceeProtection -= 15;
                    }
                    else{
                        dude.hp -= 15;
                    }
                }
                switch(curWeapon){
                    case 2:
                    dude.hp += 5;
                        break;
                    case 3:
                    dude.hp += 12;
                        break;
                    case 4:
                    dude.hp += 17;
                        break;
                }
            }
            curMob.tick(tiles, dude, mobs);
            if (curMob.isDead()) {
                mobs.remove(i);
                score++;
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
        boolean princessFound = false;
        for (int i = 0; i < mobs.size(); i++) {
            Mob curMob = mobs.get(i);
            if (curMob.getClass() == Princess.class) {
                princessFound = true;
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
        if (!princessFound && curLevel == 6) {
            // princess is dead
            if (!hasPlayedCredits) {

                if(princessDeathTick == Integer.MAX_VALUE){
                     princessDeathTick = tickCount;
                }
    
            }
        }
        if (dude.hp > 100) {
            dude.hp = 100;
        }
        if (tickCount - 20 >= princessDeathTick && !hasPlayedCredits) {
            clip = null;
            try {
                CutSceneModal m = new CutSceneModal();
            m.showModal(MainThing.gameFrame, "src/images/creditCutscene.mp4");
            } catch (Exception e) {
                // TODO: handle exception
            }
            hasPlayedCredits = true;
        }
        for (int i = 0; i < mobs.size(); i++) {
            for (int j = 0; j < mobs.size(); j++) {
                if (i == j) {
                    continue;
                }

                Mob mob1 = mobs.get(i);
                Mob mob2 = mobs.get(j);
                boolean removeMobs = false;

                if(mob1 instanceof Guard && mob2 instanceof Guard){
                    if(doSpritesCollide(mob1.x, mob1.y, mob2.x, mob2.y)){
                        Guard2 guard2 = new Guard2(mob1.x, mob1.y);
                        removeMobs = true;
                        mobs.add(guard2);
                    }
                }               

        
                if (mobs.get(i).getClass() == Guard2.class && mobs.get(j).getClass() == Guard2.class
                        && doSpritesCollide(mobs.get(i).x, mobs.get(i).y, mobs.get(j).x, mobs.get(j).y)) {
                    Guard3 guard3 = new Guard3(mobs.get(i).x, mobs.get(i).y);
                    removeMobs = true;
                    mobs.add(guard3);
                }

                if (removeMobs) {
                    if (j > i) {
                        mobs.remove(j);
                        mobs.remove(i);
                        break;
                    } else {
                        mobs.remove(i);
                        mobs.remove(j);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            tick();
            repaint();
            try {
                Thread.sleep(30);
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
            
            if (loop) {
     //           clip.loop(Clip.LOOP_CONTINUOUSLY);
     clip.start();
            } else {
                
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
            case "CroissantTile":
                placeTile = new CroissantTile();
                break;
            case "PomegranateTile":
                placeTile = new PomegranateTile();
                break;
            case "IceeTile":
                placeTile = new IceeTile();
                break;

        }
        if (placeTile != null && listener.devMode) {
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
            case "CroissantTile":
                placeTile = new CroissantTile();
                break;
            case "PomegranateTile":
                placeTile = new PomegranateTile();
                break;
            case "IceeTile":
                placeTile = new IceeTile();
                break;
        }
        if (placeTile != null  && listener.devMode) {
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
            case "CroissantTile":
                placeTile = new CroissantTile();
                break;
            case "PomegranateTile":
                placeTile = new PomegranateTile();
                break;
            case "IceeTile":
                placeTile = new IceeTile();
                break;
        }
        if (placeTile != null && listener.devMode) {
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
            case "CroissantTile":
                placeTile = new CroissantTile();
                break;
            case "PomegranateTile":
                placeTile = new PomegranateTile();
                break;
            case "IceeTile":
                placeTile = new IceeTile();
                break;
        }
        if (placeTile != null && listener.devMode) {
            tiles[(int) (x / 32)][(int) (y / 32)] = placeTile;
        }
    }
}
