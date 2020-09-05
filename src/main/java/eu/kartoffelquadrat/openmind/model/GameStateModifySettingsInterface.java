package eu.kartoffelquadrat.openmind.model;

/**
 *
 * @author Maximilian Schiedermeier, 2012
 */
public interface GameStateModifySettingsInterface
{

    /**
     * allows to modify the settings for the next game (nate: this will not
     * influence the current games settings). Don't hesitate to send settings
     * which might be identical to the current game settings. The implementation
     * is smart enough to store your option only in case it truly is wraps new
     * settings.
     *
     * @param next for the new settings
     */
    public void modifyNextGameCharacteristic(GameCharacteristic next);

}
