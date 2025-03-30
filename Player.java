import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Character {
    private KeyTracker keyTracker;
    private BufferedImage crouch;
    boolean collision;
    protected static BufferedImage playerIdle;

    public Player(KeyTracker keyTracker) {
        this.keyTracker = keyTracker;
        inventory = new Inventory(this, keyTracker);
        initPositionValues();
        initImages();
        currImage = idle;
        direction = Direction.IDLE;
    }

    /*
     *  initializes default positional values such as coordinates, delta distance (displacement each cycle),
     *  player radius, etc. diagonal delta distance calculates a diagonal displacement with the same magnitude as
     *  regular delta distance. These values are used in many places and define the player's movement and reach
     *  when picking up collectables
     */

    public void initPositionValues() {
        posX1 = (int)(((MainPanel.MAXCOLUMNS/2)-0.5) * tileSize);  //  player starts at center window
        posY1 = (int)(((MainPanel.MAXROWS/2)-0.5) * tileSize);
        deltaPosition = 6 * MainPanel.SCALE;
        deltaPositionDiag = (int) (deltaPosition/Math.sqrt(2));
        radius = tileSize;
        centerX = posX1 + tileSize/2;
        centerY = posY1 + tileSize/2;
    }

    /*
     *  updating the player consists of updating the direction they face and subsequently the player image used,
     *  the player's coordinates, and their inventory
     */

    @Override
    public void update() {
        updateDirection();
        updateImage();
        updatePosition();
        updateInventory();
    }

    /*
     *  the player's direction is updated based on the combination of keys pressed. Directions correspond to enums
     */

    public void updateDirection() {
        if (keyTracker.leftPressed && keyTracker.upPressed)         { direction = Direction.NORTHWEST; }
        else if (keyTracker.leftPressed && keyTracker.downPressed)  { direction = Direction.SOUTHWEST; }
        else if (keyTracker.rightPressed && keyTracker.upPressed)   { direction = Direction.NORTHEAST; }
        else if (keyTracker.rightPressed && keyTracker.downPressed) { direction = Direction.SOUTHEAST; }
        else if (keyTracker.leftPressed)                            { direction = Direction.LEFT; }
        else if (keyTracker.rightPressed)                           { direction = Direction.RIGHT; }
        else if (keyTracker.upPressed)                              { direction = Direction.UP; }
        else if (keyTracker.downPressed)                            { direction = Direction.DOWN; }
        else                                                        { direction = Direction.IDLE; }
    }
    
    /*
     *  unless the player is crouched (shift pressed), their image is determined by character's image update method
     */

    @Override
    public void updateImage() {
        if (keyTracker.shiftPressed) { currImage = crouch; }
        else { super.updateImage(); }
    }

    /*
     *  a player's future position is dependent on whether they experience a collision with an obstacle. 
     *  Character's test obstacle collisions runs through all obstacles to see if this occurs, if not, the 
     *  player's position is successfully updated.
     */

    @Override
    public void updatePosition() {
        collision = testObstacleCollisions();
        if (!collision) { super.updatePosition(); }
    }

    /*
     *  if the player attempts to pick up an item, this calls the inventory add method if the item is within player reach,
     *  i.e. they collide. If the player attempts to drop an item, call the inventory drop method, passing the current hotbar
     *  key selected.
     */

    @Override
    public void updateInventory() {
        if (keyTracker.eTyped) { 
            for (Collectable c : MainPanel.collectables) {
                if (c.hasCollision(this) && !c.inInventory) { c.inInventory = inventory.addItem(c); break; }
            }
        }
        if (keyTracker.qTyped) { inventory.dropItem(keyTracker.typedNum); }
    }

    /*
     *  initializes player images, importing from sprites folder
     */

    @Override
    public void initImages() {  //  read in all of the player sprites
        try {
            idle    = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_idleV2.png"));
            playerIdle = idle;
            leftA   = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_leftA.png"));
            leftB   = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_leftB.png"));
            rightA  = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_rightA.png"));
            rightB  = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_rightB.png"));
            upA     = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_upA.png"));
            upB     = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_upB.png"));
            downA   = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_downA.png"));
            downB   = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_downB.png"));
            crouch   = ImageIO.read(getClass().getResourceAsStream("/sprites/player1_crouch.png"));
        } catch (IOException e) {}
    }

    /*
     *  various getter methods mainly used by other classes to operate based on player positioning or modify inventory
     */

    @Override   public Inventory getInventory() { return inventory; }
    @Override   public int getDelta() { return deltaPosition; }
    @Override   public int getDeltaDiag() { return deltaPositionDiag; }
    @Override   public int getX1() { return posX1; }
    @Override   public int getY1() { return posY1; }
    @Override   public int getY3() { return posY1 + tileSize; }
    @Override   public int getCenterX() { return centerX; }
    @Override   public int getCenterY() { return centerY; }
    @Override   public Direction getDirection() { return direction; }
    @Override   public int getRadius() { return radius; }}