import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Sprite {
    int hp = 10;
    int def = 0;
    int x = 20;
    int y = 20;
    int xSide = 32;
    int ySide = 64;
    int n = 4;
    int speed = 3;
    Direction monsterDirection = Direction.Right;
    Random r = new Random();
    public void paint(Graphics g) {
        Graphics2D context = (Graphics2D) g;
        context.setColor(Color.PINK);
        context.fillRect(x, y, xSide, ySide);
    }

    public boolean canMoveSprite(int X, int Y, Tile[][] tiles) {
        if (doesPointCollide(X, Y, tiles) && doesPointCollide(X + 40, Y, tiles) && doesPointCollide(X, Y + 63, tiles)
                && doesPointCollide(X + 40, Y + 64, tiles) && X > 0 && X < 672 && Y > 0 && Y < 672
                && doesPointCollide(X + 20, Y, tiles) && doesPointCollide(X, Y + 30, tiles) && doesPointCollide(X + 45, Y + 30, tiles)
                && doesPointCollide(X + 20, Y + 60, tiles)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean doesPointCollide(int X, int Y, Tile[][] tiles) {
        if (X < 0 || X > 703 || Y < 0 || Y > 703 || tiles[(int) Math.floor(X / 32)][(int) Math.floor(Y / 32)].solid) {
            return false;
        }
        else{
        return true;
        }
    } 

    public void tick(Tile[][] tiles, Player dude, ArrayList<Mob> mobs) {
        switch (monsterDirection) {
            case Up:
            if(canMoveSprite(x, y - speed, tiles)){
                y -= speed;
            }
                break;
            case Left:
            if(canMoveSprite(x - speed, y, tiles)){
                x -= speed;
            }
                break;
            case Down:
            if(canMoveSprite(x, y + speed, tiles)){
                y += speed;
            }
                break;
            case Right:
            if(canMoveSprite(x + speed, y, tiles)){
                x += speed;
            }
                break;
        }
        switch(monsterDirection){
            case Up:
            if (!canMoveSprite(x, y - speed, tiles)) {
            n = r.nextInt(4);
            System.out.println(n);
            switch(n){
                case 0:
                monsterDirection = Direction.Left;
                break;
                case 1:
                monsterDirection = Direction.Up;
                break;
                case 2:
                monsterDirection = Direction.Down;
                break;
                case 3:
                monsterDirection = Direction.Right;
                break;

            }
        }
        break;
        case Left:
            if (!canMoveSprite(x - speed, y, tiles)) {
            n = r.nextInt(4);
            System.out.println(n);
            switch(n){
                case 0:
                monsterDirection = Direction.Left;
                break;
                case 1:
                monsterDirection = Direction.Up;
                break;
                case 2:
                monsterDirection = Direction.Down;
                break;
                case 3:
                monsterDirection = Direction.Right;
                break;

            }
        }
        break;
        case Down:
            if (!canMoveSprite(x, y + speed, tiles)) {
            n = r.nextInt(4);
            System.out.println(n);
            switch(n){
                case 0:
                monsterDirection = Direction.Left;
                break;
                case 1:
                monsterDirection = Direction.Up;
                break;
                case 2:
                monsterDirection = Direction.Down;
                break;
                case 3:
                monsterDirection = Direction.Right;
                break;

            }
        }
        break;
        case Right:
            if (!canMoveSprite(x + speed, y, tiles)) {
            n = r.nextInt(4);
            System.out.println(n);
            switch(n){
                case 0:
                monsterDirection = Direction.Left;
                break;
                case 1:
                monsterDirection = Direction.Up;
                break;
                case 2:
                monsterDirection = Direction.Down;
                break;
                case 3:
                monsterDirection = Direction.Right;
                break;

            }
        }
        break;
        }
        
    }
    public void gotHit(int dmg){
        hp -= (dmg - def);
        System.out.println("hitPoints: " + hp);
        if(hp <= 0){
            System.out.println("IM DEAD ):");
        }
    }
    public boolean isDead(){
        return hp <= 0;
    }
}   
