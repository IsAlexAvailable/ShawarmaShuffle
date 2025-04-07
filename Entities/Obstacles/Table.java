package Entities.Obstacles;

public abstract class Table extends Furniture {
    protected int posX2, posY2;

    @Override
    public int getY2() { return posY2; }
}   