import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Tile implements Serializable{
    int tickCount = 0;
    Image image;

    public void tick() {
        tickCount++;
    }

    public boolean solid = false;
    public static int TileSide = 32;

    public void drawTile(Graphics2D context, int x, int y) {
        if (image != null) {
            context.drawImage(image,x, y, null);
        } else {
            context.setColor(Color.WHITE);
            context.fillRect(x, y, TileSide, TileSide);
        }
    }

    public void loadImage(String path) {
        URL imgUrl = getClass().getClassLoader().getResource(path);
        try {
            image = ImageIO.read(imgUrl);
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

}
