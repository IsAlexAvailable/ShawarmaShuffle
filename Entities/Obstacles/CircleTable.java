package Entities.Obstacles;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import Entities.Hitbox;

public class CircleTable extends Table {
    private BufferedImage circleTable;

    public CircleTable(int x, int y) {
        posX1  = x;
        posY1  = y;
        width = tileSize;
        height = tileSize;
        posX2 = posX1 + width;
        posY2 = posY1 + height;
        hitbox = new Hitbox(posX1, posY1, 0, 24, width, height-24);
        initImages();
    }

    /*
     *  draws the circle table. Its width and height is one true tile size
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(circleTable, posX1, posY1, tileSize, tileSize,null);
        super.draw(g);
    }
    
    /*
     *  initializes CircleTable image, importing from sprites folder
     */
    @Override
    public void initImages() {
        try {
            circleTable = ImageIO.read(getClass().getResourceAsStream("/sprites/circleTable.png"));
        } catch(IOException e) {}
    }
}