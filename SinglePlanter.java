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
        posY2 = posY1 + height;
        hitbox = new Hitbox(posX1, posY1, 8, tileSize+24, width-16, tileSize-24);
        initImages();
    }

    /*
     *  draws the single planter which takes up the tile at its original position and one tile below. 
     *  The width and height of each section is one true tile size
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(plantA, posX1, posY1, tileSize, tileSize,null);
        g.drawImage(plantB, posX1, posY1 + tileSize, tileSize, tileSize,null);
        super.draw(g);
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