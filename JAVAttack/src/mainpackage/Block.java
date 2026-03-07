package mainpackage;

import java.awt.Image;

public class Block {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image img;
    private boolean alive = true;   // for aliens
    private boolean used = false;   // for bullets
    private PowerupType type;       // for powerups

    //setters
    Block(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }
    int setX(int x) { return this.x = x; }
    int setY(int y) { return this.y = y; }
    boolean setAlive(boolean alive) { return this.alive = alive; }
    boolean setUsed(boolean used) { return this.used = used; }
    PowerupType setType(PowerupType type) { return this.type = type; }

    // getters 
    
    int getX() { return x; }
    int getY() { return y; }
    int getWidth() { return width; }
    int getHeight() { return height; }
    Image getImg() { return img; }
    boolean isAlive() { return alive; }
    boolean isUsed() { return used; }
    PowerupType getType() { return type; }

    
}
