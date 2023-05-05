import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

// Player 
public class Player {
    Direction playerDirection = Direction.Right;
    int tickCount = 0;
    private int animationIndex = 0;
    private boolean UDindex = false;
    int hp = 100;
    int atk = 8;
    int def = 0;
    boolean isMoving = false;
    Item[] inventory;
    boolean isAttacking = false;
    boolean facingLeft = false;
    int attackTick = 0;
    private Image[] playerImages = new Image[4];
    private Image[] uPlayerImages = new Image[2];
    private Image[] dPlayerImages = new Image[2];
    boolean hasKey = true;
    int lastAttackTick = 0;
    int udindex = 0;

    public Player() {
        URL img1 = getClass().getClassLoader().getResource("images/wolf-2.png");
        URL img2 = getClass().getClassLoader().getResource("images/wolf-1.png");
        URL img3 = getClass().getClassLoader().getResource("images/wolf-3.png");
        URL img2_1 = getClass().getClassLoader().getResource("images/wolfFront-1.png");
        URL img2_2 = getClass().getClassLoader().getResource("images/wolfFront-2.png");
        URL img3_1 = getClass().getClassLoader().getResource("images/wolfBack-1.png");
        URL img3_2 = getClass().getClassLoader().getResource("images/wolfBack-2.png");
        try {
            playerImages[0] = ImageIO.read(img1);
            playerImages[1] = ImageIO.read(img2);
            playerImages[2] = ImageIO.read(img3);
            playerImages[3] = playerImages[1];
            uPlayerImages[0] = ImageIO.read(img2_1);
            uPlayerImages[1] = ImageIO.read(img2_2);
            dPlayerImages[0] = ImageIO.read(img3_1);
            dPlayerImages[1] = ImageIO.read(img3_2);
        } catch (IOException ex) {
        }
    }

    public void tick() {
        UDindex = !UDindex;
        if(UDindex){
            udindex = 1;
        }
        else{
            udindex = 0;
        }
        tickCount++;
        if (tickCount % 9 == 0) {
            animationIndex++;
            if (animationIndex >= playerImages.length) {
                animationIndex = 0;
            }
        }
        if (isAttacking) {
            attackTick++;
            if (attackTick > 5) {
                isAttacking = false;
                attackTick = 0;
            }
        }
    }

    public void attack(ArrayList<Mob> mobs, int x, int y, int weapon) {
        switch(weapon){
            case 1:
            atk = 8;
            break;
            case 2:
            atk = 12;
            break;
            case 3:
            atk = 15;
            break;
            case 4:
            atk = 20;
            break;
        }
        if(Math.abs(tickCount) - lastAttackTick <= 10){
            return;
        }
        lastAttackTick = tickCount;
        isAttacking = true;
    //   int attackX = x + 32 + 30;
    //  int attackY = y + 30;
        for(Mob curMob : mobs){
            if((curMob.x >= x - 36 && curMob.x <= x + 68 && curMob.y >= y - 35 && curMob.y <= y + 99) || (curMob.x + 35 >= x - 37 && curMob.x + 37 <= x + 67 && curMob.y >= y - 37 && curMob.y <= y + 98) || (curMob.x >= x - 35 && curMob.x <= x + 67 && curMob.y + 67 >= y - 36 && curMob.y + 64 <= y + 96) || (curMob.x + 32 >= x - 32 && curMob.x + 32<= x + 64 && curMob.y + 67 >= y - 38 && curMob.y + 7 <= y + 99)){
                System.out.println("hit!");
                curMob.gotHit(atk, this.playerDirection);
            }
        }
    }

    public void paint(Graphics2D context, int x, int y) {
        if (isMoving) {
            switch(playerDirection){
                case Left:
                context.drawImage(playerImages[animationIndex], x + 40, y, -64, 64, null);
                    break;
                case Right:
                context.drawImage(playerImages[animationIndex],x , y, 64, 64, null);
                    break;
                case Up:
                context.drawImage(dPlayerImages[udindex],x , y, 64, 64, null);
                    break;
                case Down:
                context.drawImage(uPlayerImages[udindex],x , y, 64, 64, null);
                    break;
            }
        }
            else{
                switch(playerDirection){
                    case Left:
                    context.drawImage(playerImages[0], x + 40, y, -64, 64, null);
                        break;
                    case Right:
                    context.drawImage(playerImages[0],x , y, 64, 64, null);
                        break;
                    case Up:
                    context.drawImage(dPlayerImages[0],x , y, 64, 64, null);
                        break;
                    case Down:
                    context.drawImage(uPlayerImages[0],x , y, 64, 64, null);
                        break;
                }
            }
        
        if(isAttacking){
            context.setColor(Color.GRAY);
            
        }
        context.setColor(Color.RED);
        //context.drawRect(x-32,y-32,96,128);
        }
    }
