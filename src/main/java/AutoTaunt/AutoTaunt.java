package AutoTaunt;

import AutoTaunt.command.AddTaunts;
import AutoTaunt.config.ModConfig;
import cc.polyfrost.oneconfig.events.event.InitializationEvent;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * The entrypoint of the Example Mod that initializes it.
 *
 * @see Mod
 * @see InitializationEvent
 */
@Mod(modid = AutoTaunt.MODID, name = AutoTaunt.NAME, version = AutoTaunt.VERSION)
public class AutoTaunt {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    @Mod.Instance(MODID)
    public static AutoTaunt INSTANCE; // Adds the instance of the mod, so we can access other variables.
    public static ModConfig config;


    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new ModConfig();
        CommandManager.INSTANCE.registerCommand(new AddTaunts());
        CommandManager.register(new AddTaunts());
    }

//    @SubscribeEvent
//    public void onClientTick(TickEvent.ClientTickEvent event){
//
//    }
}
