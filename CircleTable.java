import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CircleTable extends Table {
    private BufferedImage circleTable;

    public CircleTable(int x, int y) {
        posX1  = x;
        posY1  = y;
        width = tileSize;
        height = tileSize;
        posX2 = posX1 + width;
        posY3 = posY1 + height;
        initImages();
    }

    /*
     *  draws the circle table. Its width and height is one true tile size
     */

    @Override
    public void draw(Graphics g) {
        g.drawImage(circleTable, posX1, posY1, tileSize, tileSize,null);
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

    @Override
    public int getY1() { return posY1 + 16; }
}
