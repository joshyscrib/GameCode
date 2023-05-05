import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
public class LevelOne extends Panel{
    public LevelOne(String filename) {
        super(filename);
        //TODO Auto-generated constructor stub
    }
Image image;
    @Override
    public void paint(Graphics g){
        if(image == null){
            java.net.URL img = getClass().getClassLoader().getResource("images/healthPotion.png");
            try{
            image = ImageIO.read(img);
    
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
        }
        Graphics2D context = (Graphics2D) g;
        context.setColor(new Color(30,1,60));
        context.fillRect(0,0,3000,3000);
        super.paint(g);
        for(int x = 0; x < xTiles; x++){
            for(int y = 0; y < yTiles; y++){
                Tile curTile = tiles[x][y];
                    curTile.drawTile(context, x * 32, y * 32);
                    if(items[x][y] != null){
                        if(items[x][y].getClass() == HealthPotion.class){
                            context.setColor(Color.RED);
                            context.drawImage(image, x * 32, y * 32, null);
                        }
                    }
            }
        }
        dude.paint(context,placeX,placeY);
        for(Mob curMob : mobs){
            curMob.paint(context);
        }

        
        if(dude.hp <= 20 || curLevel == 7){
            context.setColor(lowHpColor);
            context.fillRect(0,0,704,704);
        }
       if(dude.isAttacking){
        switch(curWeapon){
            case 1:
            switch(dude.playerDirection){
                case Up:
                context.fillRect(placeX + 15, placeY - 25, 10, 30);
                    break;
                case Left:
                context.fillRect(placeX,placeY + 27,-30,10);
                    break;
                case Down:
                context.fillRect(placeX + 15,placeY + 63,10,30);
                    break;
                case Right:
                context.fillRect(placeX + 30,placeY + 30,30,10);
                    break;
            }
            break;
            case 2:

            break;
            case 3:

            break;
            case 4:

            break;
        }
       }
    }
}
