import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameBackground extends Entity {
    private BufferedImage[][] tileArray;
    private BufferedImage backgroundTile;
    private int columns = Constants.MAXCOLUMNS;
    private int rows = Constants.MAXROWS;
    
    public GameBackground() {
        tileArray = new BufferedImage[columns][rows];
        initImages();
    }

    /*
     *  initializes GameBackground image, importing from sprites folder
     */
    @Override
    public void initImages() {
        try {
            backgroundTile = ImageIO.read(getClass().getResourceAsStream("/sprites/backgroundTileC.png"));
        } catch (IOException e) {}
    }

    /*
     *  draws the background. Runs through a tile array, which represents the tiles that fit on-screen, and draws
     *  the tile at each position
     */
    @Override
    public void draw(Graphics g) {
        for (int x = 0; x < tileArray.length; x++) {
            for (int y = 0; y < tileArray[x].length; y++) { g.drawImage(backgroundTile, x * tileSize, y * tileSize, tileSize, tileSize, null); }
        }
    }

    /*
     *  this collision test determines if a character's future position, based on their direction, is outside
     *  of screen's bounding coordinates
     *  @param  c   the character tested against the background boundaries
     *  @return if the character collides with out-of-bound coordinates
     */
    @Override
    public boolean hasCollision(Character c) {
        int characterX1 = c.getHitbox().getX1(), characterY1 = c.getHitbox().getY1();
        int characterX2 = c.getHitbox().getX2(), characterY2 = c.getHitbox().getY2();
        MovementState characterDirection = c.getDirection();
        int characterDelta = c.getDelta();
        int characterDeltaDiag = c.getDeltaDiag();

        switch (characterDirection) {
            case LEFT: return (characterX1 - characterDelta) < 0;
            case RIGHT: return (characterX2 + characterDelta) > (screenWidth);
            case UP: return (characterY1 - characterDelta) < 0;
            case DOWN: return (characterY2 + characterDelta) > (screenHeight);
            case NORTHEAST: return (characterY1 - characterDeltaDiag) < 0 || ((characterX2 + characterDeltaDiag) > (screenWidth));
            case NORTHWEST: return (characterY1 - characterDeltaDiag) < 0 || (characterX1 - characterDeltaDiag) < 0;
            case SOUTHEAST: return ((characterY2 + characterDeltaDiag) > (screenHeight)) || (characterX2 + characterDeltaDiag) > (screenWidth);
            case SOUTHWEST: return ((characterY2 + characterDeltaDiag) > (screenHeight)) || (characterX1 - characterDeltaDiag) < 0;
            default: return false;  //  SHOULD NOT HAPPEN
        }
    }

    @Override
    public int getY2(){ return -50; }
}