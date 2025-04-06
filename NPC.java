import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class NPC extends Character {
    private Random randGenerator = new Random();
    private int roll;
    boolean collision;

    public NPC(int x, int y, int displace) {
        inventory = new Inventory(this);
        initPositionValues(x, y, displace);
        initImages();
        hitbox = new Hitbox(posX1, posY1, 16, 32, tileSize-32, tileSize-32);
        direction = MovementState.IDLE;
        currImage = idle;
    }

    /*
     *  sets default NPC position values; similarly defined in other entity subclasses
     *  @param  x   sets the default x position
     *  @param  y   sets the default y position
     *  @param  speed   sets the NPC's delta distance
     */
    public void initPositionValues(int x, int y, int displace) {
        posX1 = x;
        posY1 = y;
        deltaPosition = displace * Constants.SCALE/Constants.FPS;
        deltaPositionDiag = (int) (deltaPosition/Math.sqrt(2));
        radius = tileSize;
        centerX = posX1 + tileSize/2;
        centerY = posY1 + tileSize/2;
    }
    
    /*
     *  updating NPC consists of updating their position, then image. In the future NPC will also have an inventory
     */
    @Override
    public void update() {
        // updateDirection();
        updateImage();
        updatePosition();
        updateHitbox();
        // updateInventory();
    }
    
    /*
     *  NPC's position logic is random. First test if they experience a collision with any obstacle. If they
     *  collide, their direction is updated depending on their current direction. Otherwise they may change
     *  their direction by chance.
     */
    @Override
    public void updatePosition() {
        collision = testObstacleCollisions();
        if (collision) {
            if (direction == MovementState.DOWN || direction == MovementState.UP) { updateDirection(0, 2); }
            else if (direction == MovementState.LEFT || direction == MovementState.RIGHT) { updateDirection(2, 4); }
            else { updateDirection(0, 9); }
        }
        else {
            if (shouldChangeDirection()) { updateDirection(0, 9); }
            super.updatePosition();
        }
        // if (!collision) { super.updatePosition(); }
        // else {
        //     if (direction == Direction.DOWN || direction == Direction.UP) { updateDirection(0, 2); }
        //     else if (direction == Direction.LEFT || direction == Direction.RIGHT) { updateDirection(2, 4); }
        //     else { updateDirection(0, 5); }
        // }
    }

    @Override
    public void updateHitbox() {
        hitbox.updatePosition(posX1, posY1);
    }

    // public void updateDirection() {
    //     if (shouldChangeDirection()) { updateDirection(0, 9); }
    // }

    /*
     *  NPC's direction is updated and constrained to the index of range specified by parameters.
     *  Direction is chosen by chance, selecting a direction contained within Direction's direction array
     *  @param  lower   determines the upper (exclusive) index limit
     *  @param  upper   determines the lower (inclusive) index limit
     */
    public void updateDirection(int lower, int upper) {
        roll = randGenerator.nextInt(lower, upper);
        direction = MovementState.directions[roll];
    }

    /*
     *  used to randomly determine if NPC's direction should change, given by a dice roll
     */

    public boolean shouldChangeDirection() {
        roll = randGenerator.nextInt(0, 500);
        return roll <= 10;
    }

    @Override
    public void updateInventory() {
        //  TODO implement when npc becomes relevant
    }

    /*
     *  initializes NPC images, importing from sprites folder
     */
    @Override
    public void initImages() {
        try {
            idle    = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_idle.png"));
            leftA   = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_leftA.png"));
            leftB   = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_leftB.png"));
            rightA  = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_rightA.png"));
            rightB  = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_rightB.png"));
            upA     = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_upA.png"));
            upB     = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_upB.png"));
            downA   = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_downA.png"));
            downB   = ImageIO.read(getClass().getResourceAsStream("/sprites/NPC_downB.png"));
        } catch(IOException e) {}
    }

    /*
     *  getter methods serving similar purpose to those in Player
     */
    @Override public int getCenterX() { return centerX; }
    @Override public int getCenterY() { return centerY; }
    @Override public int getDelta() { return deltaPosition; }
    @Override public int getDeltaDiag() { return deltaPositionDiag; }
    @Override public MovementState getDirection() { return direction; }
    @Override public Inventory getInventory() { return inventory; }
    @Override public int getRadius() { return radius; }
}