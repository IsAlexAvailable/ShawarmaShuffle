package GameControl;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import Entities.Entity;
import Entities.EntityComparator;
import Entities.Characters.Character;
import Entities.Characters.NPC;
import Entities.Characters.Player;
import Entities.Items.Banana;
import Entities.Items.Collectable;
import Entities.Items.Shawarma;
import Entities.Obstacles.CircleTable;
import Entities.Obstacles.GameBackground;
import Entities.Obstacles.QuadPlanter;
import Entities.Obstacles.RectangleTable;
import Entities.Obstacles.SinglePlanter;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class GamePanel extends JPanel {
    //  TODO: refactor entity initalization

    //  CHARACTERS
    private Player player1 = new Player();
    private NPC npc1 = new NPC(50, 50, 70), npc2 = new NPC(450, 500, 90);
    private NPC npc3 = new NPC(100, 250, 80), npc4 = new NPC(830, 400, 60);

    //  OBSTACLES
    private GameBackground background = new GameBackground();
    private CircleTable table1 = new CircleTable(Constants.SCALEDTILESIZE*2, 450), table2 = new CircleTable(Constants.SCALEDTILESIZE*5, 450), table3 = new CircleTable(Constants.SCALEDTILESIZE*8, 450);
    private CircleTable table4 = new CircleTable(Constants.SCALEDTILESIZE*11, 450), table5 = new CircleTable(Constants.SCALEDTILESIZE*3, 550), table6 = new CircleTable(Constants.SCALEDTILESIZE*6, 550);
    private CircleTable table7 = new CircleTable(Constants.SCALEDTILESIZE*9, 550);
    private RectangleTable table8 = new RectangleTable(Constants.SCALEDTILESIZE*2, 350), table9 = new RectangleTable(Constants.SCALEDTILESIZE*5, 350), table10 = new RectangleTable(Constants.SCALEDTILESIZE*8, 350);
    private RectangleTable table11 = new RectangleTable(Constants.SCALEDTILESIZE*11, 350);
    private SinglePlanter plant1 = new SinglePlanter(Constants.SCALEDTILESIZE, Constants.SCALEDTILESIZE*8), plant2 = new SinglePlanter(Constants.SCALEDTILESIZE*11, Constants.SCALEDTILESIZE*8);
    private QuadPlanter plant4 = new QuadPlanter(Constants.SCALEDTILESIZE*9, Constants.SCALEDTILESIZE*3), plant3 = new QuadPlanter(Constants.SCALEDTILESIZE*5, Constants.SCALEDTILESIZE*3);

    //  COLLECTABLES
    private Banana banana1 = new Banana(50, 50), banana2 = new Banana(500, 500), banana3 = new Banana(100, 50), banana4 = new Banana(600, 600), banana5 = new Banana(400, 350), banana6 = new Banana(700, 700);
    private Shawarma shawarma1 = new Shawarma(300, 300), shawarma2 = new Shawarma(200, 300), shawarma3 = new Shawarma(100, 300), shawarma4 = new Shawarma(30, 40), shawarma5 = new Shawarma(70, 90);

    //  OBJECT COLLECTIONS
    protected static Collection<Entity> obstacles;
    protected static Collection<Collectable> collectables;
    private Collection<Character> characters;
    private List<Entity> entities; //  used to handle drawing order

    //  BUTTONS AND THEIR LISTENERS
    private JButton infoButton;
    private boolean shouldDrawInfoBox;
    private BufferedImage infoButtonImage;
    private BufferedImage controlsImage;
    private ShawarmaShuffle game;

    public GamePanel(ShawarmaShuffle mainGame) {
        game = mainGame;
        this.setPreferredSize(new Dimension(Constants.SCREENWIDTH, Constants.SCREENHEIGHT));    //  sets this panel size
        this.setDoubleBuffered(true);   //  rendering optimization
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { 
                    player1.setIdle();
                    game.pauseGame();
                }
            }
        });

        this.addKeyListener(player1.getKeyListener());
        this.addKeyListener(player1.getInventory().getKeyListener());
        this.setLayout(null);

        //  JBUTTON CONFIGURATION
        getButtonImages();  //  instantiate button images
        initInfoButton();
        this.add(infoButton);
        
        //  ADD OBJECTS TO COLLECTIONS
        initObstaclesCollection();
        initCollectablesCollection();
        initCharactersCollection();
        initEntitiesList();

        shouldDrawInfoBox = false;
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
     *  sets default values for the info button
     *  operations include: provides image icon, adds action listeners so buttons may be clicked.
     *  Info button has a tool tip that displays a text prompt when hovering over button; it is
     *  also configured to remove the default button border and focus outline
     */
    public void initInfoButton() {
        infoButton = new JButton();
        infoButton.setIcon(new ImageIcon(infoButtonImage));
        infoButton.setLocation(Constants.SCALEDTILESIZE*15, 16);
        infoButton.setSize(new Dimension(Constants.SCALEDTILESIZE, Constants.SCALEDTILESIZE));
        infoButton.setToolTipText("Click to see controls");
        infoButton.setBorderPainted(false);
        infoButton.setFocusPainted(false);
        infoButton.addActionListener(new ActionListener () {    // anonymous class, replacing the InfobuttonListener inner class
            boolean isClickedOnce = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setIdle();
                GamePanel.this.requestFocusInWindow();  //  immediately returns focus to the game panel
                if (isClickedOnce) {isClickedOnce = false; shouldDrawInfoBox = false; }
                else if (!isClickedOnce) { isClickedOnce = true; shouldDrawInfoBox = true; }
            }
        });
    }

    /*
     *  adds 'obstacles' to the obstacles list. An obstacle is an object that prevents character movement, i.e. 
     *  tables, planters, background.
     */
    public void initObstaclesCollection() { //  adds all obstacles to collection
        obstacles = new LinkedList<Entity>();
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
        collectables = new HashSet<Collectable>();
        collectables.add(banana1); collectables.add(banana2); collectables.add(shawarma1); collectables.add(shawarma2);
        collectables.add(shawarma3); collectables.add(banana3); collectables.add(banana4); collectables.add(banana5);
        collectables.add(banana6); collectables.add(shawarma4); collectables.add(shawarma5);
    }

    /*
     *  adds 'characters' to characters list. A  character is a player/NPC
     */
    public void initCharactersCollection() {
        characters = new LinkedList<Character>();
        characters.add(npc1); characters.add(npc2); characters.add(npc3); characters.add(npc4); characters.add(player1);
    }

    /*
     *  adds 'entities' to entity list. An entity is an object that must be drawn in a specific order
     *  according to other entities' y-values (although "background" is an entity, its drawing order does not depend on other entities)
     */
    public void initEntitiesList() {
        entities = new LinkedList<Entity>();
        for (Entity e : obstacles) { entities.add(e); }
        for (Collectable c : collectables) { entities.add(c); }
        for (Character c : characters) { entities.add(c); }
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

    public void drawInfoBox(Graphics g) {
        g.drawImage(controlsImage, Constants.SCALEDTILESIZE*2, 16, Constants.SCALEDTILESIZE*12, Constants.SCALEDTILESIZE, null);
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

    public static Collection<Entity> getObstacles() {
        return obstacles;
    }

    public static Collection<Collectable> getCollectables() {
        return collectables;
    }
}