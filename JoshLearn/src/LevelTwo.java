import java.awt.*;
public class LevelTwo extends Panel{
    @Override
    public void paint(Graphics g){
        Graphics2D context = (Graphics2D) g;
        context.setColor(new Color(4,255,27));
        context.fillRect(0,0,3000,3000);
        super.paint(g);
    }
}
