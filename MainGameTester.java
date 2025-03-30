public class MainGameTester {
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
        MainPanel gameTester = new MainPanel();
        gameTester.animate();
    }
}