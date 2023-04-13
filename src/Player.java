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
    int hp = 100;
    int atk = 8;
    int def = 0;
    boolean isMoving = false;
    Item[] inventory;
    boolean isAttacking = false;
    boolean facingLeft = false;
    int attackTick = 0;
    private Image[] playerImages = new Image[4];
    boolean hasKey = true;
    int lastAttackTick = 0;

    public Player() {
        URL img1 = getClass().getClassLoader().getResource("images/wolf-2.png");
        URL img2 = getClass().getClassLoader().getResource("images/wolf-1.png");
        URL img3 = getClass().getClassLoader().getResource("images/wolf-3.png");
        try {
            playerImages[0] = ImageIO.read(img1);
            playerImages[1] = ImageIO.read(img2);
            playerImages[2] = ImageIO.read(img3);
            playerImages[3] = playerImages[1];
        } catch (IOException ex) {
        }
    }

    public void tick() {
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

    public void attack(ArrayList<Mob> mobs, int x, int y) {
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
            if (playerDirection == Direction.Left) {
                context.drawImage(playerImages[animationIndex], x + 40, y, -64, 64, null);
            } else {
                context.drawImage(playerImages[animationIndex], x, y, null);
            }
        } else {
            if (playerDirection == Direction.Left) {
                context.drawImage(playerImages[0], x + 40, y, -64, 64, null);
            } else {
                context.drawImage(playerImages[0], x, y, null);
            }
        }
        
        if(isAttacking){
            context.setColor(Color.GRAY);
            switch(playerDirection){
            case Up:
            context.fillRect(x + 15, y - 25, 10, 30);
                break;
            case Left:
            context.fillRect(x,y + 27,-30,10);
                break;
            case Down:
            context.fillRect(x + 15,y + 63,10,30);
                break;
            case Right:
            context.fillRect(x + 30,y + 30,30,10);
                break;
        }
        }
        context.setColor(Color.RED);
        //context.drawRect(x-32,y-32,96,128);
    }
}