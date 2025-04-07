package Entities;
import Entities.Characters.Character;
import GameControl.Constants;

public abstract class Entity implements Drawable {
    protected int tileSize = Constants.SCALEDTILESIZE, screenWidth = Constants.SCREENWIDTH, screenHeight = Constants.SCREENHEIGHT;
    protected int posX1, posY1;

    public abstract boolean hasCollision(Character c);
    public abstract void initImages();
    public abstract int getY2();
}