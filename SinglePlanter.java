import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SinglePlanter extends Planter {
    private BufferedImage plantA, plantB;
    
    public SinglePlanter(int x, int y) {
        posX1  = x;
        posY1  = y;
        width = tileSize;
        height = tileSize*2;
        posX2 = posX1 + width;
        posY3 = posY1 + height;
        initImages();
    }
    
    @Override
    public int getY1() { return posY1 + tileSize + 16; }

    @Override
    public int getX1() { return posX1 + 12; }

    @Override
    public int getX2() { return posX2 - 12; }

    /*
     *  draws the single planter which takes up the tile at its original position and one tile below. 
     *  The width and height of each section is one true tile size
     */

    @Override
    public void draw(Graphics g) {
        g.drawImage(plantA, posX1, posY1, tileSize, tileSize,null);
        g.drawImage(plantB, posX1, posY1 + tileSize, tileSize, tileSize,null);
    }

    /*
     *  initializes SinglePlanter images, importing from sprites folder
     */

    @Override
    public void initImages() {
        try {
            plantA = ImageIO.read(getClass().getResourceAsStream("/sprites/plantA.png"));
            plantB = ImageIO.read(getClass().getResourceAsStream("/sprites/plantB.png"));
        } catch(IOException e) {}
    }
}