package com.beyound.dsem.meta.GUI;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.mysql.DTO.Module;
import com.beyound.dsem.meta.mysql.MysqlService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceGUI implements Listener {
    private final Inventory inv;
    private final Material defaultBlock=Material.REDSTONE_BLOCK;
    private final String title = ChatColor.AQUA+"Devices";
    private final ChatColor defaultItemNameColor = ChatColor.RED;
    private final int defaultInvSize = 9;
    public DeviceGUI() {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, defaultInvSize, title);
    }

    public DeviceGUI(Player p) {
        inv = Bukkit.createInventory(p, defaultInvSize, title);
    }

    public void addGuiItem(final String name, final String... lore){
        inv.addItem(createGuiItem(defaultBlock, defaultItemNameColor+name, lore));
    }
    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);


        final ItemStack clickedItem = e.getCurrentItem();
        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        final Player p = (Player) e.getWhoClicked();
        List<String> lore=clickedItem.getItemMeta().getLore();

        String itemId="0";
        String systemId="unknown";
        for(String str:lore){
            if(str.contains("item id")){
                itemId= ChatColor.stripColor(str.split(":")[1].trim());
            }else if(str.contains("system id")){
                systemId= ChatColor.stripColor(str.split(":")[1].trim());
            }
        }
        // create module
        ModuleGUI temp = new ModuleGUI(p,systemId, itemId);
        System.out.println("moduleGUI create by "+systemId);
        // add module data
        List<Module> modules = MysqlService.getInstance().getModules(Integer.parseInt(itemId));
        List<String> data;
        for(Module m:modules){
            if(m.getType().equals("sensor")){
                data=new ArrayList<>();
                data.add("property : "+m.getProperty());
                if(!m.getUnitOfMeasure().equals("none"))data.add("UoM : " + m.getUnitOfMeasure());
                temp.addGuiItem(1,m.getModelName(),data.toArray(new String[0]));
            }else if(m.getType().equals("actuator")){
                data=new ArrayList<>();
                data.add("property : "+m.getProperty());
                if(!m.getUnitOfMeasure().equals("none"))data.add("UoM : " + m.getUnitOfMeasure());
                temp.addGuiItem(2,m.getModelName(),data.toArray(new String[0]));
            }
        }

        p.closeInventory();
        Meta.getInstance().getServer().getPluginManager().registerEvents(temp,Meta.getInstance());
        temp.openInventory(p);
        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    /*
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }*/
}
