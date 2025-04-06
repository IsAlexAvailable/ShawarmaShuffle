import java.awt.CardLayout;
import javax.swing.JFrame;

public class ShawarmaShuffle extends JFrame {
    private GamePanel gamePanel;
    private PausePanel pausePanel;
    private CardLayout cardLayout;
    private static boolean gamePaused;

    public ShawarmaShuffle() {
        gamePaused = true;
        gamePanel = new GamePanel(this);
        pausePanel = new PausePanel(this);
        initWindow();
    }

    /*
     *  sets default values for the JFrame
     *  operations include: adds this panel to the window, restricts window resizability,
     *  allows program to exit upon window close, packs the window to conform to its components properties,
     *  centers window on screen
     */

    public void initWindow() {
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);
        getContentPane().add(pausePanel, Constants.CARD_PAUSE_PANEL);
        getContentPane().add(gamePanel, Constants.CARD_MAIN_GAME);
        setLayout(cardLayout);
        
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); //  passing null centers window on screen
        setVisible(true);
    }
    
    /*
     *  implements the game loop. Determines and limits the program's execution to the period of a single frame cycle.
     *  If the cycle is completed before the end of a period, the program sleeps the remaining time. Every cycle, the game 
     *  calls update() and repaint() to update various object properties and paint them accordingly.
     */

     public void runGameLoop() {
        double repaintInterval = 1000000000/Constants.FPS; //  how many nanoseconds each frame takes at 24fps
        double nextInterval = System.nanoTime() + repaintInterval;  //  next interval occurs at curr time + interval
        double remainingTime;

        while (true) {
            try {
                remainingTime = nextInterval - System.nanoTime();
                remainingTime /= 1000000;    //  convert nano to milli
                Thread.sleep((long) remainingTime);    //  delay time
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!gamePaused) {
                gamePanel.update();  //  updates game objects
            }
            repaint(); //  updates visuals for curr screen
            nextInterval = System.nanoTime() + repaintInterval; //  update next interval
        }
    }

    public void resumeGame() {
        cardLayout.show(getContentPane(), Constants.CARD_MAIN_GAME);
        gamePanel.requestFocusInWindow();
        gamePaused = false;
    }

    public void pauseGame() {
        cardLayout.show(getContentPane(), Constants.CARD_PAUSE_PANEL);
        pausePanel.requestFocusInWindow();
        gamePaused = true;
    }

    public static void main(String[] args) {
        ShawarmaShuffle game = new ShawarmaShuffle();
        game.runGameLoop();
    }
}