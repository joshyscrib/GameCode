import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;
import javax.imageio.ImageIO;

public class Fireball extends Mob{
    BufferedImage fireballImg;
    Direction direction = Direction.Left;
    double dx;
    double dy;
    double randHyp = 0.5;
    
    public Fireball(int s, Direction d, ArrayList<Mob> mobs, double angle) {
        
        super(s);
    randHyp = Math.random(); 
    if(randHyp <= 0.29){
        isFast = true;
    }
        for(int i = 0; i < mobs.size(); i++){
                if(mobs.get(i).getClass() == Princess.class){
                    direction = mobs.get(i).monsterDirection;
                }
            }
        
        try {
            fireballImg = ImageIO.read(getClass().getClassLoader().getResource("images/fireball.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dy = 8.0 * Math.sin(Math.toRadians(angle));
        dx = 8.0 * Math.cos(Math.toRadians(angle));
        
    }
    public void paint(Graphics g){
        Graphics2D context = (Graphics2D) g;
        context.drawImage(fireballImg, x, y, 32, 32, null);
    }
    @Override  
    public void tick(Tile[][] tiles, Player dude, ArrayList<Mob> mobs) {
            
                x += dx;
                y += dy;
                if(randHyp <= 0.29){
                x += dx;
                y += dy;
                }
            if(x < -10 || x > 690 || y < -10 || y > 690){
                mobs.remove(this);
            }
    }
}
