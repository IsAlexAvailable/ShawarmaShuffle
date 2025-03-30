import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameBackground extends Entity {
    private BufferedImage[][] tileArray;
    private BufferedImage backgroundTile;
    private int columns = MainPanel.MAXCOLUMNS;
    private int rows = MainPanel.MAXROWS;
    
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
        int characterX = c.getX1(), characterY = c.getY1();
        Direction characterDirection = c.getDirection();
        int characterDelta = c.getDelta();
        int characterDeltaDiag = c.getDeltaDiag();

        switch (characterDirection) {
            case Direction.LEFT: return (characterX - characterDelta) <= 0;
            case Direction.RIGHT: return (characterX + characterDelta) >= (screenWidth - tileSize);
            case Direction.UP: return (characterY - characterDelta) <= 0;
            case Direction.DOWN: return (characterY + characterDelta) >= (screenHeight - tileSize);
            case Direction.NORTHEAST: return (characterY - characterDeltaDiag) <= 0 || ((characterX + characterDeltaDiag) >= (screenWidth - tileSize));
            case Direction.NORTHWEST: return (characterY - characterDeltaDiag) <= 0 || (characterX - characterDeltaDiag) <= 0;
            case Direction.SOUTHEAST: return ((characterY + characterDeltaDiag) >= (screenHeight - tileSize)) || (characterX + characterDeltaDiag) >= (screenWidth - tileSize);
            case Direction.SOUTHWEST: return ((characterY + characterDeltaDiag) >= (screenHeight - tileSize)) || (characterX - characterDeltaDiag) <= 0;
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