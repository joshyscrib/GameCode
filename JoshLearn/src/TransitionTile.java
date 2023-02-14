import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class TransitionTile extends Tile{
    private static Image image = null;
    @Override
    public void drawTile(Graphics2D context, int x, int y){
        super.drawTile(context, x, y);
        context.drawImage(image, x, y, null);
    }
    public TransitionTile(){
        if(image == null){
        URL img = getClass().getClassLoader().getResource("images/barsTile.png");
        try{
        image = ImageIO.read(img);
        System.out.println("LOADING IMAGE BEEP BOOP");
        }
        catch(IOException ex){
            System.out.println("EXCEPTION ):  " + ex);
        }
    }
        
    } 
}
