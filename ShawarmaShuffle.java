public class ShawarmaShuffle {
    private MainPanel mainPanel;
    public static final int FPS = 24;
    private boolean gamePaused = true;

    public ShawarmaShuffle() {
        mainPanel = new MainPanel(this);
    }

    /*
     *  implements the game loop. Determines and limits the program's execution to the period of a single frame cycle.
     *  If the cycle is completed before the end of a period, the program sleeps the remaining time. Every cycle, the game 
     *  calls update() and repaint() to update various object properties and paint them accordingly.
     */

     public void gameLoop() {
        double repaintInterval = 1000000000/FPS; //  how many nanoseconds each frame takes at 24fps
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
                mainPanel.update();  //  updates objects
                mainPanel.repaint(); //  calls paintComponent and updates visuals
            }
            nextInterval = System.nanoTime() + repaintInterval; //  update next interval
        }
    }

    public void resumeGame() {
        mainPanel.showGamePanel();
        gamePaused = false;
    }

    public void pauseGame() {
        mainPanel.showPausePanel();
        gamePaused = true;
    }

    /*  
        Game summary:
        This program serves as the foundation for a top-down RPG. It implements various game mechanics such as 
        character animation, collisions, and item hotbar-inventory system, but note it has no main gameplay objectives yet.
        The game is built on a single JPanel which is attached to a JFrame of fixed size. This panel is used to paint various
        game components, i.e. the background, characters, obstacles, and items, which are updated after each game loop
        iteration. The user controls a player around an area, bound by the frame, using movement ('w','a','s','d') 
        and arrow keys, driving the main changes to the game at each update. The player may pickup items using 'e'
        and drop them using 'q'. Picking up an item places it at the first available slot of a player's hotbar, which
        has fixed size. The player can selectively drop items from different slots by using number keys. By pressing 'esc'
        the user enters a pause screen, temporarily halting in-game updates, and is given the options to continue playing or
        exit using JButtons. Lastly, the environment has pre-determined obstacles that prevent player movement through 
        collision checks, NPCs who randomly traverse the space, and player-interactable items.

        Technical fulfillments:
        JPanel, JButton, JOptionPane... 
        1. play and exit JButtons were implemented on the pause screen to allow the user to choose to continue playing or 
           quit at the click of a button
        2. JOptionPane was used to provide the user with a confirmation box dialog upon clicking exit
        3. 


     */    
    public static void main(String[] args) {
        ShawarmaShuffle tester = new ShawarmaShuffle();
        tester.gameLoop();
    }

}