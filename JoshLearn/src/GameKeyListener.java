import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener {
    Panel gamePanel;
    public boolean uping = false;
    public boolean lefting = false;
    public boolean downing = false;
    public boolean righting = false;
    public boolean attacking = false;

    public GameKeyListener(Panel panel) {

        gamePanel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.keyDown(e.getKeyCode());
        switch (e.getKeyCode()) {
            case 69:
                uping = true;
                break;
            case 83:
                lefting = true;
                break;
            case 68:
                downing = true;
                break;
            case 70:
                righting = true;
                break;
            case 32:
                attacking = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.keyUp(e.getKeyCode());
        switch (e.getKeyCode()) {
            case 69:
                uping = false;
                break;
            case 83:
                lefting = false;
                break;
            case 68:
                downing = false;
                break;
            case 70:
                righting = false;
                break;
            case 32:
                attacking = false;
                break;
        }
    }

}
