package com.beyound.dsem.meta.commands;

import com.beyound.dsem.meta.GUI.DeviceGUI;
import com.beyound.dsem.meta.Object.entity.SensorObject;
import com.beyound.dsem.meta.Object.VirtualObject;
import com.beyound.dsem.meta.Object.data.State;
import com.beyound.dsem.meta.mysql.DTO.Device;
import com.beyound.dsem.meta.mysql.MysqlService;
import com.beyound.dsem.meta.registry.ActType;
import com.beyound.dsem.meta.registry.MetaRegistry;
import com.beyound.dsem.meta.registry.PlayerRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.beyound.dsem.meta.Meta;

import java.util.ArrayList;
import java.util.List;

public class MetaCommand implements CommandExecutor {
    private final Meta plugin = Meta.getInstance();
    private final String prefix = plugin.prefix;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) { // only player
            Player p = (Player) sender;
            if(args[0].equalsIgnoreCase("getDevice")){
                p.sendMessage("check command");
                try{
                    List<Device> devices=MysqlService.getInstance().getDevices(p);
                    DeviceGUI temp = new DeviceGUI(p);
                    plugin.getServer().getPluginManager().registerEvents(temp,plugin);
                    List<String> data;
                    for(Device d:devices){
                        data=new ArrayList<>();
                        data.add("Location : "+d.getDeploymentLocation());
                        data.add("system id : " + d.getSystemId());
                        data.add("item id : " + d.getItemId());
                        temp.addGuiItem(d.getDeviceName(),data.toArray(new String[0]));
                        //System.out.println("id:"+d.getItemId()+","+d.getSystemId()+","+d.getDeploymentLocation()+","+d.getDeviceName());
                    }
                    temp.openInventory(p);
                }catch(Exception e){
                    plugin.getLogger().info("plugin is error");
                }
                /*
                try {
                    RetrofitHttpService.getService().getDevices(plugin.getLogger(), p);
                }catch (Exception e){
                    plugin.getLogger().info(String.valueOf(e.getStackTrace()));
                }*/

            }
            else if(args[0].equalsIgnoreCase("setActuator")){
                if (!PlayerRegistry.checkPlayer(p)) {
                    PlayerRegistry.addPlayer(p);
                }
                PlayerRegistry.setPlayer(p, ActType.Actuator);
            }
            else if(args[0].equalsIgnoreCase("setSensor")){
                if (!PlayerRegistry.checkPlayer(p)) {
                    PlayerRegistry.addPlayer(p);
                }
                PlayerRegistry.setPlayer(p,ActType.Sensor);
            }
            else if(args[0].equalsIgnoreCase("checkState")){

                p.sendMessage("checked : "+PlayerRegistry.isAct(p));
                //Actuator temp =PlayerRegistry.getActuator(p);
                //p.sendMessage(temp.getModel()+" : "+temp.getActuator()+" : "+temp.getSystem_id());
            }
            else if(args[0].equalsIgnoreCase("update")){
                List<VirtualObject> sensorObjects = MetaRegistry.getSensorObjects();
                for(VirtualObject virtualObject:sensorObjects){
                    SensorObject sensorObject= (SensorObject)virtualObject;
                    ArrayList<String> sensors =new ArrayList<String>();
                    sensors.add(sensorObject.getSensor().getSensor());
                    State data = MysqlService.getInstance().getSensorData(sensorObject.getSensor().getTable_name(),sensors);
                    sensorObject.update(data);
                    sensorObject.actuating();
                }
            }
            return true;
        }

        return false;
    }
}
