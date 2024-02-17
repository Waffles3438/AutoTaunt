package AutoTaunt.hud;

import AutoTaunt.config.ModConfig;
import cc.polyfrost.oneconfig.hud.SingleTextHud;

/**
 * An example OneConfig HUD that is started in the config and displays text.
 *
 * @see ModConfig
 */
public class TestHud extends SingleTextHud {
    public TestHud() {
        super("Test", false);
    }

    @Override
    public String getText(boolean example) {
        return "I'm an example HUD";
    }
}
