package AutoTaunt.config;

import AutoTaunt.AutoTaunt;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class ModConfig extends Config {

    public static ArrayList<String> taunts = new ArrayList<>();
    @KeyBind(
            name = "AutoTaunt keybind",
            size = OptionSize.DUAL
    )
    public static OneKeyBind keyBind = new OneKeyBind(UKeyboard.KEY_L);
    @Dropdown(
            name = "Chat type",        // name of the component
            options = {"Default", "All chat", "Party chat", "Shout"},
            size = OptionSize.DUAL
    )
    public static int value = 0;        // default option (here "Another Option")

    public ModConfig() {
        super(new Mod(AutoTaunt.NAME, ModType.UTIL_QOL), AutoTaunt.MODID + ".json");
        initialize();
        loadTaunts();
        if(!taunts.isEmpty()){
            registerKeyBind(keyBind, this::test);
        }
    }

    private final Random rand = new Random();
    private int prev = -10;
    private String taunt(){
        int random = rand.nextInt(taunts.size());
        while(random == prev && taunts.size() != 1){
            random = rand.nextInt(taunts.size());
        }
        prev = random;

        if(value == 0){
            return taunts.get(random);
        } else if (value == 1){
            return "/ac " + taunts.get(random);
        } else if (value == 2){
            return "/pc " + taunts.get(random);
        } else{
            return "/shout " + taunts.get(random);
        }
    }

    public void loadTaunts() {
        ArrayList<String> loadedTaunts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("taunts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadedTaunts.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assign loaded taunts to the taunts list
        taunts.clear();
        taunts.addAll(loadedTaunts);
    }



    private void test(){
        if(!taunts.isEmpty()){
            UChat.say(taunt());
        }
    }
}

