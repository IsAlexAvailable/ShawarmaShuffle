import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Character extends Entity {
    protected BufferedImage idle, crouch, leftA, leftB, rightA, rightB, upA, upB, downA, downB, currImage;
    protected Inventory inventory;
    protected int deltaPosition, deltaPositionDiag, radius, centerX, centerY;
    protected MovementState direction;

    private long timeElapsed = 0, startTime = 0, period = 500, lowTime = period/2;
    public static final int CLOCK_LOW = 1, CLOCK_HIGH = 2, CLOCK_IDLE = -1;

    /*
     *  used in character movement animation by returning a periodic clock value, determined in live time
     *  @return gives the current clock value, either HIGH/LOW
     */

    public int getClockSignal() {   //  returns 1 during low period, 2 during high period; used to cycle through character motion sprites
        timeElapsed = System.currentTimeMillis() - startTime;
        if (timeElapsed < lowTime) { return CLOCK_LOW; }
        else if (timeElapsed < period) { return CLOCK_HIGH; }
        else {  //  period elapsed so restart
            timeElapsed = 0;
            startTime = System.currentTimeMillis();
            return CLOCK_LOW;
        } 
    }

    /*
     *  displaces the character from their current position given the direction they're facing and delta distance values.
     *  Additionally updates their center coordinates
     */

    public void updatePosition() {
        switch (direction) {
            case NORTHWEST: posX1 -= deltaPositionDiag; posY1 -= deltaPositionDiag; break;
            case SOUTHWEST: posX1 -= deltaPositionDiag; posY1 += deltaPositionDiag; break;
            case NORTHEAST: posX1 += deltaPositionDiag; posY1 -= deltaPositionDiag; break;
            case SOUTHEAST: posX1 += deltaPositionDiag; posY1 += deltaPositionDiag; break;
            case LEFT: posX1 -= deltaPosition; break;
            case RIGHT: posX1 += deltaPosition; break;
            case UP: posY1 -= deltaPosition; break;
            case DOWN: posY1 += deltaPosition; break;
            default: break; //  CHARACTER IS IDLE SO NO CHANGE
        }
        centerX = getX1() + tileSize/2;
        centerY = getY1() + tileSize/2;
    }
    
    /*
     *  updates the character's current image depending on their current direction.
     *  Note this switch uses an updated syntax style to include multiple conditions
     */

    public void updateImage() {
        switch (direction) {
            case IDLE -> { currImage = idle; break; }
            case RIGHT -> { pickBetweenImages(rightA, rightB); break; }
            case LEFT -> { pickBetweenImages(leftA, leftB); break; }
            case UP, NORTHEAST, NORTHWEST -> { pickBetweenImages(upA, upB); break; }
            case DOWN, SOUTHEAST, SOUTHWEST -> { pickBetweenImages(downA, downB); break; }
            default -> { break; }   //  SHOULD NOT HAPPEN
        }
    }

    /*
     *  sets the current character image given two images to alternate between
     *  @param  imageA  sets current image to this if clock is HIGH
     *  @param  imageB  sets current image to this if clock is LOW
     */

    public void pickBetweenImages(BufferedImage imageA, BufferedImage imageB) {
        int currSignal = getClockSignal();
        if (currSignal == CLOCK_HIGH) { currImage = imageA; }
        else if (currSignal == CLOCK_LOW) { currImage = imageB; }
    }

    /*
     *  runs through all obstacles to determine if the character collides with an obstacle
     *  @return if a collision occurs
     */

    public boolean testObstacleCollisions() {
        for (Entity e : GamePanel.obstacles) { 
            if (e.hasCollision(this)) { return true; } 
        }
        return false;
    }

    /*
     *  draws the character at their current position, using the current character image.
     *  Their width and height values are the same as true tile (post-scaling) dimensions
     */

    @Override
    public void draw(Graphics g) { 
        g.drawImage(currImage, posX1, posY1, tileSize, tileSize, null);
        g.setColor(Color.RED);  //  tilebox
        g.drawRect(posX1+16, posY1+32, tileSize-32, tileSize-32);
    }

    public abstract int getCenterX();
    public abstract int getCenterY();
    public abstract int getDelta();
    public abstract int getDeltaDiag();
    public abstract MovementState getDirection();
    public abstract Inventory getInventory();
    public abstract int getRadius();
    public abstract void update();
    public abstract void updateInventory();
    @Override public boolean hasCollision(Character c) {  return false; }   //  currently unused
}