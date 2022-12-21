package com.beyound.dsem.meta.Object.entity;

import com.beyound.dsem.meta.Object.VirtualObject;
import com.beyound.dsem.meta.Object.data.State;
import com.beyound.dsem.meta.mysql.DTO.Sensor;
import org.bukkit.block.Block;

import java.util.List;

public abstract class SensorObject implements VirtualObject{
    protected final Sensor sensor;
    protected List<Sensor> sensors;
    protected Block mainBlock;
    protected int max_status;

    public SensorObject(Sensor sensor){
        this(sensor,1);
    }
    public SensorObject(Block block, Sensor sensor) {
        this(block,sensor,1);
    }

    public SensorObject(Sensor sensor, int max_status) {
        this.sensor = sensor;
        this.max_status = max_status;
    }

    public SensorObject(Block block, Sensor sensor, int max_status) {
        this.mainBlock=block;
        this.sensor = sensor;
        this.max_status = max_status;
    }
    abstract public void actuating();
    public void update(State state){
           sensor.setState(state);
    }

    public Sensor getSensor(){
        return this.sensor;
    }
    public void setBlock(Block block){
        this.mainBlock=block;
    }
    public void setSensors(List<Sensor> list){
        this.sensors=list;
        max_status= this.sensors.size();
    }

    public int getMax_status() {
        return max_status;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
}
