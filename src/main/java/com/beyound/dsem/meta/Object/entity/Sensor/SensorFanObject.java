package com.beyound.dsem.meta.Object.entity.Sensor;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.entity.SensorObject;
import com.beyound.dsem.meta.mysql.DTO.Sensor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.List;

public class SensorFanObject extends SensorObject {

    List<ArmorStand> fanList;
    Material fan_material= Material.IRON_SWORD;
    public SensorFanObject(Sensor sensor) {
        super(sensor);
    }

    public SensorFanObject(Block block, Sensor sensor) {
        super(block, sensor);
    }

    public SensorFanObject(Sensor sensor, int max_status) {
        super(sensor, max_status);
    }

    public SensorFanObject(Block block, Sensor sensor, int max_status) {
        super(block, sensor, max_status);
    }
    @Override
    public void init() {
        Meta.getInstance().getServer().getScheduler().runTask(
                Meta.getInstance(),
                (Runnable) ->{
                    init_fan();
                });
    }
    private void init_fan(){
        for(int i=0;i<6;i++){
            ArmorStand _as = create_armor_stand_fan(this.mainBlock.getLocation());
            _as.setRotation(0,60);
            fanList.add(_as);
        }
    }
    private ArmorStand create_armor_stand_fan(Location location){
        ArmorStand as = (ArmorStand) location.add(0,-0.9,0).getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setVisible(false);
        as.setMarker(true);

        as.setCustomName("fan");
        as.setCustomNameVisible(false);

        as.setArms(true);
        as.setCanPickupItems(true);
        as.setRightArmPose(new EulerAngle(180*Math.PI,0,90*Math.PI));
        as.getEquipment().setItemInMainHand(new ItemStack(fan_material));

        return as;
    }

    @Override
    public void actuating() {
        new BukkitRunnable(){
            @Override
            public void run() {
                rotateFan();
            }
        }.runTaskTimer(Meta.getInstance(),1L,10L);
    }
    private void rotateFan(){
        for(ArmorStand armorStand: fanList){
            Location location = armorStand.getLocation();
            location.setYaw(calculate_Yaw(45,Float.parseFloat(this.sensor.getState().getStatus()[0])));
            armorStand.teleport(location);
        }
    }
    private float normalization(float val1,float min, float max){
        float result = (val1-min)/(max-min);
        return result;
    }
    private float calculate_Yaw(float val1,float val2){
        float temp=val1*normalization(val2,0,255);
        return temp;
    }
}
