package com.beyound.dsem.meta.events;

import com.beyound.dsem.meta.mysql.DTO.RequestType;
import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.entity.ActuatorObject;
import com.beyound.dsem.meta.Object.entity.SensorObject;
import com.beyound.dsem.meta.Object.VirtualObject;
import com.beyound.dsem.meta.mqtt.ClientAgent;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import com.beyound.dsem.meta.registry.ActType;
import com.beyound.dsem.meta.registry.MetaRegistry;
import com.beyound.dsem.meta.registry.PlayerRegistry;
import com.beyound.dsem.meta.utils.JsonUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.logging.Logger;

public class MetaEvent implements Listener {
    private final Meta plugin = Meta.getInstance();
    private final String prefix = plugin.prefix;
    private final Logger logger = plugin.getLogger();
    private final String[] requestTypes= {
            RequestType.binary_number.getVal(),
            RequestType.ordered_values.getVal(),
            RequestType.range_of_numbers.getVal()
    };

    @EventHandler
    public void locateBlock(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        VirtualObject virtualObject = PlayerRegistry.getObject(p);
        switch (PlayerRegistry.isAct(p)) {
            case none -> {
            }
            case Actuator -> {
                if (PlayerRegistry.isObject(p)) {
                    if(virtualObject instanceof ActuatorObject) {

                        ActuatorObject actuatorObject = (ActuatorObject) virtualObject;
                        if(ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equals(actuatorObject.getActuator().getModel())) {
                            MetaRegistry.addActuatorBlock(e.getBlock(), virtualObject);
                            actuatorObject.setBlock(e.getBlock());
                            actuatorObject.init();
                            actuatorObject.actuating();
                            PlayerRegistry.setPlayer(p, ActType.none);
                            p.sendMessage("successful place actuator");
                            //ClientAgent.subscribeActuator(a);
                        }else{
                            p.sendMessage("you handle wrong item");
                        }
                    } else {
                        p.sendMessage("you first get Actuator by command");
                        e.setCancelled(true);
                    }
                }
            }
            case Sensor -> {
                System.out.println(virtualObject.getClass());
                if (virtualObject instanceof SensorObject) {
                    SensorObject sensorObject = (SensorObject) virtualObject;
                if(ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equals(sensorObject.getSensor().getModel())) {
                    MetaRegistry.addSensorBlock(e.getBlock(), virtualObject);
                    sensorObject.setBlock(e.getBlock());
                    sensorObject.init();
                    sensorObject.actuating();
                    PlayerRegistry.setPlayer(p, ActType.none);
                    p.sendMessage("successful place sensor");
                }else{
                    p.sendMessage("you handle wrong item.");
                }
                } else {
                    p.sendMessage("you first get Sensor by command");
                    e.setCancelled(true);
                }
            }
            case Actuating -> {

            }
            default -> {
                System.out.println("시스템 상 없는 오류");
            }
        }
    }
    @EventHandler
    public void getActuatingGUI(BlockDamageEvent e){
        Block b = e.getBlock();
        if(MetaRegistry.getInstance().checkActuatorBlock(b)) {
            Actuator a = MetaRegistry.getActuatorObject(b).getActuator();
            Player p = e.getPlayer();
            p.getWorld().getBlockAt(p.getLocation()).setType(Material.OAK_SIGN);
            Sign sign = (Sign) p.getWorld().getBlockAt(p.getLocation()).getState();
            PlayerRegistry.setPlayer(p,ActType.Actuating);
            PlayerRegistry.addObject(p,MetaRegistry.getActuatorObject(b));
            p.openSign(sign);
            /*
            ItemStack itemStack = new ItemStack(Material.OAK_SIGN);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(a.getActuator());
            List<String> data = new ArrayList<>();
            data.add("Max Val :"+a.getMax_value());
            data.add("Min Val :"+a.getMin_value());
            itemMeta.setLore(data);
            PlayerRegistry.setPlayer(p,ActType.Actuating);
            PlayerRegistry.addActuator(p,a);
            Location l=b.getLocation().add(0,1,0);
            */
            // panel code
            /*
            Actuator a = MetaRegistry.getActuatorBlock(b);
            ActuatorControlGUI temp = new ActuatorControlGUI(a);
            plugin.getServer().getPluginManager().registerEvents(temp, plugin);
            int index = 0;
            System.out.println("Check actuator request_type : " + a.getRequest_type());
            switch (a.getRequest_type()) {
                case ("binary") -> {
                    temp.addGuiItem(index++, Action.ON, "ON");
                    temp.addGuiItem(index++, Action.OFF, "OFF");
                    temp.setGuiItem(Integer.parseInt(a.getState()), a.getState());
                }
                case ("ordered") -> {
                    temp.addGuiItem(index++, Action.PLUS, "1");
                    temp.addGuiItem(index++, Action.MINUS, "-1");
                    temp.addGuiItem(index++, Action.PLUS, "5");
                    temp.addGuiItem(index++, Action.MINUS, "-5");
                    temp.addGuiItem(index++, Action.PLUS, "10");
                    temp.addGuiItem(index++, Action.MINUS, "-10");
                    temp.addGuiItem(index++, Action.PLUS, "50");
                    temp.addGuiItem(index++, Action.MINUS, "-50");
                    temp.setGuiItem(a.getState(), "max val : " + a.getMax_value(), "min val : " + a.getMin_value());
                }
                case ("range") -> {
                    temp.addGuiItem(index++, Action.PLUS, "1");
                    temp.addGuiItem(index++, Action.MINUS, "-1");
                    temp.addGuiItem(index++, Action.PLUS, "5");
                    temp.addGuiItem(index++, Action.MINUS, "-5");
                    temp.addGuiItem(index++, Action.PLUS, "10");
                    temp.addGuiItem(index++, Action.MINUS, "-10");
                    temp.addGuiItem(index++, Action.PLUS, "50");
                    temp.addGuiItem(index++, Action.MINUS, "-50");
                    temp.setGuiItem(a.getState(), "max val : " + a.getMax_value(), "min val : " + a.getMin_value());
                }
            }
            temp.openInventory(e.getPlayer());
            ClientAgent.subscribeActuator(a);
        }*/
        }
    }
    @EventHandler
    public void newVisit(PlayerJoinEvent e) {
        PlayerRegistry.addPlayer(e.getPlayer());
    }
    @EventHandler
    public void onSignEvent(SignChangeEvent e){
        if(PlayerRegistry.isAct(e.getPlayer())==ActType.Actuating) {
            Player p = e.getPlayer();
            Actuator a = ((ActuatorObject)PlayerRegistry.getObject(p)).getActuator();
            HashMap<String, String> data = new HashMap<>();
            String[] arrStr = e.getLines();
            for(String str:e.getLines()){
                System.out.println(str);
            }
            String topic = a.getSystem_id() + "/COMMAND";
            String value="";
            boolean check=true; // condition checker for min, max val
            switch (a.getRequest_type()) {
                case ("binary") -> {
                    String temp = arrStr[0];
                    if(temp.equals("1") || temp.toLowerCase().equals("on")){
                        value+="1";
                    }else if (temp.equals("0") || temp.toLowerCase().equals("off")){
                        value+=0;
                    }else{
                        check=false;
                    }
                    data.put(a.getActuator(),value);

                }
                case ("ordered") -> { // 제어 정의 다시 해야함.
                }
                case ("range") -> {
                    String temp = arrStr[0];
                    int val = Integer.parseInt(temp);
                    if(val> a.getMin_value() && val<a.getMax_value()){
                        value+=arrStr[0];
                    }
                    else{
                        check=false;
                    }
                    data.put(a.getActuator(),value);
                }
            }
            try {
                if(check)
                    ClientAgent.getClient().sendMessage(topic, JsonUtils.createActuatorData(a.getSystem_id(),data));
                else
                    p.sendMessage("잘 못 된 형식입니다. 형식에 맞게 입력하십시오.");
            }
            catch (MqttException exception){
                System.out.println(exception);
            }
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
        }

    }
}
