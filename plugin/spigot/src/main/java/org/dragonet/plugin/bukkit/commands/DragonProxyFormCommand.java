/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.plugin.bukkit.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dragonet.common.gui.CustomFormComponent;
import org.dragonet.common.gui.DropDownComponent;
import org.dragonet.common.gui.InputComponent;
import org.dragonet.common.gui.LabelComponent;
import org.dragonet.plugin.bukkit.BedrockPlayer;
import org.dragonet.plugin.bukkit.DPAddonBukkit;

/**
 *
 * @author Epic
 */
public class DragonProxyFormCommand implements CommandExecutor {

    private final DPAddonBukkit plugin;

    public DragonProxyFormCommand(DPAddonBukkit plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("form")) {
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                if (this.plugin.isBedrockPlayer(player.getUniqueId())) {
                    CustomFormComponent form = new CustomFormComponent("This is a test form");
                    form.addComponent(new LabelComponent("Test label"));
                    form.addComponent(new InputComponent("Test input").setPlaceholder("placeholder"));
                    List<String> dropDown = new ArrayList();
                    dropDown.add("Choice 1");
                    dropDown.add("Choice 2");
                    dropDown.add("Choice 3");
                    form.addComponent(new DropDownComponent("Test dropdown", dropDown));
                    BedrockPlayer.getForPlayer(player).sendForm(0, form);
                }
                // do something
            }
            return true;
        }
        return false;
    }
}
