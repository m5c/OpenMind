package eu.kartoffelquadrat.openmind.controller;

import java.util.LinkedList;
import java.util.List;
import eu.kartoffelquadrat.openmind.model.Combination;
import eu.kartoffelquadrat.openmind.model.GameCharacteristic;

/**
 * Creates a random master combination, based on the chosen game parameters.
 * @author Maximilian Schiedermeier, 2012
 */
public class CombinationTool
{

    /**
     * generates a combination without any restrictions.
     *
     * @param size
     * @param range
     * @param repetition
     * @return
     */
    public static Combination generateCombination(GameCharacteristic characteristic) throws CombinationException
    {
        int size = characteristic.getNumberOfPebbles();
        int range = characteristic.getNumberOfColours();
        boolean repetition = characteristic.isWithRepetitions();

        if (size > OpenMind.MAX_SIZE || range > OpenMind.MAX_RANGE)
        {
            throw new RuntimeException("Illegal parameter received on combination generation");
        }

        //create non repetitive combination if desired
        if (!repetition)
        {
            return new Combination(getSimpleCombination(range, size), range, size);
        }

        //otherwise create truely random combination
        int[] combination = new int[size];

        for (int i = 0; i < combination.length; i++)
        {
            combination[i] = getRandomNumber(range);
        }
        return new Combination(combination, range, size);
    }

    private static int getRandomNumber(int range)
    {
        return (int) (Math.random() * range);
    }

    /**
     * generates a combination with the restriction that each value may occur
     * only once.
     *
     * @param range
     * @param size
     * @return
     */
    private static int[] getSimpleCombination(int range, int size) throws CombinationException
    {
        if (range < size)
        {
            throw new CombinationException("Cant create list, not enough originals.");
        }

        List<Integer> allNumbers = new LinkedList<Integer>();

        //throw all in a list
        for (int i = 0; i < range; i++)
        {
            allNumbers.add(i);
        }

        //randomly grab (size) elements
        int[] combination = new int[size];

        for (int i = 0; i < combination.length; i++)
        {
            int randomPosition = getRandomNumber(allNumbers.size());
            combination[i] = allNumbers.remove(randomPosition);
        }

        return combination;
    }

    /**
     * tells the primary distance between two combinations (in case they are
     * ready to be compared)
     */
    public static int getPrimaryDistance(Combination master, Combination slave)
    {
        if (!master.isCompatibleTo(slave))
        {
            throw new RuntimeException("Compution cannot be performed on combinations based on differing parameters");
        }

        //just check the number of matching positions
        int matching = 0;

        for (int i = 0; i < master.getSize(); i++)
        {
            if (master.getValueAtPosition(i) == slave.getValueAtPosition(i))
            {
                matching++;
            }
        }

        return matching;
    }

    /**
     * tells the secondary distance between two combinations (in case they are
     * ready to be compared)
     */
    public static int getSecondaryDistance(Combination master, Combination slave)
    {
        if (!master.isCompatibleTo(slave))
        {
            throw new RuntimeException("Compution cannot be performed on combinations based on differing parameters");
        }

        //store counter for each colour
        int[] masterColours = master.getCoulourCounter();
        int[] slaveColours = slave.getCoulourCounter();

        int rawSecondary = 0;

        for (int i = 0; i < slaveColours.length; i++)
        {
            rawSecondary = rawSecondary + (Math.min(masterColours[i], slaveColours[i]));
        }

        int result = rawSecondary - getPrimaryDistance(master, slave);

        return result;
    }

    public static class CombinationException extends Exception
    {
        public CombinationException(String message)
        {
        }
    }
}
