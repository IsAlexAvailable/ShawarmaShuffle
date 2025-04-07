package Entities.Obstacles;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import Entities.Hitbox;

public class RectangleTable extends Table {
    private BufferedImage rectangleTableA, rectangleTableB;

    public RectangleTable(int x, int y) {
        posX1  = x;
        posY1  = y;
        width = tileSize*2;
        height = tileSize;
        posX2 = posX1 + width;
        posY2 = posY1 + height;
        hitbox = new Hitbox(posX1, posY1, 8, 20, width-12, height-20);
        initImages();
    }

    /*
     *  draws the rectangle table which takes up the tile at its original position and one tile to the right. 
     *  The width and height of each section is one true tile size
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(rectangleTableA, posX1, posY1, tileSize, tileSize,null);
        g.drawImage(rectangleTableB, posX1 + tileSize, posY1, tileSize, tileSize,null);
        super.draw(g);
    }

    /*
     *  initializes RectangleTable images, importing from sprites folder
     */
    
    @Override
    public void initImages() {
        try {
            rectangleTableA = ImageIO.read(getClass().getResourceAsStream("/sprites/rectangleTableA.png"));
            rectangleTableB = ImageIO.read(getClass().getResourceAsStream("/sprites/rectangleTableB.png"));
        } catch(IOException e) {}
    }
}