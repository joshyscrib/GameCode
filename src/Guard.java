import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Guard extends Mob {

    static int startingHp = 50;
    private Image[] guardImages = new Image[2];
    int guardWidth = 64;
    int tickCount = 0;
    int animationIndex = 0;
    public Guard(int pX, int pY) {
        super(1);
        URL img1 = getClass().getClassLoader().getResource("images/guard-1.png");
        URL img2 = getClass().getClassLoader().getResource("images/guard-2.png");
        try {
            guardImages[0] = ImageIO.read(img1);
            guardImages[1] = ImageIO.read(img2);
        } catch (IOException ex) {
        }

        hp = startingHp;
        x = pX;
        y = pY;
    }
    @Override
    public void paint(Graphics g) {
        Graphics2D context = (Graphics2D) g;
        //chooses how to draw image based on direction
        switch(monsterDirection){
            case Right:
            context.drawImage(guardImages[animationIndex], x + 64, y, -64, 64, null);
            break;
            case Left:
            context.drawImage(guardImages[animationIndex], x, y, 64, 64, null);
            break;
            default:
            context.drawImage(guardImages[animationIndex], x, y, 64, 64, null);
            break;
        }

        if(Math.abs(lastHitTick - tickCount) < 5){
            Color damageColor = new Color(255, 0,0,55);
            context.setColor(damageColor);
            context.fillRect(x+15,y,40,70);
        }

        Color fillColor = new Color(255,255, 255, 50);
        context.setColor(fillColor);
        context.fillRect(x+10,y-10,50,8);
        context.setColor(Color.black);
    
        context.drawRect(x+10,y-10,50,8);

        Color healthColor = new Color(0,255, 0, 90);

        double maxHealthWidth = 48;
        double healthPercent = (double)hp/(double)startingHp;
        maxHealthWidth *= healthPercent;
        context.setColor(healthColor);
        context.fillRect(x+11,y-9,(int)maxHealthWidth,6);
    }
    @Override
    public void tick() {
        tickCount++;
        if (tickCount % 4 == 0) {
            animationIndex++;
            if (animationIndex >= guardImages.length) {
                animationIndex = 0;
            }
        }

    }
}
/*
 * private Image[] playerImages = new Image[4];
 * public Guard(){
 * URL img1 = getClass().getClassLoader().getResource("images/wolf-2.png");
 * URL img2 = getClass().getClassLoader().getResource("images/wolf-1.png");
 * URL img3 = getClass().getClassLoader().getResource("images/wolf-3.png");
 * try{
 * playerImages[0] = ImageIO.read(img1);
 * playerImages[1] = ImageIO.read(img2);
 * playerImages[2] = ImageIO.read(img3);
 * playerImages[3] = playerImages[1];
 * }
 * catch(IOException ex){}
 * }
 * 
 * public void tick() {
 * tickCount++;
 * if(tickCount % 6 == 0){
 * animationIndex++;
 * if(animationIndex >= playerImages.length){
 * animationIndex = 0;
 * }
 * }
 * }
 * 
 * public void paint(Graphics2D context, int x, int y) {
 * if(isMoving){
 * if(playerDirection == Direction.Left){
 * context.drawImage(playerImages[animationIndex], x + 40, y, -64, 64, null);
 * }
 * else{
 * context.drawImage(playerImages[animationIndex], x, y, null);
 * }
 * }
 * else{
 * if(playerDirection == Direction.Left){
 * context.drawImage(playerImages[0], x + 40, y, -64, 64, null);
 * }
 * else{
 * context.drawImage(playerImages[0], x, y, null);
 * }
 * }
 * }
 * }
 */