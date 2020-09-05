package eu.kartoffelquadrat.openmind.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import eu.kartoffelquadrat.openmind.model.GameCharacteristic;
import eu.kartoffelquadrat.openmind.model.GameState;

/**
 * Sets the main layout.
 *
 * @author Maximilian Schiedermeier, 2012
 */
public class MainFrame extends JFrame
{

    private Container topLayer;
    //backdrop for the paint experiment
    private Image img;
    //images for the actual board
    /*
     private BufferedImage[] pebbles;
     private BufferedImage[] whiteCounters;
     private BufferedImage[] redCounters;
     */
    //reference to the GUI element that may change
    protected MainPanel mainPanel;

    public MainFrame(GameCharacteristic characteristic)
    {
        //load backdrop -> move to resources class
        img = new ImageIcon(Resources.getResources().getBackDrop()).getImage();

        topLayer = getContentPane();        
        mainPanel = new MainPanel(characteristic);
        topLayer.add(mainPanel);
    }

    public void refresh(GameState currentGameState, boolean interventionRequired)
    {
        mainPanel.refresh(currentGameState, interventionRequired);
    }

    //inner class for the background and all the other gui stuff (is packed on this class)
    //backdrops dont work with frames, only with panels
    class MainPanel extends JPanel
    {

        private Image backdrop;
        private JPanel currentBoard;
        private JPanel topBar;
        private GameCharacteristic defaultCharacteristic;
        
        public MainPanel(GameCharacteristic defaultCharacteristic)
        {
            this.defaultCharacteristic = defaultCharacteristic;
            this.backdrop = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(new BorderLayout());
            topBar = new TopBar(defaultCharacteristic);
            add(topBar, BorderLayout.NORTH);
        }

        @Override
        public void paintComponent(Graphics g)
        {
            g.drawImage(backdrop, 0, 0, null);
        }

        /**
         * this method can be used to reload the board.
         */
        private void refresh(GameState currentGameState, boolean interventionRequired)
        {
            /**
             * The users settings were illegal, therfore the default settings
             * were restored. To avoid confusion the settings panel needs to be
             * adapted.
             */
            if (interventionRequired)
            {
                remove(topBar);
                topBar = new TopBar(defaultCharacteristic);
                add(topBar, BorderLayout.NORTH);
            }    
                
            /* It is necessary to remove
             * the old board, since the opaque elements otherwise turn darker to to
             * the overlay with the previous generations elements.
             */
            {
                if (currentBoard != null)
                {
                    remove(currentBoard);
                }
            }
            currentBoard = new BoardPanel(currentGameState);
            add(currentBoard, BorderLayout.CENTER);
            revalidate();
        }

    }
}
