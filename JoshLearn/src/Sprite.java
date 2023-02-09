import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Sprite {
    int hp = 10;
    int def = 0;
    int x = 200;
    int y = 200;
    int xSide = 32;
    int ySide = 64;
    int n = 4;
    int speed = 1;
    int knockback = 15;
    int nextKnockbackX = 0;
    int nextKnockbackY = 0;
    Direction monsterDirection = Direction.Right;
    Random r = new Random();
    int tickCount = 0;
    int lastHitTick = -1000;

    public void paint(Graphics g) {
        Graphics2D context = (Graphics2D) g;
        context.setColor(Color.PINK);
        context.fillRect(x, y, xSide, ySide);
    }

    public boolean canMoveSprite(int X, int Y, Tile[][] tiles) {
        if (doesPointCollide(X, Y, tiles) && doesPointCollide(X + 40, Y, tiles) && doesPointCollide(X, Y + 63, tiles)
                && doesPointCollide(X + 40, Y + 64, tiles) && X > 0 && X < 672 && Y > 0 && Y < 672
                && doesPointCollide(X + 20, Y, tiles) && doesPointCollide(X, Y + 30, tiles)
                && doesPointCollide(X + 45, Y + 30, tiles)
                && doesPointCollide(X + 20, Y + 60, tiles)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean doesPointCollide(int X, int Y, Tile[][] tiles) {
        if (X < 0 || X > 703 || Y < 0 || Y > 703 || tiles[(int) Math.floor(X / 32)][(int) Math.floor(Y / 32)].solid) {
            return false;
        } else {
            return true;
        }
    }

    protected void tick() {

    }

    public void tick(Tile[][] tiles, Player dude, ArrayList<Mob> mobs) {
        tickCount++;
        tick();
        if (nextKnockbackX > 0) {
            if (canMoveSprite(nextKnockbackX, y, tiles)) {
                x = nextKnockbackX;
            }
            nextKnockbackX = 0;
        }
        if (nextKnockbackY > 0) {
            if (canMoveSprite(x, nextKnockbackY, tiles)) {
                y = nextKnockbackY;
            }
            nextKnockbackY = 0;
        }
        switch (monsterDirection) {
            case Up:
                if (canMoveSprite(x, y - speed, tiles)) {
                    y -= speed;
                }
                break;
            case Left:
                if (canMoveSprite(x - speed, y, tiles)) {
                    x -= speed;
                }
                break;
            case Down:
                if (canMoveSprite(x, y + speed, tiles)) {
                    y += speed;
                }
                break;
            case Right:
                if (canMoveSprite(x + speed, y, tiles)) {
                    x += speed;
                }
                break;
        }
        switch (monsterDirection) {
            case Up:
                if (!canMoveSprite(x, y - speed, tiles)) {
                    n = r.nextInt(4);
                    System.out.println(n);
                    switch (n) {
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
                    switch (n) {
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
                    switch (n) {
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
                    switch (n) {
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

    public void gotHit(int dmg, Direction attackDirection){
        hp -= (dmg - def);
        System.out.println("hitPoints: " + hp);
        if(hp <= 0){
            System.out.println("IM DEAD ):");
        }

        lastHitTick = tickCount;
        switch(attackDirection){
            case Right:        
                nextKnockbackX = x+knockback;
            break;
            case Left:
                nextKnockbackX = x - knockback;
            break;
            case Down:
                nextKnockbackY = y + knockback;
                break;
            case Up:
                nextKnockbackY = y - knockback;
                break;
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }
}
