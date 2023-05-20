import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
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
    BufferedImage dagImage;
    BufferedImage clayImage;
    BufferedImage macImage;
    BufferedImage darkImage;
    double rotation = 0;

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

        //sets color for shield
        Color bubbleColor = new Color(47, 207, 224,75);

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
        java.net.URL img1 = getClass().getClassLoader().getResource("images/dagger.png");
            try{
                dagImage = ImageIO.read(img1);
    
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
            java.net.URL img2 = getClass().getClassLoader().getResource("images/claymore.png");
            try{
                clayImage = ImageIO.read(img2);
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
            java.net.URL img3 = getClass().getClassLoader().getResource("images/mace.png");
            try{
                macImage = ImageIO.read(img3);
    
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
            java.net.URL img4 = getClass().getClassLoader().getResource("images/darkSword.png");
            try{
                darkImage = ImageIO.read(img4);
    
            }
            catch(IOException ex){
                System.out.println("EXCEPTION ):  " + ex);
            }
            int lx = placeX;
            int ly = placeY;
      
       if(dude.isAttacking){
        switch(dude.playerDirection){
            case Up:
            ly -= 60;
            rotation = Math.toRadians(0);
                break;
            case Left:
                lx -= 60;
            rotation = Math.toRadians(270);
                break;
            case Down:
            ly += 30;
            rotation = Math.toRadians(180);
                break;
            case Right:
            lx += 20;
            rotation = Math.toRadians(90);
                break;
            
        }
        if(curWeapon >= 4){
            rotation -= Math.toRadians(45);
        }
        AffineTransform tx = AffineTransform.getRotateInstance(rotation, 33, 33);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        switch(curWeapon){
            case 1:
            context.drawImage(op.filter(dagImage , null), lx, ly, null);
            break;
            case 2:
            context.drawImage(op.filter(clayImage , null), lx, ly, null);
            break;
            case 3:
            context.drawImage(op.filter(macImage , null), lx, ly, null);
            break;
            case 4:
            context.drawImage(op.filter(darkImage , null), lx, ly, null);
            break;
        }
       }
       // context.drawImage(image1, 717, 252, null);
       if(hasCroissant){
        context.drawImage(cImage, 712, 348, 70, 70, null);
       }
       if(hasPomegranate){
        context.drawImage(pImage, 717, 455, 60, 60, null);
       }
       if(hasIcee){
        int amountLeft = 0;
        context.drawImage(iImage, 718, 555, 58, 58, null);
        if(dude.playerDirection == Direction.Left){
            amountLeft = 10;
        }
        else{
            amountLeft = 0;
        }
        if(iceeProtection <= 25){
            bubbleColor = new Color(250, 0, 0, 75);
        }
        else{
            bubbleColor = new Color(47, 207, 224,75);
        }

        context.setColor(bubbleColor);
        context.fillOval(placeX - 12 - amountLeft, placeY - 7, 75, 75);
       }
       context.setColor(Color.WHITE);
       context.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
       context.drawString("Score :" + score, 10, 730);
       if(listener.devMode){
        context.setColor(Color.RED);
       context.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
       context.drawString("DEV", 10, 760);
       }
    }
}
