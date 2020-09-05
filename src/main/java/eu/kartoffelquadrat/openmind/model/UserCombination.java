package eu.kartoffelquadrat.openmind.model;

/**
 * Bean that represents a color code entered by the user.
 * @author Maximilian Schiedermeier, 2012
 */
public class UserCombination extends Combination
{
    //setting this flag to true will make future changes impossible,
    //trying to do so will result in an exception
    private boolean freeze = false;

    /**
     * Crates a combination, consisting of grey (unset) values (-1), only.
     *
     * @param combination
     */
    public UserCombination(int size, int range)
    {
        super(getPreset(size), range, size);
    }

    /**
     * static helper method to workaround the need of the super call as
     * first command in this sub-classes constructor.
     */
    private static int[] getPreset(int size)
    {
        int[] preset = new int[size];
        for (int i = 0; i < preset.length; i++)
        {
            preset[i] = -1;
        }
        return preset;
    }

    /**
     * Calling this method will toggle a boolean which prevents any future
     * changes to this object. This cannot be undone.
     *
     */
    public void freeze()
    {
        freeze = true;
    }

    public void changePebble(int position, int nextValue)
    {
        //check if params are fine
        if (position >= super.size || position < 0)
        {
            throw new RuntimeException("Cant change value at position out of range");
        }
        if (nextValue < -1 || nextValue >= super.range)
        {
            throw new RuntimeException("Can't change to value out of valid range");
        }

        //check if modifiable
        if (freeze)
        {
            throw new RuntimeException("Can't modify Combination - already frozen");
        }

        //actually modify
        combination[position] = nextValue;
    }
}
