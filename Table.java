public abstract class Table extends Furniture {
    protected int posX2, posY3;

    @Override
    public int getX1() { return posX1; }
    @Override
    public int getY1() { return posY1; }
    @Override
    public int getX2() { return posX2; }
    @Override
    public int getY3() { return posY3; }
}