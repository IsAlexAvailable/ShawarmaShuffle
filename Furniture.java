public abstract class Furniture extends Entity {
    protected int width, height;

    /*  How furniture coordinates are defined
     *  posX  = x1, posY  = y1
     *  posX2 = x2, posY2 = y2
     *  posX3 = x3, posY3 = y3
     *  posX4 = x4, posY4 = y4
     * 
     *  (x1, y1)  _______________ (x2, y2)
     *           |               |
     *           |               |
     *           |               |
     *  (x3, y3) |_______________| (x4, y4)
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

    public boolean hasCollision(Character c) {  //  TODO is this the best logic???
        int characterX1 = c.getX1() + tileSize/2, characterY1 = c.getY1();
        // int characterX2 = c.getX1() + tileSize;
        int characterY3 = characterY1 + tileSize - 8;
        MovementState characterDirection = c.getDirection();
        int characterDelta = c.getDelta();
        int characterDeltaDiag = c.getDeltaDiag();
        int obstacleX2 = getX2(), obstacleX1 = getX1(), obstacleY1 = getY1(), obstacleY3 = getY3();
        int futureCharaX1, futureCharaX2, futureCharaY, futureCharaY3;

        switch (characterDirection) {
            case LEFT:
                futureCharaX1 = characterX1 - characterDelta - 16;
                return 
                    ((futureCharaX1 <= obstacleX2) &&
                    (futureCharaX1 >= obstacleX1) &&       // bottom left character corner test
                    (characterY3 >= obstacleY1) &&
                    (characterY3 <= obstacleY3));
            case RIGHT:
                futureCharaX2 = characterX1 + characterDelta + 16;
                return
                    ((futureCharaX2 >= obstacleX1) &&     //  bottom right character corner test
                    (futureCharaX2 <= obstacleX2) &&
                    (characterY3 >= obstacleY1) &&
                    (characterY3 <= obstacleY3));
            case UP:
                futureCharaY = characterY3 - characterDelta;
                return
                    ((futureCharaY >= obstacleY1) &&
                    (futureCharaY <= obstacleY3) &&      //  bottom left character corner test
                    (characterX1 >= obstacleX1) &&
                    (characterX1 <= obstacleX2)) ||
                    ((characterX1 <= obstacleX2) &&
                    (characterX1 >= obstacleX1) &&       // bottom right character corner test
                    (futureCharaY <= obstacleY3) &&
                    (futureCharaY >= obstacleY1));
            case DOWN:
                futureCharaY3 = characterY3 + characterDelta;
                return
                    ((futureCharaY3 <= obstacleY3) &&
                    (futureCharaY3 >= obstacleY1) &&      // bottom left character corner test
                    (characterX1 >= obstacleX1) &&
                    (characterX1 <= obstacleX2)) || 
                    ((futureCharaY3 <= obstacleY3) &&
                    (futureCharaY3 >= obstacleY1) &&      // bottom right character corner test
                    (characterX1 >= obstacleX1) &&
                    (characterX1 <= obstacleX2));
            case NORTHWEST:
                futureCharaX1 = characterX1 - characterDeltaDiag;
                futureCharaY3 = characterY3 - characterDeltaDiag;
                return 
                    ((futureCharaX1 <= obstacleX2) &&
                    (futureCharaX1 >= obstacleX1) &&       // bottom left character corner test
                    (futureCharaY3 >= obstacleY1) &&
                    (futureCharaY3 <= obstacleY3));
            case SOUTHWEST:
                futureCharaX1 = characterX1 - characterDeltaDiag;
                futureCharaY3 = characterY3 + characterDeltaDiag;
                return 
                    ((futureCharaX1 <= obstacleX2) &&
                    (futureCharaX1 >= obstacleX1) &&       // bottom left character corner test
                    (futureCharaY3 >= obstacleY1) &&
                    (futureCharaY3 <= obstacleY3));
            case NORTHEAST:
                futureCharaX2 = characterX1 + characterDeltaDiag;
                futureCharaY3 = characterY3 - characterDeltaDiag;
                return
                    ((futureCharaX2 >= obstacleX1) &&     //  bottom right character corner test
                    (futureCharaX2 <= obstacleX2) &&
                    (futureCharaY3 >= obstacleY1) &&
                    (futureCharaY3 <= obstacleY3));
            case SOUTHEAST:
                futureCharaX2 = characterX1 + characterDeltaDiag;
                futureCharaY3 = characterY3 + characterDeltaDiag;
                return
                    ((futureCharaX2 >= obstacleX1) &&     //  bottom right character corner test
                    (futureCharaX2 <= obstacleX2) &&
                    (futureCharaY3 >= obstacleY1) &&
                    (futureCharaY3 <= obstacleY3));
            default: return false;
        }
    }

    public abstract int getX2();
    public abstract int getY3();
    public abstract void initImages();
}