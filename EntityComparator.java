import java.util.Comparator;

public class EntityComparator implements Comparator<Entity> {

    /*
     *  defines an order based on entity lower y-values (y3). Entities with smaller magnitude y3 values appear first
     */

    @Override
    public int compare(Entity e1, Entity e2) {
        return e1.getY3() - e2.getY3();
    }
    
}
