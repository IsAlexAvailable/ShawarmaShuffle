package Entities.Obstacles;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import Entities.Hitbox;

public class QuadPlanter extends Planter {
    private BufferedImage plantA, plantB, plantC, plantD, plantE, plantF;
    
    public QuadPlanter(int x, int y) {
        posX1  = x;
        posY1  = y;
        width = tileSize*4;
        height = tileSize*2;
        posX2 = posX1 + width;
        posY2 = posY1 + height;
        hitbox = new Hitbox(posX1, posY1, 8, tileSize, width-8, tileSize);
        initImages();
    }

    /*
     *  draws the quad planter beginning from its original position, for every tile
     *  it occupies. The width and height of each section is one true tile size
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(plantA, posX1,              posY1, tileSize, tileSize,null); //  top left image
        g.drawImage(plantB, posX1 + tileSize,   posY1, tileSize, tileSize,null); //  top middle image
        g.drawImage(plantB, posX1 + tileSize*2, posY1, tileSize, tileSize,null); //  top middle image
        g.drawImage(plantC, posX1 + tileSize*3, posY1, tileSize, tileSize,null); //  top right image
        g.drawImage(plantD, posX1,              posY1 + tileSize, tileSize, tileSize, null); //  bottom left image
        g.drawImage(plantE, posX1 + tileSize,   posY1 + tileSize, tileSize, tileSize,null);  //  bottom middle image
        g.drawImage(plantE, posX1 + tileSize*2, posY1 + tileSize, tileSize, tileSize,null);  //  bottom middle image
        g.drawImage(plantF, posX1 + tileSize*3, posY1 + tileSize, tileSize, tileSize,null);  //  bottom right image
        super.draw(g);
    }

    /*
     *  initializes QuadPlanter images, importing from sprites folder
     */
    @Override
    public void initImages() {
        try {
            plantA = ImageIO.read(getClass().getResourceAsStream("/sprites/quadPlantA.png"));
            plantB = ImageIO.read(getClass().getResourceAsStream("/sprites/quadPlantB.png"));
            plantC = ImageIO.read(getClass().getResourceAsStream("/sprites/quadPlantC.png"));
            plantD = ImageIO.read(getClass().getResourceAsStream("/sprites/quadPlantD.png"));
            plantE = ImageIO.read(getClass().getResourceAsStream("/sprites/quadPlantE.png"));
            plantF = ImageIO.read(getClass().getResourceAsStream("/sprites/quadPlantF.png"));
        } catch(IOException e) {}
    }
}