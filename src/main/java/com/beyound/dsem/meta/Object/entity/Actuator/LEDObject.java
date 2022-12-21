package com.beyound.dsem.meta.Object.entity.Actuator;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.entity.ActuatorObject;
import com.beyound.dsem.meta.Object.entity.BlockType;
import com.beyound.dsem.meta.Object.entity.binaryUtil;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.util.Vector;

public class LEDObject extends ActuatorObject {


    Block infoSign;

    public LEDObject(Actuator actuator) {
        super(actuator);
    }

    public LEDObject(Actuator actuator, int max_status) {
        super(actuator, max_status);
    }

    public LEDObject(Block block, Actuator actuator) {
        super(block, actuator);
    }

    public LEDObject(Block block, Actuator actuator, int max_status) {
        super(block, actuator, max_status);
    }

    public void init(){
        initMainBlock();
        initDisplay();
    }
    private void initMainBlock(){
        Meta.getInstance().getServer().getScheduler().runTask(Meta.getInstance(),
                (Runnable) ()->{
                    mainBlock.setType(BlockType.LED.getMaterial());
                }
        );
    }
    private void initDisplay(){
        setSign();
    }
    private void setSign(){
        Meta.getInstance().getServer().getScheduler().runTask(
                Meta.getInstance(),
                (Runnable) () -> {
                    Actuator a= this.actuator;
                    Block target = this.mainBlock;
                    Block temp = Meta.getInstance().getServer().getWorld("world").getBlockAt(target.getLocation().add(new Vector(0, 0, 1)));
                    temp.setType(Material.OAK_WALL_SIGN);
                    Sign sign = (Sign) temp.getState();
                    WallSign blockData = (WallSign) sign.getBlockData();
                    blockData.setFacing(BlockFace.SOUTH);
                    sign.setBlockData(blockData);
                    sign.setGlowingText(true);
                    sign.setLine(0, a.getModel());
                    sign.setLine(1, a.getSystem_id());
                    sign.setLine(2, a.getActuator());
                    sign.setLine(3, a.getState().getStatus()[0]);
                    sign.update();
                    sign.setBlockData(blockData);
                }
        );
    }

    @Override
    public void actuating() {
        setSign();
        Meta.getInstance().getServer().getScheduler().runTask(Meta.getInstance(),
                (Runnable) ()->{
                    Lightable blockData=(Lightable)mainBlock.getBlockData();
                    boolean Flag=false;
                    int status = binaryUtil.getStatus(actuator);
                    switch(status){
                        case 0-> {
                            Flag = false;
                        }
                        case 1->{
                            Flag = true;
                        }
                    }
                    if(blockData.isLit() != Flag){
                        blockData.setLit(Flag);
                    }
                    mainBlock.setBlockData(blockData);
                    mainBlock.getState().update();
                }
        );
    }
}
