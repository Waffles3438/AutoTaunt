package AutoTaunt.command;

import AutoTaunt.AutoTaunt;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static AutoTaunt.config.ModConfig.taunts;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in ExampleMod.java with `CommandManager.INSTANCE.registerCommand(new ExampleCommand());`
 *
 * @see Command
 * @see Main
 * @see AutoTaunt
 */
@Command(value = "taunts", description = "Add taunts")
public class AddTaunts {

    @Main
    private void main(){
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("list - see list of taunts"));
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("add - add a taunt"));
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("remove - remove a taunt"));
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("clear - removes all taunts"));
    }

    @SubCommand()
    private void add(@Greedy String s) {
        taunts.add(s);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Added: " + s));
        saveTaunts();

    }

    @SubCommand
    private void remove(@Greedy String s){
        if (taunts.contains(s)) {
            taunts.remove(s);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Removed " + s));
        } else {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Taunt not found"));
        }
        saveTaunts();

    }

    @SubCommand
    private void clear(){
        taunts.clear();
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Cleared list of taunts!"));
        saveTaunts();

    }

    @SubCommand()
    private void list(){
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("List of taunts: "));
        for (String taunt : taunts) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("- " + taunt));
        }
        saveTaunts();

    }

    @SubCommand
    private void defaultTaunts(){
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Added default taunts"));
        taunts.add("Your fucking dogshit");
        taunts.add("Stop fucking running");
        taunts.add("Ur so dogshit at the game");
        taunts.add("Just log off");
        taunts.add("Waste of space");
        taunts.add("Imagine being this bad");
        taunts.add("Olympic runners run less than you");
        taunts.add("Holy fuck just stop playing the game");
        taunts.add("Uninstall the game");
        taunts.add("So fucking bad");
        taunts.add("L");
        saveTaunts();

    }

    public void saveTaunts() {
        System.out.println("Added to txt");
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("taunts.txt")))) {
            // Clear the file
            writer.print("");
            // Write new taunts
            for (String taunt : taunts) {
                writer.println(taunt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}