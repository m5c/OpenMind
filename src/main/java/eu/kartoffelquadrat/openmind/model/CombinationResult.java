package eu.kartoffelquadrat.openmind.model;

import eu.kartoffelquadrat.openmind.controller.CombinationTool;

/**
 * This class stores a combination plus two ints for primary and secondary
 * distance. Use this class to distribute information from the controller to the
 * gui. The settings is needed to tell the gui what the layout shall look like
 *
 * @author Maximilian Schiedermeier, 2012
 */
public class CombinationResult
{

    private Combination userCombination;
    private int primaryDistance;
    private int secondaryDistance;
    private GameCharacteristic settings;
    private boolean matching;

    public CombinationResult(Combination masterCombination, Combination userCombination, GameCharacteristic settings)
    {
        this.settings = settings;
        this.userCombination = userCombination;
        primaryDistance = CombinationTool.getPrimaryDistance(masterCombination, userCombination);
        secondaryDistance = CombinationTool.getSecondaryDistance(masterCombination, userCombination);
        matching = (primaryDistance == settings.getNumberOfPebbles());
    }

    public Combination getUserCombination()
    {
        return userCombination;
    }

    public int getPrimaryDistance()
    {
        return primaryDistance;
    }

    public int getSecondaryDistance()
    {
        return secondaryDistance;
    }

    public boolean isMatching()
    {
        return matching;
    }

}
