import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class WoodBarsTile extends Tile {
    int hp = 3;
    private static Image image = null;

    @Override
    public void drawTile(Graphics2D context, int x, int y){
        if(image == null){
            URL img = getClass().getClassLoader().getResource("images/woodBarsTile.png");
            try{
            image = ImageIO.read(img);

            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
        }
        super.drawTile(context, x, y);
        context.drawImage(image, x, y, null);
        
    }

    public WoodBarsTile() {
        solid = true;
        if (image == null) {
            URL img = getClass().getClassLoader().getResource("images/woodBarsTile.png");
            try {
                image = ImageIO.read(img);

            } catch (IOException ex) {
                System.out.println("EXCEPTION ):  " + ex);
            }
        }

    }

}
