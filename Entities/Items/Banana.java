package Entities.Items;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import Entities.Characters.Character;

public class Banana extends Collectable {
    public static final int health = 10;
    private BufferedImage idleBanana, collectBanana;

    public Banana(int x, int y) {
        initPositionValues(x, y);
        initImages();
        currImage = idleBanana;
    }

    /*
     *  updating Banana consists of updating its position and subsequent image
     *  @param  c   image should be updated accordingly if provided character is within reach (has a collision)
     */
    @Override
    public void update(Character c) {
        updateImage(c, idleBanana, collectBanana);
    }

    /*
     *  initializes Banana images, importing from sprites folder
     */
    @Override
    public void initImages() {
        try {
            idleBanana = ImageIO.read(getClass().getResourceAsStream("/sprites/idleBanana.png"));
            collectBanana = ImageIO.read(getClass().getResourceAsStream("/sprites/collectBanana.png"));
        } catch(IOException e) {}
    }
}