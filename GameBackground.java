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
        int characterX = c.getX1()+16, characterY = c.getY1();
        int characterX2 = c.getX1() -16, characterY3 = c.getY1();
        MovementState characterDirection = c.getDirection();
        int characterDelta = c.getDelta();
        int characterDeltaDiag = c.getDeltaDiag();

        switch (characterDirection) {
            case LEFT: return (characterX - characterDelta) < 0;
            case RIGHT: return (characterX2 + characterDelta) > (screenWidth - tileSize);
            case UP: return (characterY - characterDelta) < 0;
            case DOWN: return (characterY3 + characterDelta) > (screenHeight - tileSize);
            case NORTHEAST: return (characterY - characterDeltaDiag) < 0 || ((characterX2 + characterDeltaDiag) > (screenWidth - tileSize));
            case NORTHWEST: return (characterY - characterDeltaDiag) < 0 || (characterX - characterDeltaDiag) < 0;
            case SOUTHEAST: return ((characterY3 + characterDeltaDiag) > (screenHeight - tileSize)) || (characterX2 + characterDeltaDiag) > (screenWidth - tileSize);
            case SOUTHWEST: return ((characterY3 + characterDeltaDiag) > (screenHeight - tileSize)) || (characterX - characterDeltaDiag) < 0;
            default: return false;  //  SHOULD NOT HAPPEN
        }
    }

    @Override
    public int getX1() { throw new UnsupportedOperationException("Unimplemented method 'getX'"); }
    @Override
    public int getY1() { throw new UnsupportedOperationException("Unimplemented method 'getY'"); }
    @Override
    public int getY3(){ throw new UnsupportedOperationException("Unimplemented method 'getY3'"); }
}