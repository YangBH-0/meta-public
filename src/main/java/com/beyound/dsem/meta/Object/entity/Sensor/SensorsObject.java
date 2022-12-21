package com.beyound.dsem.meta.Object.entity.Sensor;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.entity.SensorObject;
import com.beyound.dsem.meta.Object.entity.binaryUtil;
import com.beyound.dsem.meta.mysql.DTO.Sensor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.util.Vector;

import java.util.List;

public class SensorsObject  extends SensorObject {

    private Block signBlock;


    public SensorsObject(Sensor sensor) {
        super(sensor);
    }

    public SensorsObject(Block block, Sensor sensor) {
        super(block, sensor);
    }

    public SensorsObject(Sensor sensor, int max_status) {
        super(sensor, max_status);
    }

    public SensorsObject(Block block, Sensor sensor, int max_status) {
        super(block, sensor, max_status);
    }

    public SensorsObject(Block block, List<Sensor> sensors){
        super(block,sensors.get(0),sensors.size());
        this.sensors=sensors;
    }

    @Override
    public void init() {
        setSign();
    }

    @Override
    public void actuating() {
        Meta.getInstance().getServer().getScheduler().runTask(Meta.getInstance(),
                (Runnable) ()->{
                    updateSign();

                }
        );

    }
    private void setSign(){
        Meta.getInstance().getServer().getScheduler().runTask(
                Meta.getInstance(),
                (Runnable) () -> {
                    //BlockFace blockFace = ((Directional)mainBlock.getBlockData()).getFacing();
                    Sensor s = this.sensor;
                    Block target = this.mainBlock;
                    Block temp =Meta.getInstance().getServer().getWorld("world").getBlockAt(target.getLocation().add(new Vector(0, 0, 1)));
                    temp.setType(Material.OAK_WALL_SIGN);
                    Sign sign = (Sign) temp.getState();


                    WallSign blockData = (WallSign) sign.getBlockData();
                    blockData.setFacing(BlockFace.SOUTH);
                    sign.setGlowingText(true);
                    sign.setLine(0, s.getModel());
                    sign.setLine(1, "sensor");
                    sign.setLine(2, s.getSensor());
                    System.out.println(s.getSensor()+" sign");
                    sign.setLine(3, s.getUnit_of_measure());
                    sign.update();
                    temp.setBlockData(blockData);
                    //sign.setBlockData(blockData);
                    this.signBlock=temp;
                }
        );
    }
    private void updateSign(){
        Sign sign= (Sign) signBlock.getState();
        //WallSign blockData = (WallSign) sign.getBlockData();
        //blockData.setFacing(BlockFace.SOUTH);
        //sign.setGlowingText(true);
        System.out.println(((WallSign)signBlock.getBlockData()).getFacing());
        int len = sensors.size()<=4 ? sensors.size() : 4;
        for(int i=0;i<len;i++) {
            String str = sensors.get(i).getSensor()+":"+this.sensor.getState().getStatus()[i];
            sign.setLine(i, str);
        }
        sign.update();
    }
    private void setFacing(Block block, BlockFace facing){
    }
}
