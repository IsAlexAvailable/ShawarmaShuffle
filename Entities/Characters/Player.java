package Entities.Characters;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import Entities.Hitbox;
import Entities.Inventory;
import Entities.Items.Collectable;
import GameControl.Constants;
import GameControl.GamePanel;

public class Player extends Character {
    private BufferedImage crouch;
    private boolean hasCollision;
    protected static BufferedImage playerIdle;
    private boolean canMove, movingLeft, movingRight, movingUp, movingDown, isCrouching, tryItemPickUp, tryItemDrop;

    public Player() {
        inventory = new Inventory(this);
        initPositionValues();
        initImages();
        hitbox = new Hitbox(posX1, posY1, 16, 32, tileSize-32, tileSize-32);
        direction = MovementState.IDLE;
        currImage = idle;
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
        deltaPosition = 120*Constants.SCALE /Constants.FPS;
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
        updateHitbox();
        updateInventory();
        resetInteractionKeys();
    }

    /*
     *  the player's direction is updated based on the combination of keys pressed. Directions correspond to enums
     */
    public void updateDirection() {
        if      (canMove && movingLeft && movingUp)    { direction = MovementState.NORTHWEST; }
        else if (canMove && movingLeft && movingDown)  { direction = MovementState.SOUTHWEST; }
        else if (canMove && movingRight && movingUp)   { direction = MovementState.NORTHEAST; }
        else if (canMove && movingRight && movingDown) { direction = MovementState.SOUTHEAST; }
        else if (canMove && movingLeft)                { direction = MovementState.LEFT; }
        else if (canMove && movingRight)               { direction = MovementState.RIGHT; }
        else if (canMove && movingUp)                  { direction = MovementState.UP; }
        else if (canMove && movingDown)                { direction = MovementState.DOWN; }
        else                                           { direction = MovementState.IDLE; }
    }
    
    /*
     *  unless the player is crouched (shift pressed), their image is determined by character's image update method
     */
    @Override
    public void updateImage() {
        if (isCrouching) { currImage = crouch; }
        else { super.updateImage(); }
    }

    /*
     *  a player's future position is dependent on whether they experience a collision with an obstacle. 
     *  Character's test obstacle collisions runs through all obstacles to see if this occurs, if not, the 
     *  player's position is successfully updated.
     */
    @Override
    public void updatePosition() {
        hasCollision = testObstacleCollisions();
        if (!hasCollision) { super.updatePosition(); }
    }

    @Override
    public void updateHitbox() {
        hitbox.updatePosition(posX1, posY1);
    }

    /*
     *  if the player attempts to pick up an item, this calls the inventory add method if the item is within player reach,
     *  i.e. they collide. If the player attempts to drop an item, call the inventory drop method, passing the current hotbar
     *  key selected.
     */
    @Override
    public void updateInventory() {
        boolean addToInventory;
        if (tryItemPickUp) { 
            for (Collectable c : GamePanel.getCollectables()) {
                if (c.hasCollision(this) && !c.isInInventory()) { 
                    addToInventory = inventory.addItem(c);
                    c.setInInventory(addToInventory); 
                    break;
                }
            }
        }
        if (tryItemDrop) { inventory.dropActiveItem(); }
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

    public void canMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setIdle() {
        movingLeft = false;
        movingRight = false;
        movingDown = false;
        movingUp = false;
        isCrouching = false;
    }

    public void resetInteractionKeys() {    //  after typing an interaction key and updating interactions we reset typed so it doesn't act as pressed
        tryItemPickUp = false;
        tryItemDrop = false;
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
                movingLeft = isLeftPressed;
            }
            if (keyCode == KeyEvent.VK_RIGHT || keyChar == 'd') { 
                isRightPressed = true;
                movingRight = isRightPressed;
            }
            if (keyCode == KeyEvent.VK_UP || keyChar == 'w') { 
                isUpPressed = true;
                movingUp = isUpPressed;
            }
            if (keyCode == KeyEvent.VK_DOWN || keyChar == 's') { 
                isDownPressed = true;
                movingDown = isDownPressed;
            }
            if (keyCode == KeyEvent.VK_SHIFT) {
                isShiftPressed = true;
                isCrouching = isShiftPressed;
                canMove(!isCrouching);
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
                movingLeft = isLeftPressed;
            }
            if (keyCode == KeyEvent.VK_RIGHT || keyChar == 'd') { 
                isRightPressed = false;
                movingRight = isRightPressed;
            }
            if (keyCode == KeyEvent.VK_UP || keyChar == 'w') { 
                isUpPressed = false;
                movingUp = isUpPressed;
            }
            if (keyCode == KeyEvent.VK_DOWN || keyChar == 's') { 
                isDownPressed = false;
                movingDown = isDownPressed;
            }
            if (keyCode == KeyEvent.VK_SHIFT) { 
                isShiftPressed = false;
                isCrouching = isShiftPressed;
                canMove(!isCrouching);
            }
            if (keyChar == 'e') { 
                isTypedE = false;
                tryItemPickUp = isTypedE;
            }
            if (keyChar == 'q') { 
                isTypedQ = false;
                tryItemDrop = isTypedQ;
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
                    tryItemPickUp = isTypedE;
                    break;
                case 'q': 
                    isTypedQ = true; 
                    tryItemDrop = isTypedQ;
                    break;
            }
        }
    }

     /*
     *  various getter methods mainly used by other classes to operate based on player positioning or modify inventory
     */
    public static BufferedImage getPlayerIdleImage() { return playerIdle; }
     @Override   public Inventory getInventory() { return inventory; }
     @Override   public int getDelta() { return deltaPosition; }
     @Override   public int getDeltaDiag() { return deltaPositionDiag; }
     @Override   public int getCenterX() { return centerX; }
     @Override   public int getCenterY() { return centerY; }
     @Override   public MovementState getDirection() { return direction; }
     @Override   public int getRadius() { return radius; }
}