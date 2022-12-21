package com.beyound.dsem.meta.Object.entity;

import com.beyound.dsem.meta.Object.VirtualObject;
import com.beyound.dsem.meta.Object.data.State;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import org.bukkit.block.Block;

public abstract class ActuatorObject implements VirtualObject {

    protected Block mainBlock;
    protected final Actuator actuator;
    protected int max_status;
    public ActuatorObject(Actuator actuator){
        this(actuator,1);
    }
    public ActuatorObject (Actuator actuator,int max_status){
        this.actuator=actuator;
        this.max_status=max_status;

    }
    public ActuatorObject(Block block,Actuator actuator) {
        this(block,actuator,1);
    }

    public ActuatorObject(Block block,Actuator actuator, int max_status) {
        this.mainBlock=block;
        this.actuator = actuator;
        this.max_status = max_status;
    }
    abstract public void actuating();
    public void update(State state){
        actuator.setState(state);
    }
    public Actuator getActuator() {
        return actuator;
    }
    public void setBlock(Block block){
        this.mainBlock=block;
    }
}
