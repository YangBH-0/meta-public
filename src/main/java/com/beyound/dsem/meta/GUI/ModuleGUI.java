package com.beyound.dsem.meta.GUI;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.entity.VirtualFactory;
import com.beyound.dsem.meta.registry.PlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ModuleGUI implements Listener {
    private final Inventory inv;
    private final Material[] defaultBlock = {Material.OBSERVER, Material.REDSTONE_LAMP, Material.PISTON};
    private final String title = ChatColor.AQUA + "Modules";
    private final ChatColor defaultItemNameColor = ChatColor.GREEN;
    private final int defaultInvSize = 9;
    private final String system_id;
    private final String item_id;

    public ModuleGUI(String system_id, String item_id) {
        this.system_id = system_id;
        this.item_id = item_id;
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, defaultInvSize, title);
    }

    public ModuleGUI(Player p, String system_id, String item_id) {
        this.system_id = system_id;
        this.item_id = item_id;
        inv = Bukkit.createInventory(p, defaultInvSize, title);
        p.sendMessage("item id : "+item_id);
    }

    public void addGuiItem(int option, final String name, final String... lore) {
        switch (option) {
            case (1) -> {
                inv.addItem(createGuiItem(defaultBlock[0], defaultItemNameColor + name, lore));
            }
            case (2) -> {
                inv.addItem(createGuiItem(defaultBlock[1], defaultItemNameColor + name, lore));
            }
            case (3) -> {
                inv.addItem(createGuiItem(defaultBlock[2], defaultItemNameColor + name, lore));
            }
            default -> {
                Meta.getInstance().getLogger().info("잘못된 형태의 아이템");
            }
        }

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

        if (clickedItem.getType() == Material.REDSTONE_LAMP) {
            String model = clickedItem.getItemMeta().getDisplayName();
            String property = clickedItem.getItemMeta().getLore().get(0);
            model=ChatColor.stripColor(model);
            property=ChatColor.stripColor(property);
            PlayerRegistry.addObject(p, VirtualFactory.getObject("actuator",system_id,Integer.parseInt(item_id),model,property));
            p.getInventory().addItem(clickedItem);
            p.sendMessage("now place actuator");
            p.closeInventory();
        }else if(clickedItem.getType() == Material.OBSERVER){
            System.out.println("setting system Id"+ system_id);
            String model = clickedItem.getItemMeta().getDisplayName();
            String property = clickedItem.getItemMeta().getLore().get(0);
            property=ChatColor.stripColor(property);
            System.out.println(property);
            property=property.split(":")[1].trim();
            System.out.println(property);
            model=ChatColor.stripColor(model);
            PlayerRegistry.addObject(p, VirtualFactory.getObject("sensor",system_id,Integer.parseInt(item_id),model,property));
            p.getInventory().addItem(clickedItem);
            p.sendMessage("now place Sensor");
            p.closeInventory();
        }
        else {
            List<String> lore = clickedItem.getItemMeta().getLore();
            String type = "none";
            for (String str : lore) {
                if (str.contains("type")) {
                    type = str.split(":")[1].trim();
                }
            }
            e.setCancelled(true);
        }


        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

}
