package com.beyound.dsem.meta.GUI;

import com.beyound.dsem.meta.mqtt.ClientAgent;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Arrays;
import java.util.List;

public class ActuatorControlGUI implements Listener {
    private final Inventory inventory;
    private final Material defaultItem=Material.OAK_SIGN;
    private final String title= ChatColor.GREEN+ "Actuator Control Panel";
    private final int defaultSize = InventoryType.CHEST.getDefaultSize();  // 0 ~ 54 <- size
    private int stack;
    private String actuator;
    private String topic;
    public ActuatorControlGUI() {
        inventory = Bukkit.createInventory(null,defaultSize,title);
        addGuiItem((inventory.getSize()-6),Action.OK,"OK","");
        addGuiItem((inventory.getSize()-4),Action.CANCEL,"CANCEL","");
        stack=0;
    }
    public ActuatorControlGUI(Actuator val) {
        inventory = Bukkit.createInventory(null,defaultSize,title);
        addGuiItem((inventory.getSize()-6),Action.OK,"OK","");
        addGuiItem((inventory.getSize()-4),Action.CANCEL,"CANCEL","");
        stack=0;
        topic=val.getSystem_id()+"/actuator";
        actuator=val.getActuator();
    }

    public void addGuiItem(Action action, final String name, final String... lore){
        inventory.addItem(createGuiItem(action.getMaterial(), name, lore));
    }
    public void addGuiItem(int index,Action action, final String name, final String... lore){
        inventory.setItem(stack + index,createGuiItem(action.getMaterial(), name, lore));
    }
    public void setGuiItem(final String name, final String... lore){
        if(stack>defaultSize){
            stack = 0;
        }
        stack+=9;
        inventory.setItem(stack-1,createGuiItem(defaultItem, name, lore));
    }
    public void setGuiItem(int option,final String name, final String... lore){
        if(stack>defaultSize){
            stack = 0;
        }
        stack+=9;
        switch (option){
            case 0->{
                inventory.setItem(stack-1,createGuiItem(Action.OFF.getMaterial(), name, lore));
            }
            case 1->{
                inventory.setItem(stack-1,createGuiItem(Action.ON.getMaterial(), name, lore));
            }
            default -> {
                inventory.setItem(stack-1,createGuiItem(defaultItem, name, lore));
            }
        }
    }


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
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inventory) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        final Player p = (Player) e.getWhoClicked();
        String name = clickedItem.getItemMeta().getDisplayName();
        List<String> lore=clickedItem.getItemMeta().getLore();
        Material material = clickedItem.getType();
        ItemMeta temp;
        ItemStack target;
        if(material == Action.OK.getMaterial()){ // ok button action
            p.sendMessage("ok button clicked");
            int i=8;
            Material material1;
            String message="";
            while(i<defaultSize&&inventory.getItem(i)!=null){
                target = inventory.getItem(i);
                material1=target.getType();
                    if (Action.ON.getMaterial() == material1) {
                        // on state
                        message+=":1";
                    } else if (Action.OFF.getMaterial() == material1) {
                        // off state
                        message+=":0";
                    } else {
                        // ordered & range
                        message+=":"+ChatColor.stripColor(target.getItemMeta().getDisplayName());
                    }
                    i += 9;
            }
            try {
                ClientAgent.getClient().sendMessage(topic, message);
            }catch (MqttException me){
                me.printStackTrace();
            }
            // check each row last lost item value
            // doing actuating mqtt
            p.closeInventory();
        }else if (material == Action.CANCEL.getMaterial()){ // cancel button action
            p.sendMessage("cancel button clicked");
            // doing nothing
            p.closeInventory();
        }else if (material == Action.PLUS.getMaterial()){ // add button action
            // do a last slot item plus
            target = inventory.getItem((e.getSlot()/9*9)+8);
            temp= target.getItemMeta();
            temp.setDisplayName(addStrInt(target.getItemMeta().getDisplayName(),clickedItem.getItemMeta().getDisplayName()));
            target.setItemMeta(temp);
            p.sendMessage("You clicked "+name);
        }else if (material == Action.MINUS.getMaterial()){ // minus button action
            // do a last slot item minus
            target = inventory.getItem((e.getSlot()/9*9)+8);
            temp= target.getItemMeta();
            temp.setDisplayName(minusStrInt(target.getItemMeta().getDisplayName(),clickedItem.getItemMeta().getDisplayName().replace("-","")));
            target.setItemMeta(temp);
            p.sendMessage("You clicked "+name);
        }else if (material == Action.ON.getMaterial()){
            target = inventory.getItem((e.getSlot()/9*9)+8);
            if(target.getType()!=Action.ON.getMaterial()){
                target.setType(Action.ON.getMaterial());
            }
        }else if(material == Action.OFF.getMaterial()){
            target = inventory.getItem((e.getSlot()/9*9)+8);
            if(target.getType()!=Action.OFF.getMaterial()){
                target.setType(Action.OFF.getMaterial());
            }
        }
        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at raw slot " + e.getRawSlot());
        p.sendMessage("You clicked at slot"+ e.getSlot());
    }
    protected String addStrInt(String str1, String str2){
        return (Integer.parseInt(str1)+Integer.parseInt(str2))+"";
    }
    protected String minusStrInt(String str1, String str2){
        return (Integer.parseInt(str1)-Integer.parseInt(str2))+"";
    }
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inventory);
    }
}
