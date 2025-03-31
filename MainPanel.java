import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

//  TODO globally optimize draw positioning (simplify the math)
//  TODO figure out item spawns
//  TODO what does this game do ???

public class MainPanel extends JPanel {

    private static final String CARD_MAIN_GAME = "MainGame";
    private static final String CARD_PAUSE_PANEL = "PausePanel";

    // FRAME AND KEYLISTENER
    private JFrame window;

    //  CHARACTERS
    private Player player1 = new Player();
    private NPC npc1 = new NPC(50, 50, 3), npc2 = new NPC(450, 500, 2);
    private NPC npc3 = new NPC(100, 250, 1), npc4 = new NPC(830, 400, 1);

    //  OBSTACLES
    private GameBackground background = new GameBackground();
    private CircleTable table1 = new CircleTable(Constants.TRUETILESIZE*2, 450), table2 = new CircleTable(Constants.TRUETILESIZE*5, 450), table3 = new CircleTable(Constants.TRUETILESIZE*8, 450);
    private CircleTable table4 = new CircleTable(Constants.TRUETILESIZE*11, 450), table5 = new CircleTable(Constants.TRUETILESIZE*3, 550), table6 = new CircleTable(Constants.TRUETILESIZE*6, 550);
    private CircleTable table7 = new CircleTable(Constants.TRUETILESIZE*9, 550);
    private RectangleTable table8 = new RectangleTable(Constants.TRUETILESIZE*2, 350), table9 = new RectangleTable(Constants.TRUETILESIZE*5, 350), table10 = new RectangleTable(Constants.TRUETILESIZE*8, 350);
    private RectangleTable table11 = new RectangleTable(Constants.TRUETILESIZE*11, 350);
    private SinglePlanter plant1 = new SinglePlanter(Constants.TRUETILESIZE, Constants.TRUETILESIZE*8), plant2 = new SinglePlanter(Constants.TRUETILESIZE*11, Constants.TRUETILESIZE*8);
    private QuadPlanter plant4 = new QuadPlanter(Constants.TRUETILESIZE*9, Constants.TRUETILESIZE*3), plant3 = new QuadPlanter(Constants.TRUETILESIZE*5, Constants.TRUETILESIZE*3);

    //  COLLECTABLES
    private Banana banana1 = new Banana(50, 50), banana2 = new Banana(500, 500), banana3 = new Banana(100, 50), banana4 = new Banana(600, 600), banana5 = new Banana(400, 350), banana6 = new Banana(700, 700);
    private Shawarma shawarma1 = new Shawarma(300, 300), shawarma2 = new Shawarma(200, 300), shawarma3 = new Shawarma(100, 300), shawarma4 = new Shawarma(30, 40), shawarma5 = new Shawarma(70, 90);

    //  OBJECT COLLECTIONS
    protected static Collection<Entity> obstacles = new LinkedList<Entity>();
    protected static Collection<Collectable> collectables = new HashSet<Collectable>();
    private Collection<Character> characters = new LinkedList<Character>(); //  
    private List<Entity> entities = new LinkedList<Entity>(); //  used to handle drawing order

    //  BUTTONS AND THEIR LISTENERS
    private JButton infoButton = new JButton();
    private boolean shouldDrawInfoBox = false;
    private BufferedImage infoButtonImage;
    private BufferedImage controlsImage;
    private InfoButtonListener infoListener = new InfoButtonListener();
    private CardLayout cardLayout;
    private ShawarmaShuffle game;


