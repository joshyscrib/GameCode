import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Guard3 extends Mob {

    static int startingHp = 125;
    private Image[] guardImages = new Image[2];
    int guardWidth = 64;
    int tickCount = 0;
    int animationIndex = 0;
    int speedRand = (int) Math.floor(Math.random() * 10);

    public Guard3(int pX, int pY) {
        super(1);
        switch (speedRand) {
            case 8:
                speed = 2;
                break;
            case 9:
                speed = 3;
                break;
            case 0:
                speed = 4;
                break;
            default:
                speed = 1;
                break;
        }
        URL img1 = getClass().getClassLoader().getResource("images/guard3-1.png");
        URL img2 = getClass().getClassLoader().getResource("images/guard3-2.png");
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
        // chooses how to draw image based on direction
        switch (monsterDirection) {
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

        if (Math.abs(lastHitTick - tickCount) < 5) {
            Color damageColor = new Color(255, 0, 0, 55);
            context.setColor(damageColor);
            context.fillRect(x + 15, y, 40, 70);
        }

        Color fillColor = new Color(255, 255, 255, 50);
        context.setColor(fillColor);
        context.fillRect(x + 10, y - 10, 50, 8);
        context.setColor(Color.black);

        context.drawRect(x + 10, y - 10, 50, 8);

        Color healthColor = new Color(0, 255, 0, 90);

        double maxHealthWidth = 48;
        double healthPercent = (double) hp / (double) startingHp;
        maxHealthWidth *= healthPercent;
        context.setColor(healthColor);
        context.fillRect(x + 11, y - 9, (int) maxHealthWidth, 6);
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