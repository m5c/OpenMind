package eu.kartoffelquadrat.openmind.model;

/**
 *
 * @author Maximilian Schiedermeier, 2012
 */
public class Combination
{

    /**
     * this array stores the state of a combination. The length matches the
     * number of slots in the current game, the range (starting at 0) matches
     * the number of colours used in this game. A value of -1 can be used in
     * order to specify an yet not set value (grey in the GUI)
     */
    protected int[] combination;
    protected final int range;
    protected final int size;

    public Combination(int[] combination, int range, int size)
    {
        this.combination = combination;
        this.range = range;
        this.size = size;
    }

    /**
     * getter for the combination returns a deep copy, to avoid modifications
     * from outside
     *
     * @return the combination
     */
    public int[] getCombination()
    {
        return combination.clone();
    }

    public int getValueAtPosition(int position)
    {
        return combination[position];
    }

    public int getSize()
    {
        return combination.length;
    }

    /**
     * this method tells whether all positions in the combination array store a
     * valid number (0 to upper range)
     *
     * @return boolean if the Combination is ready to be compared
     */
    public boolean isReady()
    {
        for (int i = 0; i < combination.length; i++)
        {
            //check if value not within valid range
            if (combination[i] < -1 || combination[i] >= range)
            {
                throw new Error("Appart of all checks somehow an invalid value now is stored in the array. This is very bad!");
            }

            //if not a "-1" now -> throw error
            if (combination[i] == -1)
            {
                return false;
            }
        }

        //all values are fine, looks good
        return true;
    }

    public boolean isCompatibleTo(Combination other)
    {
        return ((size == other.size) && (range == other.range));
    }

    /**
     * returns an array telling you how often each colour occurs in this
     * combination.
     *
     * @return the number of instances per colour in an array
     */
    public int[] getCoulourCounter()
    {
        int[] colourCounter = new int[range];

        for (int i = 0; i < combination.length; i++)
        {
            //increase count for the occured colour
            colourCounter[combination[i]]++;
        }
        
        return colourCounter;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < combination.length - 1; i++)
        {
            sb.append(combination[i]);
            sb.append(", ");

        }
        sb.append(combination[combination.length - 1]);
        sb.append(")");

        return sb.toString();
    }

}
