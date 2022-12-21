package com.beyound.dsem.meta.Object.entity.Sensor;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.entity.BlockType;
import com.beyound.dsem.meta.Object.entity.SensorObject;
import com.beyound.dsem.meta.Object.entity.binaryUtil;
import com.beyound.dsem.meta.mysql.DTO.Sensor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.util.Vector;

public class DoorObject extends SensorObject {

    Block infoSign;
    Block top;

    public DoorObject(Sensor sensor) {
        super(sensor);
    }

    public DoorObject(Sensor sensor, int max_status) {
        super(sensor, max_status);
    }

    public DoorObject(Block block, Sensor sensor) {
        super(block, sensor);
    }

    public DoorObject(Block block, Sensor sensor, int max_status) {
        super(block, sensor, max_status);
    }

    public void init(){
        initMainBlock();
        initDisplay();
    }
    private void initMainBlock(){
        Meta.getInstance().getServer().getScheduler().runTask(Meta.getInstance(),
                (Runnable) ()->{
                    //BlockFace blockFace = ((Directional)mainBlock.getBlockData()).getFacing();
                    Block bottom = mainBlock;
                    Block top = bottom.getRelative(BlockFace.UP);
                    bottom.setType(BlockType.Door.getMaterial(),false);
                    top.setType(BlockType.Door.getMaterial(),false);

                    Door d1 = (Door) bottom.getBlockData();
                    Door d2 = (Door) top.getBlockData();
                    d1.setHalf(Bisected.Half.BOTTOM);
                    d2.setHalf(Bisected.Half.TOP);
                    //d1.setFacing(blockFace);
                    //d2.setFacing(blockFace);

                    bottom.setBlockData(d1);
                    top.setBlockData(d2);
                }
        );
    }
    private void initDisplay(){

        //setSign();
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
                    sign.setBlockData(blockData);
                }
        );
    }
    @Override
    public void actuating(){
        Meta.getInstance().getServer().getScheduler().runTask(Meta.getInstance(),
                (Runnable) ()->{
                    BlockData door= (Door) mainBlock.getBlockData();
                    boolean Flag=false;
                    int status = binaryUtil.getStatus(this.sensor);
                    switch(status){
                        case 1-> {
                            Flag = false;
                        }
                        case 0->{
                                Flag = true;
                            }
                        }
                    ((Openable)door).setOpen(Flag);
                    mainBlock.setBlockData(door);
                    mainBlock.getState().update();
                }
        );
    }

}
