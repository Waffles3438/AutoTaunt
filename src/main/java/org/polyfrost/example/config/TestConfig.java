package org.polyfrost.example.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import org.polyfrost.example.ExampleMod;

import java.util.Random;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class TestConfig extends Config {
    @KeyBind(
            name = "I'm a keybind!"
    )
// using OneKeyBind to set the default key combo to Shift+S
    public static OneKeyBind keyBind = new OneKeyBind(UKeyboard.KEY_LSHIFT, UKeyboard.KEY_S);

    public TestConfig() {
        super(new Mod(ExampleMod.NAME, ModType.UTIL_QOL), ExampleMod.MODID + ".json");
        initialize();
        registerKeyBind(keyBind, () -> UChat.say(Taunt()));
    }

    private int prev = -1;

    public String Taunt(){
        Random rand = new Random();
        int a = rand.nextInt(10);
        while(a == prev){
            a = rand.nextInt(10);
        }
        prev = a;
        if(a == 1){
            return "Your fucking dogshit";
        } else if (a == 2) {
            return "Stop fucking running";
        } else if (a == 3) {
            return "L";
        } else if (a == 4) {
            return "Ur so dogshit at the game";
        } else if (a == 5) {
            return "Just log off";
        } else if (a == 6) {
            return "Waste of space";
        } else if (a == 7) {
            return "Imagine being this bad";
        } else if (a == 8) {
            return "Olympic runners run less than you";
        } else if (a == 9) {
            return "Holy fuck just stop playing the game";
        } else {
            return "Uninstall the game";
        }
    }
}

