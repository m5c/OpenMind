/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.kartoffelquadrat.openmind.controller;

import eu.kartoffelquadrat.openmind.controller.CombinationTool.CombinationException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import eu.kartoffelquadrat.openmind.model.Combination;
import eu.kartoffelquadrat.openmind.model.CombinationResult;
import eu.kartoffelquadrat.openmind.model.GameCharacteristic;
import eu.kartoffelquadrat.openmind.model.GameState;
import eu.kartoffelquadrat.openmind.model.GameStateModifySettingsInterface;
import eu.kartoffelquadrat.openmind.model.UserCombination;
import eu.kartoffelquadrat.openmind.view.MainFrame;
import eu.kartoffelquadrat.openmind.view.Resources;

/**
 * Launcher class. Fires up the UI and creates a master combination for the default game parameters.
 * @author Maximilian Schiedermeier, 2012
 */
public class OpenMind
{
    //version

    final public static String version = "1.3";
    //about message
    final public static String aboutMessage = "OpenMind " + version + "\n\nFor Resa\nBy Maex\n\nOpenmind is a Kartoffelquadrat product and licensed under GNU/GPL\nFor more information / sources visit:\nhttps://github.com/kartoffelquadrat\n\nSpecial thanks to the testing team: Resa, Dad & Magnus.";
    //max parameters
    public static final int MAX_RANGE = 8;
    public static final int MAX_SIZE = 5;
    //maximum number of guesses
    public static int limit = 11;
    //initialized with default values
    private static int defaultRange = 8;
    private static int defaultSize = 4;
    private static boolean repetitions = true;
    //gui params
    public static final int contentWidth = 600;
    public static final int contentHeight = 800;
    //master combination - is dubbed on creation of new game
    private static Combination masterKey;
    //stores the information already submit by the user
    List<CombinationResult> gameHistory = new LinkedList<CombinationResult>();
    //private reference to GUI
    private static MainFrame mainFrame;
    //stores the settings for the current game
    private static GameCharacteristic defaultCharacteristics;
    //game state
    private static GameState state = null;
    //timer
    private static long startupTime;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CombinationException, UnsupportedLookAndFeelException
    {
        //load default settings
        defaultCharacteristics = new GameCharacteristic(defaultRange, defaultSize, repetitions, limit);

        //generate master key
        Combination masterCombination = CombinationTool.generateCombination(defaultCharacteristics);

        //intialize the GUI
        mainFrame = new MainFrame(defaultCharacteristics);
        mainFrame.getContentPane().setPreferredSize(new Dimension(contentWidth, contentHeight));
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        startGame();
    }

    public static void startGame() throws CombinationException
    {
        startupTime = System.currentTimeMillis();


        //in case this is the first game, use default settings, otherwise gather changes if existent
        GameCharacteristic settings;
        if (state == null)
        {
            //generate new settings
            settings = defaultCharacteristics;
        }
        else
        {
            settings = state.getNextCharacteristic();
        }

        boolean interventionRequired = false;
        try
        {
            //actually create the new game
            masterKey = CombinationTool.generateCombination(settings);
        }
        catch (CombinationException ex)
        {
            JOptionPane.showMessageDialog(null, "Not enough colours for the Böppels!\nSwitching to default...", "Nope!", JOptionPane.ERROR_MESSAGE);

            settings = defaultCharacteristics;
            masterKey = CombinationTool.generateCombination(settings);
            interventionRequired = true;
        }

        //generate a new GameObject applying to the settings
        state = new GameState(settings);

        //reload gui
        mainFrame.refresh(state, interventionRequired);
    }

    /**
     * receives a user combination, compares it to the master and updates the
     * GUI using special objects for the information transmission. This method
     * is called by the GUI's submit button.
     */
    public static void iterate(UserCombination latestGuess) throws CombinationException
    {
        //build CombinationResult Object
        //-> update gui
        CombinationResult result = new CombinationResult(masterKey, latestGuess, state.getCharacteristic());
        mainFrame.refresh(state.addEntry(result), false);

        //check if correct
        //-> show neat congrats message, relaunch game with recent params
        //-> restart game
        if (result.isMatching())
        {
            int seconds = (int) ((System.currentTimeMillis() - startupTime) / 1000);
            int minutes = seconds / 60;

            //in case the seconds count is less then 10 i need to put an addition 0 in fornt of the digit to make it look nicer.
            String additionalZero = seconds % 60 < 10 ? "0" : "";
            JOptionPane.showMessageDialog(null, "Time: " + minutes + ":" + additionalZero + seconds % 60, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
            startGame();
        }

        //check if full (arraysize == limit)
        //-> show neat epic fail message
        //-> restart game
        if (state.getNumberOfRows() == state.getCharacteristic().getLimit())
        {
            //launch game over popup (shows sollution)
            showFailPopup();

            //start next game afterwards
            startGame();
        }
    }

    /**
     * this getter allows the GUI elements of the TopBar to change the settings
     * for game sessions which are next to come.
     *
     * @return the GameState wrapped in an interface which minimizes
     * accessibility to the needed methods.
     */
    public static GameStateModifySettingsInterface getGameState()
    {
        return state;
    }

    private static void showFailPopup()
    {
        //check one pebbles measures
        int iconBounds = Resources.getResources().getPebbleImage(0).getHeight();
        int space = 5;

        //initialize array for the graphics
        Image[] masterIcons = new Image[masterKey.getSize()];
        for (int i = 0; i < masterKey.getSize(); i++)
        {
            int currentColourIndex = masterKey.getValueAtPosition(i);
            masterIcons[i] = Resources.getResources().getPebbleImage(currentColourIndex);
        }

        //prepare BufferedImage for all the Pebbles (+ 2 pixels for the rim around the sollution)
        int totalHeight = iconBounds + 2 * space + 2;
        //spaces fot the pebble + for the spaces + for the rim
        int totalWidth = iconBounds * masterIcons.length + (masterIcons.length + 1) * space + 2;
        BufferedImage sumBuffer = new BufferedImage(totalWidth, totalHeight, 8); //8 is for 8 bits
        Graphics g = sumBuffer.createGraphics();

        //add all pebbles to the bufferedimage
        int grayscale = 105;
        g.setColor(new Color(grayscale - 100, grayscale - 100, grayscale - 100));
        g.drawRect(0, 0, totalWidth, totalHeight);
        g.setColor(new Color(grayscale, grayscale, grayscale));
        g.fillRect(1, 1, totalWidth - 2, totalHeight - 2);
        for (int i = 0; i < masterIcons.length; i++)
        {
            int offsetX = space + i * (iconBounds + space) + 1;
            g.drawImage(masterIcons[i], offsetX, space + 1, null);
        }
        g.dispose();
        ImageIcon sumIcon = new ImageIcon(sumBuffer);
        JOptionPane.showMessageDialog(null, "Epic Fail", "Game Over!", JOptionPane.ERROR_MESSAGE, sumIcon);
    }

    /**
     * allows to safely terminate the application e.g. via the GUI
     */
    public static void quitOpenMind()
    {
        WindowEvent wev = new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

}
