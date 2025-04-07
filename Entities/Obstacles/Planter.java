package Entities.Obstacles;

public abstract class Planter extends Furniture {
    protected int posX2, posY2;
    
    @Override
    public int getY2() { return posY2; }
}