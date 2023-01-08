import java.awt.*;
public class LevelOne extends Panel{
    @Override
    public void paint(Graphics g){
        Graphics2D context = (Graphics2D) g;
        context.setColor(new Color(230,1,255));
        context.fillRect(0,0,3000,3000);
        super.paint(g);
        for(int x = 0; x < xTiles; x++){
            for(int y = 0; y < yTiles; y++){
                Tile curTile = tiles[x][y];
                    curTile.drawTile(context, x * 32, y * 32);
                
            }
        }
        dude.paint(context,placeX,placeY);
    }
}
