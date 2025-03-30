import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyTracker implements KeyListener {
    private int keyCode;
    private char keyChar;
    protected boolean leftPressed, rightPressed, upPressed, downPressed, shiftPressed, pauseGame = true;
    protected boolean eTyped, qTyped;
    protected int typedNum;
    
    /*
     *  primarily sets booleans corresponding to player movement key actions (wasd and arrows) as well as
     *  shift and escape which correspond to crouching and pausing the game
     */

    @Override
    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();
        keyChar = e.getKeyChar();

        if (keyCode == KeyEvent.VK_LEFT || keyChar == 'a') { leftPressed = true; }
        if (keyCode == KeyEvent.VK_RIGHT || keyChar == 'd') { rightPressed = true; }
        if (keyCode == KeyEvent.VK_UP || keyChar == 'w') { upPressed = true; }
        if (keyCode == KeyEvent.VK_DOWN || keyChar == 's') { downPressed = true; }
        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
            leftPressed = false;
            rightPressed = false;
            upPressed = false;
            downPressed = false;
        }
        if (keyCode == KeyEvent.VK_ESCAPE) { pauseGame = true; }
    }

    /*
     *  performs the opposite operation as keyPressed method
     */

    @Override
    public void keyReleased(KeyEvent e) {
        keyCode = e.getKeyCode();
        keyChar = e.getKeyChar();

        if (keyCode == KeyEvent.VK_LEFT || keyChar == 'a') { leftPressed = false; }
        if (keyCode == KeyEvent.VK_RIGHT || keyChar == 'd') { rightPressed = false; }
        if (keyCode == KeyEvent.VK_UP || keyChar == 'w') { upPressed = false; }
        if (keyCode == KeyEvent.VK_DOWN || keyChar == 's') { downPressed = false; }
        if (keyCode == KeyEvent.VK_SHIFT) { shiftPressed = false; }
        if (keyChar == 'e') { eTyped = false; }
        if (keyChar == 'q') { qTyped = false; }
    }

    /*
     *  calls methods to update typed key booleans corresponding to item pickup/drop and hotbar slot selection
     */

    @Override
    public void keyTyped(KeyEvent e) {
        keyChar = e.getKeyChar();
        updateTypedCollectableInteraction();
        updateTypedNum();
    }

    public void updateTypedCollectableInteraction() {   //  records typed keys that map to collectable pickups/drops
        switch (keyChar) {
            case 'e': eTyped = true; break;
            case 'q': qTyped = true; break;
        }
    }

    /*
     *  sets typed number keys (case values are unicode number constants)
     */

    public void updateTypedNum() {
        switch (keyChar) {  //  records typed numbers so hotbar selection can be updated accordingly 
            case 49: typedNum = 0; break;  //  unicode 1
            case 50: typedNum = 1; break;
            case 51: typedNum = 2; break;
            case 52: typedNum = 3; break;
            case 53: typedNum = 4; break;
            case 54: typedNum = 5; break;
            case 55: typedNum = 6; break;
            case 56: typedNum = 7; break;
            case 57: typedNum = 8; break;  //  unicode 9
            case 48: typedNum = 9; break;  //  unicode 0
            default:  break; //  non-num key typed 
        }
    }
}