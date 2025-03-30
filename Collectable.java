import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Collectable extends Entity {
    protected int centerX, centerY, radius, width = tileSize/2, height = tileSize/2;
    protected BufferedImage currImage;
    protected boolean inInventory = false;

    /*
     *  sets default Collectable position values; similarly defined in other entity subclasses
     *  @param  x   sets the default x position
     *  @param  y   sets the default y position
     */

    public void initPositionValues(int x, int y) {
        posX1 = x;
        posY1 = y;
        centerX = posX1 + width;
        centerY = posY1 + height+16;
        radius = tileSize;
    }

    /*
     *  tests if the provided character collides with the collectable. A collision is defined by the sum of collectable and character radii
     *  @param  c   character for collision test
     *  @return whether the distance between is within the sum of the radii
     */

    @Override
    public boolean hasCollision(Character c) {
        int sumRadii = (int) (Math.pow(radius + c.getRadius()-180, 2));    //  distance for radii to touch (squared) due to following modification
        int distance = (int) (Math.pow(centerX - c.getCenterX(), 2) + Math.pow(centerY - c.getCenterY(), 2)); // distance between centers modified to get rid of square rooting
        return distance <= sumRadii;
    }
    
    /*
     *  masks the inventory contains method to determine whether the collectable is in the provided character's inventory
     *  @param  character   character whose inventory will be tested
     *  @return whether collectable is in inventory
     */

    public boolean inInventory(Character character) { return character.getInventory().containsItem(this); }

    /*
     *  draws the collectable at its current position, centered on the character who drops it,
     *  using the current collectable image. Its width and height are each half of the true tile size
     */

    @Override
    public void draw(Graphics g) {
        g.drawImage(currImage, centerX, centerY, width, height, null);
    }

    public abstract void update(Character c);

    /*  Collectable's position is determined by characters who interact with it
     *  @param  x   new collectable x coordinate
     *  @param  y   new collectable y coordinate 
     */

    public void updatePosition(int x, int y) {
        posX1 = x;
        posY1 = y;
        centerX = posX1 + width;
        centerY = posY1 + height + 16;
    }

    /*
     *  updates the collectable's current image depending on if a character is within reach using collision test
     *  @param  c   tests if the given character is within reach of this collectable
     *  @param  idleImage   if no collision, collectable has its idle image
     *  @param  collectImage    if collision, collectable has its collectable image (highlighted in blue)
     */

    public void updateImage(Character c, BufferedImage idleImage, BufferedImage collectImage) {
        if (hasCollision(c)) { currImage = collectImage; }
        else { currImage = idleImage; }
    }

    @Override
    public int getY3() { return posY1 + height; }

    public void setInInventory(boolean b) { inInventory = b; }
    @Override
    public int getX1() { throw new UnsupportedOperationException("Unimplemented method 'getX'"); }
    @Override
    public int getY1() { throw new UnsupportedOperationException("Unimplemented method 'getY'"); }

}