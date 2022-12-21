package com.beyound.dsem.meta.commands;

import com.beyound.dsem.meta.Meta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SampleCommand implements CommandExecutor {
    private final Meta plugin = Meta.getInstance();
    private final String prefix = plugin.prefix;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) { // only player
            Player player = (Player) sender;

            Inventory gui = Bukkit.createInventory(player, 9, ChatColor.AQUA+"Custom GUI");
            ItemStack suicide = new ItemStack(Material.TNT);
            ItemStack feed = new ItemStack(Material.BREAD);
            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);

            ItemMeta suicide_meta = suicide.getItemMeta();
            suicide_meta.setDisplayName(ChatColor.RED+"Suicide");
            ArrayList<String> suicide_lore = new ArrayList<>();
            suicide_lore.add(ChatColor.GOLD+"Kill yourself.");
            suicide.setItemMeta(suicide_meta);


            ItemMeta feed_meta = feed.getItemMeta();
            feed_meta.setDisplayName(ChatColor.DARK_GREEN+"feed");
            ArrayList<String> feed_lore = new ArrayList<>();
            feed_lore.add(ChatColor.GOLD+"Hunger no more.");
            feed.setItemMeta(feed_meta);


            ItemMeta sword_meta = sword.getItemMeta();
            sword_meta.setDisplayName(ChatColor.LIGHT_PURPLE+"sword");
            ArrayList<String> sword_lore = new ArrayList<>();
            sword_lore.add(ChatColor.GOLD+"Get a sword.");
            sword.setItemMeta(sword_meta);

            ItemStack[] menu_item = { suicide,feed,sword};
            gui.setContents(menu_item);
            player.openInventory(gui);

            return false;
        }

        return true;
    }
}