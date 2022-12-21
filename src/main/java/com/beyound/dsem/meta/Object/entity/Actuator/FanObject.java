package com.beyound.dsem.meta.Object.entity.Actuator;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.entity.ActuatorObject;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;

public class FanObject extends ActuatorObject {

    private List<ArmorStand> fanList;
    private Material fan_material= Material.IRON_SWORD;

    public FanObject(Actuator actuator) {
        super(actuator);
    }

    public FanObject(Actuator actuator, int max_status) {
        super(actuator, max_status);
    }

    public FanObject(Block block, Actuator actuator) {
        super(block, actuator);
    }

    public FanObject(Block block, Actuator actuator, int max_status) {
        super(block, actuator, max_status);
    }
    @Override
    public void init() {
        Meta.getInstance().getServer().getScheduler().runTask(
                Meta.getInstance(),
                (Runnable) ->{
                    init_mainBlock();
                    init_fan();
                });
    }
    private void init_mainBlock(){
        Block target = mainBlock;
        target.setType(Material.POLISHED_BLACKSTONE_BUTTON);
        BlockData floorface = Material.POLISHED_BLACKSTONE_BUTTON.createBlockData("[face=floor]");
        target.setBlockData(floorface);
        target.setBlockData(floorface);
    }
    private void init_fan(){
        fanList = new ArrayList<>();
        int len=6;
        for(int i=0;i<len;i++){
            ArmorStand _as = create_armor_stand_fan(this.mainBlock.getLocation());
            Location location = _as.getLocation();
            location.setYaw(location.getYaw()+i*(360/len));
            _as.teleport(location);

            System.out.println("Yaw:" + _as.getLocation().getYaw());
            fanList.add(_as);
        }
    }
    private ArmorStand create_armor_stand_fan(Location location){
        ArmorStand as = (ArmorStand) location.add(0.5,-1.4,0.5).getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setVisible(false);
        as.setMarker(true);

        as.setCustomName("fan");
        as.setCustomNameVisible(false);

        as.setArms(true);
        as.setCanPickupItems(true);
        as.setRightArmPose(createEulerAngle(180,0,90));
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
        }.runTaskTimer(Meta.getInstance(),0L,8L);
    }
    private void rotateFan(){
        if(fanList!=null && !fanList.isEmpty()) {
            for (ArmorStand armorStand : fanList) {
                Location location = armorStand.getLocation();

                //System.out.println(Float.parseFloat(actuator.getState().getStatus()[0]));
                //System.out.println(calculate_Yaw(30,Float.parseFloat(actuator.getState().getStatus()[0])));
                location.setYaw((location.getYaw() + calculate_Yaw(Float.parseFloat(actuator.getState().getStatus()[0]),30))%360);
                armorStand.teleport(location);
            }
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
    private EulerAngle createEulerAngle(double val1, double val2, double val3){
        return new EulerAngle(Math.toRadians(val1),Math.toRadians(val2),Math.toRadians(val3));
    }
}
