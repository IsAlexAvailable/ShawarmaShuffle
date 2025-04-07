package GameControl;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import Entities.Characters.Player;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PausePanel extends JPanel {
    private JButton playButton, exitButton;
    private BufferedImage playButtonImage, exitButtonImage, continueButtonImage;
    private ShawarmaShuffle game;

    private boolean isFirstScreen = true;

    public PausePanel(ShawarmaShuffle game) {
        this.game = game;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(Constants.SCREENWIDTH, Constants.SCREENHEIGHT));    //  sets this panel size
        getButtonImages();
        initPlayButton();
        initExitButton();
        this.add(playButton);   //  add buttons to this panel
        this.add(exitButton);
    }

    public void getButtonImages() {
        try {
            playButtonImage = ImageIO.read(getClass().getResourceAsStream("/sprites/play_button.png"));
            exitButtonImage = ImageIO.read(getClass().getResourceAsStream("/sprites/exit_button.png"));
            continueButtonImage = ImageIO.read(getClass().getResourceAsStream("/sprites/continue_button.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void initPlayButton() {
        playButton = new JButton();
        playButton.setIcon(new ImageIcon(playButtonImage));
        playButton.setSize(new Dimension(Constants.SCALEDTILESIZE*4, Constants.SCALEDTILESIZE));
        playButton.setAlignmentX(CENTER_ALIGNMENT);
        playButton.addActionListener(new ActionListener() {     // anonymous class, replacing the PlaybuttonListener inner class
            @Override
            public void actionPerformed(ActionEvent e) { 
                game.resumeGame();
                if (isFirstScreen) {    //  after starting game, subsequent pauses display "continue" instead of "play game"
                    playButton.setIcon(new ImageIcon(continueButtonImage));
                    isFirstScreen = false;
                }
            }
        });
    }

    public void initExitButton() {
        exitButton = new JButton();
        exitButton.setIcon(new ImageIcon(exitButtonImage));
        exitButton.setSize(new Dimension(Constants.SCALEDTILESIZE*4, Constants.SCALEDTILESIZE));
        exitButton.setAlignmentX(CENTER_ALIGNMENT);
        exitButton.addActionListener(new ActionListener() {     // anonymous class, replacing the ExitbuttonListener inner class
            @Override
            public void actionPerformed(ActionEvent e) { 
                String[] options = {"Yes", "No"};
                int choice = JOptionPane.showOptionDialog(
                    PausePanel.this, 
                    "Are you sure you want to quit?",
                    "Quit game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon(Player.getPlayerIdleImage()),
                    options,
                    options[1] 
                );
                if (choice == 0) { System.exit(0); }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        //  paints the pause screen
        super.paintComponent(g);
        g.drawRect(50, 50, 100, 100);   //  dummy test
    }
}