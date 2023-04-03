import java.awt.*;
public class LevelOne extends Panel{
    public LevelOne(String filename) {
        super(filename);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void paint(Graphics g){
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
                            context.fillRect(x * 32 + 5, y * 32 + 5, 22, 22);
                        }
                    }
            }
        }
        dude.paint(context,placeX,placeY);
        for(Mob curMob : mobs){
            curMob.paint(context);
        }

        
        if(dude.hp <= 20 || curLevel == 6){
            context.setColor(lowHpColor);
            context.fillRect(0,0,704,704);
        }
    }
}
