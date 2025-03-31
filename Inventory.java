import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashSet;

public class Inventory implements Drawable {
    private HashSet<Collectable> itemSet = new HashSet<Collectable>(10);    //  used to test if inventory full
    private Collectable[] itemArr = new Collectable[10];

    private Character character;
    private BufferedImage emptySlot, highlightedEmptySlot, bananaSlot, highlightedBananaSlot, shawarmaSlot, highlightedShawarmaSlot;
    private int activeSlotIndex = 0;

    public Inventory(Character c) {
        character = c;
        initImages();
        for (int i = 0; i < 10; i++) {
            itemArr[i] = null;
        }
    }

    /*
     *  searches the inventory for the specified collectable by calling set contains method
     *  @param  c   the collectable teseted against the inventory set
     *  @return if the collectable is in the inventory
     */

    public boolean containsItem(Collectable c) { return itemSet.contains(c); } 

    /*
     *  adds a collectable to the inventory if it doesn't already contain it and the inventory isn't full.
     *  Adding to the inventory means adding to its set (used to test containment) and its array (used to hold the object).
     *  Adds to the first available empty slot, which is occupied by null element. It updates the collectable's inInventory boolean
     *  so if the character tries to add the same item (since an added item maintains the same reachable position), the attempt is ignored.
     *  @param  c   the added collectable
     *  @return if the item was added
     */

    public boolean addItem(Collectable c) {
        if (!containsItem(c) && itemSet.size() < 10) {   //  only add item to inventory if not duplicate and inventory not full
            itemSet.add(c);
            for (int i = 0; i < itemArr.length; i++) {  //  find first available empty slot
                if (itemArr[i] == null) { itemArr[i] = c; break; }
            }
            c.setInInventory(true);
            return true;    //  item was added
        }
        else {
            return false;   //  item was not added
        }
    }

    /*
     *  removes a collectable from the inventory given by the provided index position corresponding to a hotbar slot.
     *  Note this method bars an empty slot from dropping an item (as it is null). It also 
     *  updates the collectable's position to reflect the character's position at the time of removal and sets
     *  the collectable's inInventory boolean to allow it to be picked up again.
     *  @param  index   
     *  @return if an item was dropped
     */

    public boolean dropActiveItem() {
        if (itemArr[activeSlotIndex] == null) { return false; }
        Collectable droppedItem = itemArr[activeSlotIndex];
        itemSet.remove(droppedItem);
        itemArr[activeSlotIndex] = null;
        droppedItem.setInInventory(false);
        droppedItem.updatePosition(character.getX1(), character.getY1());
        return true;
    }

    /*
     *  draws hotbar slots corresponding to the inventory. Iterates through length of the item array, checking which
     *  item, or lack thereof, to draw, then calls a method which determines the specific image to draw based on
     *  the hotbar's selection status
     */

    @Override
    public void draw(Graphics g) {
        
        for (int i = 0; i < itemArr.length; i++) {
            if (itemArr[i] instanceof Banana) { drawHotbarSlot(bananaSlot, highlightedBananaSlot, i, g); } 
            else if (itemArr[i] instanceof Shawarma) { drawHotbarSlot(shawarmaSlot, highlightedShawarmaSlot, i, g); }
            else {  drawHotbarSlot(emptySlot, highlightedEmptySlot, i, g); }
        }
    }

    /*
     *  draws the correct hotbar slot at the right tile position in sequence depending on the item images passed in
     *  and which hotbar slot is selected.
     *  @param  slot    
     *  @param  highlightedSlot 
     *  @param  index
     */

    public void drawHotbarSlot(BufferedImage slot, BufferedImage highlightedSlot, int index, Graphics g) {
        int tileSize = Constants.TRUETILESIZE;
        int hotbarY = Constants.SCREENHEIGHT-tileSize*2+32, hotbarX = tileSize*3;
        if (activeSlotIndex == index) { g.drawImage(highlightedSlot, hotbarX+tileSize*index, hotbarY, tileSize, tileSize, null); }
        else { g.drawImage(slot, hotbarX+tileSize*index, hotbarY, tileSize, tileSize, null); }
    }

    /*
     *  initializes Inventory slot images, importing from sprites folder
     */

    public void initImages() {
        try {
            emptySlot = ImageIO.read(getClass().getResourceAsStream("/sprites/empty_slot_t.png"));
            highlightedEmptySlot = ImageIO.read(getClass().getResourceAsStream("/sprites/select_empty_slot_t.png"));
            bananaSlot = ImageIO.read(getClass().getResourceAsStream("/sprites/banana_slot_t.png"));
            highlightedBananaSlot = ImageIO.read(getClass().getResourceAsStream("/sprites/select_banana_slot_t.png"));
            shawarmaSlot = ImageIO.read(getClass().getResourceAsStream("/sprites/shawarma_slot_t.png"));
            highlightedShawarmaSlot = ImageIO.read(getClass().getResourceAsStream("/sprites/select_shawarma_slot_t.png"));
        } catch (IOException e) {}
    }

    public void setActiveSlotIndex(int newActiveSlotIndex) {
        activeSlotIndex = newActiveSlotIndex;
    }

    KeyListener keyListener = new InventoryKeyListener();
    public KeyListener getKeyListener() {
        return keyListener;
    }

    class InventoryKeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            switch (e.getKeyChar()) {  //  records typed numbers so hotbar selection can be updated accordingly 
                case '1': setActiveSlotIndex(0); break;
                case '2': setActiveSlotIndex(1); break;
                case '3': setActiveSlotIndex(2); break;
                case '4': setActiveSlotIndex(3); break;
                case '5': setActiveSlotIndex(4); break;
                case '6': setActiveSlotIndex(5); break;
                case '7': setActiveSlotIndex(6); break;
                case '8': setActiveSlotIndex(7); break;
                case '9': setActiveSlotIndex(8); break;
                case '0': setActiveSlotIndex(9); break;
                default:  break; //  non-num key typed 
            }
        }
    }
}