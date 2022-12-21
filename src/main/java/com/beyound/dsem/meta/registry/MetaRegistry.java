package com.beyound.dsem.meta.registry;

import com.beyound.dsem.meta.Object.entity.ActuatorObject;
import com.beyound.dsem.meta.Object.entity.SensorObject;
import com.beyound.dsem.meta.Object.VirtualObject;
import com.beyound.dsem.meta.Object.data.State;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import com.beyound.dsem.meta.mysql.DTO.Sensor;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaRegistry {
    private static final MetaRegistry registry = new MetaRegistry();
    private static final Map<Block, VirtualObject> actuatorBlock = new HashMap<>();
    private static final Map<Block, VirtualObject> sensorBlock = new HashMap<>();
    private static final Map<Block, VirtualObject> deviceBlock = new HashMap<>();

    public static MetaRegistry getInstance() {
        return registry;
    }

    public static void addActuatorBlock(Block val1, VirtualObject val2) {
        actuatorBlock.put(val1, val2);
    }

    public static void removeActuatorBlock(Block val1) {
        if (actuatorBlock.isEmpty())
            return;
        actuatorBlock.remove(val1);
    }

    public static ActuatorObject getActuatorObject(Block val1) {
        if (actuatorBlock.isEmpty())
            return null;
        return (ActuatorObject) actuatorBlock.get(val1);
    }

    public static ActuatorObject getActuatorObject(Actuator val1) {
        for (Block block : actuatorBlock.keySet()) {
            ActuatorObject actuatorObject = (ActuatorObject) actuatorBlock.get(block);
            if (val1.getSystem_id().equals(actuatorObject.getActuator().getSystem_id())) {
                return actuatorObject;
            }
        }
        return null;
    }

    public List<VirtualObject> getActuatorObjects() {
        return actuatorBlock.values().stream().toList();
    }


    public Block getActuatorBlockByActuator(Actuator actuator) {
        for (Block block : actuatorBlock.keySet()) {
            ActuatorObject actuatorObject = (ActuatorObject) actuatorBlock.get(block);
            if (actuator.getSystem_id().equals(actuatorObject.getActuator().getSystem_id())) {
                return block;
            }
        }
        return null;
    }

    public Actuator getActuatorBySystemId(String system_id) {
        for (VirtualObject virtualObject : actuatorBlock.values()) {
            Actuator actuator = virtualObjectToActuator(virtualObject);
            if (actuator.getSystem_id().equals(system_id)) {
                return actuator;
            }
        }
        return null;
    }
    public ActuatorObject getActuatorObjectBySystemId(String val1) {
        for (VirtualObject virtualObject : actuatorBlock.values()) {
            Actuator actuator = this.virtualObjectToActuator(virtualObject);
            if (actuator.getSystem_id().equals(val1)) {
                return (ActuatorObject) virtualObject;
            }
        }
        return null;
    }

    public boolean checkActuatorBlock(Block b) {
        return actuatorBlock.containsKey(b);
    }

    public boolean checkActuatorSystemId(String system_id) {
        System.out.println("check system");
        for (VirtualObject virtualObject : actuatorBlock.values()) {
            Actuator actuator = this.virtualObjectToActuator(virtualObject);
            System.out.println(actuator.getSystem_id());
            if (actuator.getSystem_id().equals(system_id)) {
                return true;
            }
        }
        return false;
    }

    public void changeActuatorState(VirtualObject virtualObject,State val2, boolean flag) {
        ActuatorObject temp= (ActuatorObject) virtualObject;
        temp.update(val2);
        if(flag)
            temp.actuating();
    }

    public void changeActuatorState(VirtualObject val1, State val2) {
        this.changeActuatorState(val1,val2,true);
    }
    public static void addSensorBlock(Block val1, VirtualObject val2) {
        sensorBlock.put(val1, val2);
    }

    public static void removeSensorBlock(Block b) {
        if (sensorBlock.isEmpty())
            return;
        sensorBlock.remove(b);
    }

    public static SensorObject getSensorObject(Block b) {
        if (sensorBlock.isEmpty())
            return null;
        return (SensorObject) sensorBlock.get(b);
    }
    public static List<VirtualObject> getSensorObjects(){
        return sensorBlock.values().stream().toList();
    }

    public static boolean checkSensorBlock(Block b) {
        return sensorBlock.containsKey(b);
    }

    private Actuator virtualObjectToActuator(VirtualObject val1) {
        return ((ActuatorObject) val1).getActuator();
    }

    private Sensor virtualObjectToSensor(VirtualObject val1) {
        return ((SensorObject) val1).getSensor();
    }
}
