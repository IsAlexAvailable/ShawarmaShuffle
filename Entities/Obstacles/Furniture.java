package Entities.Obstacles;
import java.awt.Color;
import java.awt.Graphics;
import Entities.Entity;
import Entities.Hitbox;
import Entities.Characters.Character;
import Entities.Characters.MovementState;
import GameControl.GlobalSettings;

public abstract class Furniture extends Entity {
    protected int width, height;
    protected Hitbox hitbox;

    /*  How furniture coordinates are defined
        *  posX1 = x1, posY1 = y1
        *  posX2 = x2, posY2 = y2
        * 
        *  (x1, y1)  _______________ (x2, y1)
        *           |               |
        *           |               |
        *           |               |
        *  (x1, y2) |_______________| (x2, y2)
        */

    /*
     *  this collision test determines whether a character's future 'bounding' coordinates intersect with a given obstacle's hitbox area.
     *  The tested coordinates are the character's corner points.
     *  Examples: if a character attempts to move up towards a table, the character's bottom y-value, and left and right x-values
     *  are tested; if they attempt to move left towards a table, the character's bottom y-value, and left x-value are tested.
     *  The selected test points vary by direction to allow the character to approach obstacles with a natural distance.
     * 
     *  @param  c   character tested against obstacle collision
     *  @return if the character collides with the obstacle
     */
    @Override
    public boolean hasCollision(Character c) {
        int characterX1 = c.getHitbox().getX1();
        int characterX2 = c.getHitbox().getX2();
        int characterY2 = c.getHitbox().getY2();
        MovementState characterDirection = c.getDirection();
        int characterDelta = c.getDelta();
        int characterDeltaDiag = c.getDeltaDiag();
        int futureCharaX1, futureCharaX2, futureCharaY1, futureCharaY2;

        switch (characterDirection) {
            case LEFT:
                futureCharaX1 = characterX1 - characterDelta;
                return horizontalCollisionTest(futureCharaX1, characterY2);
            case RIGHT:
                futureCharaX2 = characterX2 + characterDelta;
                return horizontalCollisionTest(futureCharaX2, characterY2);
            case UP:
                futureCharaY1 = characterY2 - characterDelta;
                return verticalCollisionTest(futureCharaY1, characterX1, characterX2);
            case DOWN:
                futureCharaY2 = characterY2 + characterDelta;
                return verticalCollisionTest(futureCharaY2, characterX1, characterX2);
            case NORTHWEST:
                futureCharaX1 = characterX1 - characterDeltaDiag;
                futureCharaX2 = characterX2 - characterDeltaDiag;
                futureCharaY2 = characterY2 - characterDeltaDiag;
                return diagonalCollisionTest(futureCharaX1, futureCharaX2, futureCharaY2);
            case SOUTHWEST:
                futureCharaX1 = characterX1 - characterDeltaDiag;
                futureCharaX2 = characterX2 - characterDeltaDiag;
                futureCharaY2 = characterY2 + characterDeltaDiag;
                return diagonalCollisionTest(futureCharaX1, futureCharaX2, futureCharaY2);
            case NORTHEAST:
                futureCharaX1 = characterX1 + characterDeltaDiag;
                futureCharaX2 = characterX2 + characterDeltaDiag;
                futureCharaY2 = characterY2 - characterDeltaDiag;
                return diagonalCollisionTest(futureCharaX1, futureCharaX2, futureCharaY2);
            case SOUTHEAST:
                futureCharaX1 = characterX1 + characterDeltaDiag;
                futureCharaX2 = characterX2 + characterDeltaDiag;
                futureCharaY2 = characterY2 + characterDeltaDiag;
                return diagonalCollisionTest(futureCharaX1, futureCharaX2, futureCharaY2);
            default: return false;  //  can't collide if not moving
        }
    }

    public boolean diagonalCollisionTest(int futureCharaX1, int futureCharaX2, int futureCharaY2) {
        return
            ((futureCharaX2 >= hitbox.getX1()) &&     //  bottom right character corner test
            (futureCharaX2 <= hitbox.getX2()) &&
            (futureCharaY2 >= hitbox.getY1()) &&
            (futureCharaY2 <= hitbox.getY2()) ||
            (futureCharaX1 >= hitbox.getX1()) &&
            (futureCharaX1 <= hitbox.getX2()) &&        // bottom left character corner test
            (futureCharaY2 >= hitbox.getY1()) &&
            (futureCharaY2 <= hitbox.getY2()));
    }

    public boolean verticalCollisionTest(int futureCharaYi, int characterX1, int characterX2) {
        return
            ((characterX1 >= hitbox.getX1()) &&
            (characterX1 <= hitbox.getX2()) &&
            (futureCharaYi >= hitbox.getY1()) &&
            (futureCharaYi <= hitbox.getY2()) ||      // bottom left character corner test 
            (characterX2 >= hitbox.getX1()) &&
            (characterX2 <= hitbox.getX2()) &&
            (futureCharaYi >= hitbox.getY1()) &&
            (futureCharaYi <= hitbox.getY2()));      // bottom right character corner test
    }

    public boolean horizontalCollisionTest(int futureCharaXi, int characterY2) {
        return
            ((futureCharaXi >= hitbox.getX1()) &&
            (futureCharaXi <= hitbox.getX2()) &&     // bottom [direction] character corner test
            (characterY2 >= hitbox.getY1()) &&
            (characterY2 <= hitbox.getY2()));
    }

    @Override
    public void draw(Graphics g) {
        if (GlobalSettings.getInstance().isShowHitboxes()) {
            hitbox.setColor(Color.GREEN);
            hitbox.draw(g);
        }
    }

    public void updateHitbox() {
        hitbox.updatePosition(posX1, posY1);
    }
    public abstract int getY2();
    public abstract void initImages();
}