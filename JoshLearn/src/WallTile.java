import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
public class WallTile extends Tile{
    private static Image image = null;
    @Override
    public void drawTile(Graphics2D context, int x, int y){
        if(image == null){
            URL img = getClass().getClassLoader().getResource("images/wallTile.png");
            try{
            image = ImageIO.read(img);
            System.out.println("LOADING IMAGE BEEP BOOP");
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
        }
        super.drawTile(context, x, y);
        context.drawImage(image, x, y, null);
    }
    public WallTile(){
        solid = true;
       
        
    }

}
