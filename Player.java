import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Character {
    private BufferedImage crouch;
    boolean collision;
    protected static BufferedImage playerIdle;
    private boolean canMove, movingLeft, movingRight, movingUp, movingDown, crouching, typedE, typedQ;

    public Player() {
        inventory = new Inventory(this);
        initPositionValues();
        initImages();
        currImage = idle;
        direction = Direction.IDLE;
        canMove = true;
    }

    /*
     *  initializes default positional values such as coordinates, delta distance (displacement each cycle),
     *  player radius, etc. diagonal delta distance calculates a diagonal displacement with the same magnitude as
     *  regular delta distance. These values are used in many places and define the player's movement and reach
     *  when picking up collectables
     */

    public void initPositionValues() {
        posX1 = (int)(((Constants.MAXCOLUMNS/2)-0.5) * tileSize);  //  player starts at center window
        posY1 = (int)(((Constants.MAXROWS/2)-0.5) * tileSize);
        deltaPosition = 200 * Constants.SCALE/Constants.FPS;
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
        resetInteractionKeys();
    }

    /*
     *  the player's direction is updated based on the combination of keys pressed. Directions correspond to enums
     */

    public void updateDirection() {
        if      (movingLeft && movingUp)    { direction = Direction.NORTHWEST; }
        else if (movingLeft && movingDown)  { direction = Direction.SOUTHWEST; }
        else if (movingRight && movingUp)   { direction = Direction.NORTHEAST; }
        else if (movingRight && movingDown) { direction = Direction.SOUTHEAST; }
        else if (movingLeft)                { direction = Direction.LEFT; }
        else if (movingRight)               { direction = Direction.RIGHT; }
        else if (movingUp)                  { direction = Direction.UP; }
        else if (movingDown)                { direction = Direction.DOWN; }
        else                                { direction = Direction.IDLE; }
        // if (!canMove) { direction = Direction.IDLE; }
    }
    
    /*
     *  unless the player is crouched (shift pressed), their image is determined by character's image update method
     */

    @Override
    public void updateImage() {
        if (crouching) { currImage = crouch; }
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
        if (typedE) { 
            for (Collectable c : GamePanel.collectables) {
                if (c.hasCollision(this) && !c.inInventory) { 
                    c.inInventory = inventory.addItem(c);
                    break;
                }
            }
        }
        if (typedQ) { inventory.dropActiveItem(); }
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

    public void setCanMove(boolean b) {
        // this.canMove = b;
        if (!b) {
            setMovingLeft(false);
            setMovingRight(false);
            setMovingUp(false);
            setMovingDown(false);
        }
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }
    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public void setTypedE(boolean isTypedE) {
        this.typedE = isTypedE;
    }

    public void setTypedQ(boolean isTypedQ) {
        this.typedQ = isTypedQ;
    }

    public void resetInteractionKeys() {    //  after typing an interaction key and updating interactions we reset typed so it doesn't act as pressed
        typedE = false;
        typedQ = false;
    }

    PlayerKeyListener keyListener = new PlayerKeyListener();

    public KeyListener getKeyListener() {
        return keyListener;
    }

    class PlayerKeyListener implements KeyListener {
        private int keyCode;
        private char keyChar;
        protected boolean isLeftPressed, isRightPressed, isUpPressed, isDownPressed, isShiftPressed;
        protected boolean isTypedE, isTypedQ;
        
        /*
         *  primarily sets booleans corresponding to player movement key actions (wasd and arrows) as well as
         *  shift and escape which correspond to crouching and pausing the game
         */
    
        @Override
        public void keyPressed(KeyEvent e) {
            keyCode = e.getKeyCode();
            keyChar = e.getKeyChar();

            if (keyCode == KeyEvent.VK_LEFT || keyChar == 'a') { 
                isLeftPressed = true;
                setMovingLeft(isLeftPressed);
            }
            if (keyCode == KeyEvent.VK_RIGHT || keyChar == 'd') { 
                isRightPressed = true;
                setMovingRight(isRightPressed);
            }
            if (keyCode == KeyEvent.VK_UP || keyChar == 'w') { 
                isUpPressed = true;
                setMovingUp(isUpPressed);
            }
            if (keyCode == KeyEvent.VK_DOWN || keyChar == 's') { 
                isDownPressed = true;
                setMovingDown(isDownPressed);
            }
            if (keyCode == KeyEvent.VK_SHIFT) {
                isShiftPressed = true;
                setCrouching(isShiftPressed);
                setCanMove(false);
            }
        }
    
        /*
         *  performs the opposite operation as keyPressed method
         */
    
        @Override
        public void keyReleased(KeyEvent e) {
            keyCode = e.getKeyCode();
            keyChar = e.getKeyChar();
    
            if (keyCode == KeyEvent.VK_LEFT || keyChar == 'a') { 
                isLeftPressed = false;
                setMovingLeft(isLeftPressed);
            }
            if (keyCode == KeyEvent.VK_RIGHT || keyChar == 'd') { 
                isRightPressed = false;
                setMovingRight(isRightPressed); 
            }
            if (keyCode == KeyEvent.VK_UP || keyChar == 'w') { 
                isUpPressed = false;
                setMovingUp(isUpPressed); 
            }
            if (keyCode == KeyEvent.VK_DOWN || keyChar == 's') { 
                isDownPressed = false;
                setMovingDown(isDownPressed);
            }
            if (keyCode == KeyEvent.VK_SHIFT) { 
                isShiftPressed = false;
                setCrouching(isShiftPressed);
                setCanMove(true);
            }
            if (keyChar == 'e') { 
                isTypedE = false;
                setTypedE(isTypedE);
            }
            if (keyChar == 'q') { 
                isTypedQ = false;
                setTypedQ(isTypedQ);
            }
        }
    
        /*
         *  calls methods to update typed key booleans corresponding to item pickup/drop and hotbar slot selection
         */
    
        @Override
        public void keyTyped(KeyEvent e) {
            switch (e.getKeyChar()) {
                case 'e': 
                    isTypedE = true; 
                    setTypedE(isTypedE);
                    break;
                case 'q': 
                    isTypedQ = true; 
                    setTypedQ(isTypedQ);
                    break;
            }
        }
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
     @Override   public int getRadius() { return radius; }
}
