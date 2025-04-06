import java.awt.Color;
import java.awt.Graphics;

public class Hitbox implements Drawable {
    private int posX1, posY1, posX2, posY2, offsetX, offsetY, width, height;
    private Color color;

    public Hitbox(int posX1, int posY1, int offsetX, int offsetY, int width, int height) {
        this.posX1 = posX1 + offsetX;
        this.posY1 = posY1 + offsetY;
        posX2 = this.posX1 + width;
        posY2 = this.posY1 + height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        color = Color.RED;
    }

    public void updatePosition(int posX1, int posY1) {
        this.posX1 = posX1 + offsetX;
        this.posY1 = posY1 + offsetY;
        posX2 = this.posX1 + width;
        posY2 = this.posY1 + height;
    }

    public void setColor(Color c) {
        color = c;
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawRect(posX1, posY1, width, height);
    }

    public int getX1() {
        return posX1;
    }

    public int getY1() {
        return posY1;
    }

    public int getX2() {
        return posX2;
    }

    public int getY2() {
        return posY2;
    }
}