package com.beyound.dsem.meta.registry;

import com.beyound.dsem.meta.Object.VirtualObject;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import com.beyound.dsem.meta.mysql.DTO.Sensor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerRegistry {
    private static final PlayerRegistry instance = new PlayerRegistry();
    private static final Map<Player, ActType> actor = new HashMap<>();
    private static final Map<Player, VirtualObject> setObject = new HashMap<>();

    public static PlayerRegistry getInstance() {
        return instance;
    }

    public static void addPlayer(Player p) {
        if (!checkPlayer(p)) {
            actor.put(p, ActType.none);
        }
    }

    public static boolean checkPlayer(Player p) {
        return actor.containsKey(p);
    }

    public static void setPlayer(Player p, ActType value) {
        if (actor.containsKey(p)) {
            actor.replace(p, value);
        } else {
            addPlayer(p);
        }
    }

    public static ActType isAct(Player p) {
        return actor.get(p);
    }

    public static  void addObject(Player val1, VirtualObject val2){
        setObject.put(val1,val2);
    }
    public static boolean isObject(Player p){
        return setObject.containsKey(p);
    }
    public static VirtualObject getObject(Player p){
        return setObject.get(p);
    }
    public static boolean removeActuator(Player p){
        if(isObject(p)){
            setObject.remove(p);
            return true;
        }
        return false;
    }
}
