public abstract class Entity implements Drawable {
    protected int tileSize = Constants.TRUETILESIZE, screenWidth = Constants.SCREENWIDTH, screenHeight = Constants.SCREENHEIGHT;
    protected int posX1, posY1;

    public abstract boolean hasCollision(Character c);
    public abstract void initImages();
    public abstract int getX1();
    public abstract int getY1();
    public abstract int getY3();
}