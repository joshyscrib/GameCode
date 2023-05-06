import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
public class LevelOne extends Panel{
    public LevelOne(String filename) {
        super(filename);
        //TODO Auto-generated constructor stub
    }
    Image hImage;
    Image cImage;
    Image pImage;
    Image iImage;
    @Override
    public void paint(Graphics g){
        if(hImage == null){
            java.net.URL img = getClass().getClassLoader().getResource("images/healthPotion.png");
            try{
            hImage = ImageIO.read(img);
    
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
        }
        if(cImage == null){
            java.net.URL img = getClass().getClassLoader().getResource("images/croissant.png");
            try{
                cImage = ImageIO.read(img);
    
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
        }
        if(pImage == null){
            java.net.URL img = getClass().getClassLoader().getResource("images/pomegranate.png");
            try{
                pImage = ImageIO.read(img);
    
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
        }
        if(iImage == null){
            java.net.URL img = getClass().getClassLoader().getResource("images/Icee.png");
            try{
                iImage = ImageIO.read(img);
    
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
                            context.drawImage(hImage, x * 32, y * 32, 32, 32, null);
                        }
                        if(items[x][y].getClass() == Croissant.class){
                            context.setColor(Color.RED);
                            context.drawImage(cImage, x * 32, y * 32, 32, 32, null);
                        }
                        if(items[x][y].getClass() == Pomegranate.class){
                            context.setColor(Color.RED);
                            context.drawImage(pImage, x * 32, y * 32, 32, 32, null);
                        }
                        if(items[x][y].getClass() == Icee.class){
                            context.setColor(Color.RED);
                            context.drawImage(iImage, x * 32, y * 32, 32, 32, null);
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
