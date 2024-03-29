import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
public class FloorTile extends Tile{
    private static Image image = null;
    @Override
    public void drawTile(Graphics2D context, int x, int y){
        super.drawTile(context, x, y);
        context.drawImage(image, x, y, null);
    }
    public FloorTile(){
        if(image == null){
        URL img = getClass().getClassLoader().getResource("images/floorTile.png");
        try{
        image = ImageIO.read(img);

        }
        catch(IOException ex){
            System.out.println("EXCEPTION ):  " + ex);
        }
    }
        
    }

}