    public MainPanel(ShawarmaShuffle game) {
        this.game = game;

        //  JPANEL CONFIGURATION
        this.setPreferredSize(new Dimension(Constants.SCREENWIDTH, Constants.SCREENHEIGHT));    //  sets this panel size
        this.setDoubleBuffered(true);   //  rendering optimization

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { 
                    game.pauseGame();
                }
            }
        });

        this.addKeyListener(player1.getKeyListener());
        this.addKeyListener(player1.getInventory().getKeyListener());

        this.setFocusable(true);    //  allows the main panel to be focusable
        this.setLayout(null);

        //  JBUTTON CONFIGURATION
        getButtonImages();  //  instantiate button images
        initDefaultButtons();

        this.add(infoButton);
        
        //  ADD OBJECTS TO COLLECTIONS
        initObstaclesCollection();
        initCollectablesCollection();
        initCharactersCollection();
        initEntitiesList();
        //  JFRAME CONFIGURATION
        setDefaultWindowValues();
    }

    /*
     *  an update cycle consists of checking whether the player has paused the game given by pause game status,
     *  in which case it calls a method to run the pause screen and skip regular game functions. Otherwise, each character
     *  and collectable is updated, which may include updating positions and directions, making collision checks, and handling collectable
     *  interactions.
     */

     public void update() {    //  update object positions
        for (Character c : characters) { c.update(); }
        for (Collectable c : collectables) { c.update(player1); }
    }
    
    /*
     *  sets default values for the JFrame
     *  operations include: adds this panel to the window, restricts window resizability,
     *  allows program to exit upon window close, packs the window to conform to its components properties,
     *  centers window on screen
     */

    public void setDefaultWindowValues() {

        //TODO: Move this into a separate class

        window = new JFrame("Shawarma Shuffle");  //  create game window
        
        cardLayout = new CardLayout();
        
        window.getContentPane().setLayout(cardLayout);
        window.getContentPane().add(new PausePanel(game), CARD_PAUSE_PANEL);
        window.getContentPane().add(this, CARD_MAIN_GAME);

        window.setLayout(cardLayout);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null); //  passing null centers window on screen
        window.setVisible(true);
    }

    public void showGamePanel() {
        cardLayout.show(window.getContentPane(), CARD_MAIN_GAME);
        this.requestFocusInWindow();    // IMPORTANT! Without this, the game will not have focus and keystrokes aren't detected
    }

    public void showPausePanel() {
        cardLayout.show(window.getContentPane(), CARD_PAUSE_PANEL);
    }

    /*
     *  sets default values for the play, exit, and info buttons
     *  operations include: provides image icon, adds action listeners so buttons may be clicked.
     *  Info button has a tool tip that displays a text prompt when hovering over button; it is
     *  also configured to remove the default button border and focus outline
     */

    public void initDefaultButtons() {
        infoButton.setIcon(new ImageIcon(infoButtonImage));
        infoButton.setLocation(Constants.TRUETILESIZE*15, 16);
        infoButton.setSize(new Dimension(Constants.TRUETILESIZE, Constants.TRUETILESIZE));
        infoButton.setToolTipText("Click to see controls");
        infoButton.setBorderPainted(false);
        infoButton.setFocusPainted(false);
        infoButton.addActionListener(infoListener);
    }

    /*
     *  adds 'obstacles' to the obstacles list. An obstacle is an object that prevents character movement, i.e. 
     *  tables, planters, background.
     */

    public void initObstaclesCollection() { //  adds all obstacles to 
        obstacles.add(background); obstacles.add(table1); obstacles.add(table2); obstacles.add(table3);
        obstacles.add(table4); obstacles.add(table5); obstacles.add(table6); obstacles.add(table7);
        obstacles.add(table8); obstacles.add(table9); obstacles.add(table10); obstacles.add(table11);
        obstacles.add(plant1); obstacles.add(plant2); obstacles.add(plant3); obstacles.add(plant4);
    }

    /*
     *  adds 'collectables' to the collectables set. A collectable is an object that a character may pick up/drop out
     *  of their inventory, i.e. banana and shawarma
     */

    public void initCollectablesCollection() {
        collectables.add(banana1); collectables.add(banana2); collectables.add(shawarma1); collectables.add(shawarma2);
        collectables.add(shawarma3); collectables.add(banana3); collectables.add(banana4); collectables.add(banana5);
        collectables.add(banana6); collectables.add(shawarma4); collectables.add(shawarma5);
    }

    /*
     *  adds 'characters' to characters list. A  character is a player/NPC
     */

    public void initCharactersCollection() {
        characters.add(npc1); characters.add(npc2); characters.add(npc3); characters.add(npc4); characters.add(player1);
    }

    /*
     *  adds 'entities' to entity list. An entity is an object that must be drawn in a specific order
     *  according to other entities' y-values (although "background" is an entity, its drawing order does not depend on other entities)
     */

    public void initEntitiesList() {
        for (Entity e : obstacles) { entities.add(e); }
        for (Collectable c : collectables) { entities.add(c); }
        for (Character c : characters) { entities.add(c); }
        entities.remove(background);
    }

    /*
     *  draws the pause screen or in-game screen depending on game pause status
     */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCurrLevel(g);     //  if game not paused then draw this level
        // g.dispose();    //  save memory // CAUSES BUTTON TO NOT DISPLAY????
    }

    /*
     *  paint order is prioritized so that objects that appear near the top of the screen are painted before 
     *  those that appear lower, which is accomplished by sorting entities based on an entity comparator that 
     *  compares object y-values. This allows objects to consistently either be in front of/behind other objects.
     *  Note a collectable is only drawn if it is not in the player's inventory. The background must be drawn first 
     *  as it is underneath all other drawn objects. Lastly, the player's hotbar is drawn.
     *  
     */

    public void drawCurrLevel(Graphics g) {
        Collections.sort(entities, new EntityComparator()); //  sort all entities to determine layering by y coord
        background.draw(g); //  draw background
        for (Entity e : entities) { 
            if (e instanceof Collectable) {
                Collectable c = (Collectable) e;
                if (!c.inInventory(player1)) { c.draw(g); }   //  only draw collectable if isn't in player inventory
            }
            else { e.draw(g); }
        }
        if (shouldDrawInfoBox) { drawInfoBox(g); }
        player1.getInventory().draw(g); //  draw player hotbar
    }

    /*
     *  draws the pause screen background
     */

    public void drawPauseScreen(Graphics g) {
        // TODO draw pause screen elements
    }

    public void drawInfoBox(Graphics g) {
        g.drawImage(controlsImage, Constants.TRUETILESIZE*2, 16, Constants.TRUETILESIZE*12, Constants.TRUETILESIZE, null);
    }

    /*
     *  initializes button images, importing them from sprites folder
     */

    public void getButtonImages() {
        try {
            infoButtonImage = ImageIO.read(getClass().getResourceAsStream("/sprites/info_button.png"));
            controlsImage = ImageIO.read(getClass().getResourceAsStream("/sprites/controls.png"));
        } catch (IOException e) {};
    }

 

    private class InfoButtonListener implements ActionListener {
        boolean isClickedOnce = false;
        /*
         *  when clicked, sets boolean to draw information box. When clicked twice
         *  it hides the info box. To prevent this button from stealing focus for too long
         *  from the main panel, the panel immediately requests for focus. Movement keys
         *  are momentarily disabled to prevent button press from cancelling movement
         *  key releases
         */

        @Override
        public void actionPerformed(ActionEvent e) {
            MainPanel.this.requestFocusInWindow();

            if (isClickedOnce) {isClickedOnce = false; shouldDrawInfoBox = false; }
            else if (!isClickedOnce) { isClickedOnce = true; shouldDrawInfoBox = true; }
            
            player1.stopMoving();
        }
    }
}