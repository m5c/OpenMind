package eu.kartoffelquadrat.openmind.view;

import eu.kartoffelquadrat.openmind.controller.CombinationTool.CombinationException;
import eu.kartoffelquadrat.openmind.controller.OpenMind;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import eu.kartoffelquadrat.openmind.model.GameState;
import eu.kartoffelquadrat.openmind.model.UserCombination;

/**
 * Draws the main UI
 *
 * @author Maximilian Schiedermeier, 2012
 */
public class BoardPanel extends JPanel
{
    private int pebbleBoardWidth;
    private int pebbleBoardHeight;
    private PebbleField pebbleField;

    private GameState state;
    private TopBar topBar; //needed to gather the setting (done by submit button)
    
    //variables for the current line
    private UserCombination currentLine;
    private PebbleImage[] currentImages;

    public BoardPanel(GameState state)
    {

        //assigns
        this.state = state;
        //this.pebbleImages = pebbleImages;
        //this.whiteCounterImages = whiteCounterImages;
        //this.redCounterImages = redCounterImages;

        //prepare current (empty) line
        currentLine = new UserCombination(state.getCharacteristic().getNumberOfPebbles(), state.getCharacteristic().getNumberOfColours());
        currentImages = new PebbleImage[state.getCharacteristic().getNumberOfPebbles()];

        //set transparency options
        setOpaque(false);
        setLayout(new BorderLayout());

        //create empty gaps, to shrink pebbleboard
        int rimWidth = getRimWidth(OpenMind.contentWidth, Resources.getResources().getBlankRedBoxImage().getWidth(), Resources.getResources().getBlankPebbleImage().getWidth(), state.getCharacteristic().getNumberOfPebbles());
        add(Box.createRigidArea(new Dimension(rimWidth, 1)), BorderLayout.WEST);
        add(Box.createRigidArea(new Dimension(rimWidth, 1)), BorderLayout.EAST);
        pebbleField = new PebbleField();
        add(pebbleField, BorderLayout.CENTER);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitListener());
        submitButton.setBackground(Color.gray);
        add(submitButton, BorderLayout.SOUTH);
    }

    //private method to compute the rim size
    private int getRimWidth(int contentWidth, int counterWidth, int pebbleWidth, int numberOfPebbles)
    {
        int rimSpace = 10;
        //pebbles + spaces between pebbles
        int neededPebbleSpace = (numberOfPebbles * pebbleWidth) + ((numberOfPebbles - 1) * rimSpace);
        int neededCouterSpace = (2 * counterWidth) + 2 * rimSpace;
        int rimWidth = ((contentWidth - neededPebbleSpace - neededCouterSpace) / 2);
        return rimWidth;
    }

    //inner class for one boxlayout line of pebbles
    class PebbleField extends JPanel
    {

        /**
         * set gray for all which are still to come. Set editable for all which
         * are currently in use. Use linked-list for all which already exist.
         */
        PebbleField()
        {

            removeAll();
            setOpaque(false);
            //+2 for the red and the white box
            setLayout(new GridLayout(0, state.getCharacteristic().getNumberOfPebbles() + 2));

            for (int index = 0; index < state.getCharacteristic().getLimit(); index++)
            {

                int activeLine = state.getCharacteristic().getLimit() - state.getNumberOfRows() - 1;
                boolean active = (index == activeLine);
                boolean colourized = index > activeLine;

                //get redbox, whitebox
                JLabel redBox;
                JLabel whiteBox;
                if (colourized)
                {
                    redBox = new RedBox(state.getRowWithIndex(index).getPrimaryDistance());
                    whiteBox = new WhiteBox(state.getRowWithIndex(index).getSecondaryDistance());
                }
                else
                {
                    redBox = new RedBox(6);
                    whiteBox = new WhiteBox(6);
                }

                //draw redbox
                add(redBox);

                //draw a line
                for (int linePosition = 0; linePosition < state.getCharacteristic().getNumberOfPebbles(); linePosition++)
                {
                    if (!colourized)
                    {
                        //a  grey and muted line
                        PebbleImage currentPebble = new PebbleImage(8);
                        add(currentPebble);

                        //a grey and acive line
                        if (active)
                        {
                            currentImages[linePosition] = currentPebble;
                            currentPebble.addMouseWheelListener(new PebbleScoller(linePosition));
                        }
                    }
                    //a colourized and muted line
                    else
                    {
                        int colour = state.getRowWithIndex(index).getUserCombination().getValueAtPosition(linePosition);
                        PebbleImage currentPebble = new PebbleImage(convertPebbleCodeToColour(colour));
                        add(currentPebble);
                    }    
                }

                //draw whitebox
                add(whiteBox);
            }
        }

    }

    /**
     * inner class for the pebble fields - has a listener capability for the
     * mouse wheel.
     */
    private class PebbleImage extends JLabel
    {

        public PebbleImage(int colour)
        {
            super(new ImageIcon(Resources.getResources().getPebbleImage(colour)));
        }

        public void changeImage(int nextIndex)
        {
            super.setIcon(new ImageIcon(Resources.getResources().getPebbleImage(nextIndex)));
        }

    }

    private class RedBox extends JLabel
    {

        public RedBox(int count)
        {
            super(new ImageIcon(Resources.getResources().getRedBoxImage(count)));
        }

    }

    private class WhiteBox extends JLabel
    {

        public WhiteBox(int count)
        {
            super(new ImageIcon(Resources.getResources().getWhiteBoxImage(count)));
        }

    }

    /**
     * listener class for the Pebbles
     */
    private class PebbleScoller implements MouseWheelListener
    {

        private int index;

        /**
         * index for the position in the current active line
         *
         * @param index
         */
        public PebbleScoller(int index)
        {
            this.index = index;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent mwe)
        {
            //down -> change to next colour
            if (mwe.getWheelRotation() > 0)
            {
                int nextColourIndex = getNextIndex(currentLine.getValueAtPosition(index));
                currentImages[index].changeImage(nextColourIndex);
                currentLine.changePebble(index, nextColourIndex);
            }
            //up -> change to previous colour
            else
            {
                int previousColourIndex = getPreviousIndex(currentLine.getValueAtPosition(index));
                currentImages[index].changeImage(previousColourIndex);
                currentLine.changePebble(index, previousColourIndex);
            }
        }

    }

    private int getNextIndex(int currentIndex)
    {
        return (currentIndex + 1) % state.getCharacteristic().getNumberOfColours();
    }

    private int getPreviousIndex(int currentIndex)
    {
        return ((currentIndex - 1) + state.getCharacteristic().getNumberOfColours()) % state.getCharacteristic().getNumberOfColours();
    }

    private int convertPebbleCodeToColour(int count)
    {
        int numberOfColours = state.getCharacteristic().getNumberOfColours();
        if (count == -1)
        {
            return numberOfColours;
        }
        return count;
    }
    
    /**
     * listener class for the submit button
     */
        private class SubmitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            //if valid -> freeze current usercombination, send to controller
            //igonre otherwise
            if(currentLine.isReady())
            {   
                //set current line to unmodifiable and launch next round
                currentLine.freeze();
                try
                {
                    OpenMind.iterate(currentLine);
                }
                catch (CombinationException ex)
                {
                    throw new RuntimeException("Invalid settings occurred although test did detect them.");
                }
            }

        }
    }

}
