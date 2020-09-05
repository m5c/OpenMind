package eu.kartoffelquadrat.openmind.model;

/**
 * Stored the game parameters (amount of pebbles, colours, repetitions activated or not)
 * @author Maximilian Schiedermeier, 2012
 */
public class GameCharacteristic implements Comparable<GameCharacteristic>
{

    private int numberOfColours;
    private int numberOfPebbles;
    private boolean withRepetitions;
    private int limit;

    public GameCharacteristic(int numberOfColours, int numberOfPebbles, boolean withRepetitions, int limit)
    {
        this.numberOfColours = numberOfColours;
        this.numberOfPebbles = numberOfPebbles;
        this.withRepetitions = withRepetitions;
        this.limit = limit;
    }

    public int getLimit()
    {
        return limit;
    }

    public int getNumberOfColours()
    {
        return numberOfColours;
    }

    public int getNumberOfPebbles()
    {
        return numberOfPebbles;
    }

    public boolean isWithRepetitions()
    {
        return withRepetitions;
    }

    @Override
    public int compareTo(GameCharacteristic other)
    {
        if(other == null)
        {
            throw new NullPointerException();
        }
        if (
                (other.getNumberOfColours()==this.getNumberOfColours()) && 
                (other.getNumberOfPebbles()==this.getNumberOfPebbles()) && 
                (other.isWithRepetitions()==this.isWithRepetitions()) && 
                (other.getLimit()==this.getLimit()))
        {
            //they are equal
            return 0;
        }
        else
        {
            //the are different
            return 42;
        }
    }

}
