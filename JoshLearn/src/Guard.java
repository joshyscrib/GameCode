import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

// Player 
public class Guard {
    Direction playerDirection = Direction.Right;
    int tickCount = 0;
    private int animationIndex = 0;
    int hp = 100;
    int atk = 5;
    int def = 2;
    boolean isMoving = false;
    Item[] inventory;

    boolean facingLeft = false;
    private Image[] playerImages = new Image[4];
    public Guard(){
        URL img1 = getClass().getClassLoader().getResource("images/wolf-2.png");
        URL img2 = getClass().getClassLoader().getResource("images/wolf-1.png");
        URL img3 = getClass().getClassLoader().getResource("images/wolf-3.png");
        try{
            playerImages[0] = ImageIO.read(img1);
            playerImages[1] = ImageIO.read(img2);
            playerImages[2] = ImageIO.read(img3);
            playerImages[3] = playerImages[1];
        }
        catch(IOException ex){}
    }

    public void tick() {
        tickCount++;
        if(tickCount % 6 == 0){
            animationIndex++;
            if(animationIndex >= playerImages.length){
                animationIndex = 0;
            }
        }
    }

    public void paint(Graphics2D context, int x, int y) {
        if(isMoving){
            if(playerDirection == Direction.Left){
                context.drawImage(playerImages[animationIndex], x + 40, y, -64, 64, null);
            }
            else{
                context.drawImage(playerImages[animationIndex], x, y, null);
            }
        }
        else{
            if(playerDirection == Direction.Left){
                context.drawImage(playerImages[0], x + 40, y, -64, 64, null);
            }
            else{
                context.drawImage(playerImages[0], x, y, null);
            }
        }
}
}