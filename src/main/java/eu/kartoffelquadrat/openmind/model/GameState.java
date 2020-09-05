package eu.kartoffelquadrat.openmind.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Use this object to transfer the information to be displayed in the window to
 * the GUI
 *
 * @author Maximilian Schiedermeier, 2012
 */
public class GameState implements GameStateModifySettingsInterface
{

    private List<CombinationResult> rows;
    private GameCharacteristic currentCharacteristic;
    //defines settings for the next game to be launched. (does not influence the current boards settings but is needed to remember the users changes on the settings) 
    private GameCharacteristic nextCharacteristic;
    private boolean nextCharacterisitcAvailable = false;

    /**
     * does not store any rows on creation
     *
     * @param characteristic
     */
    public GameState(GameCharacteristic characteristic)
    {
        this.currentCharacteristic = characteristic;

        //on creation there is no next Characterisitc. It is created in case the user changes thje settings during the game without immediately launching a new game.
        nextCharacteristic = null;
        rows = new LinkedList<CombinationResult>();
    }

    /**
     * additional rows can be added afterwards
     *
     * @param nextRow
     */
    public GameState addEntry(CombinationResult nextRow)
    {
        rows.add(nextRow);
        return this;
    }

    public GameCharacteristic getCharacteristic()
    {
        return currentCharacteristic;
    }

    @Override
    public void modifyNextGameCharacteristic(GameCharacteristic next)
    {
        //security check
        if (next == null)
        {
            throw new RuntimeException("Defying to store next GameCharacteristic - It is null");
        }

        //store next characterisitc and set flag (one way)
        if (!currentCharacteristic.equals(next))
        {
            nextCharacteristic = next;
            nextCharacterisitcAvailable = true;
        }
    }

    /**
     * return users characteristic in case he modified the current setting, use
     * current setting otherwise
     */
    public GameCharacteristic getNextCharacteristic()
    {
        return (nextCharacterisitcAvailable ? nextCharacteristic : currentCharacteristic);
    }

    public int getNumberOfRows()
    {
        return rows.size();
    }

    /**
     * a bit tricky. the arrays indexes do not directly match to the game
     * indexes. In fact the last game index is the first (#0), etc...
     *
     * @param index
     * @return
     */
    public CombinationResult getRowWithIndex(int index)
    {
        int arrayIndex = getCharacteristic().getLimit() - index - 1;
        return rows.get(arrayIndex);
    }

}
