package Entities.Items;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import Entities.Characters.Character;

public class Shawarma extends Collectable {
    public static final int health = 50;
    private BufferedImage idleShawarma, collectShawarma;

    public Shawarma(int x, int y) {
        initPositionValues(x, y);
        initImages();
        currImage = idleShawarma;
    }

    /*
     *  updating Shawarma consists of updating its position and subsequent image
     *  @param  c   image should be updated accordingly if provided character is within reach (has a collision)
     */
    @Override
    public void update(Character c) {
        updateImage(c, idleShawarma, collectShawarma);
    }

    /*
     *  initializes Shawarma images, importing from sprites folder
     */
    @Override
    public void initImages() {
        try {
            idleShawarma = ImageIO.read(getClass().getResourceAsStream("/sprites/idleShawarma.png"));
            collectShawarma = ImageIO.read(getClass().getResourceAsStream("/sprites/collectShawarma.png"));
        } catch(IOException e) {}
    }
}