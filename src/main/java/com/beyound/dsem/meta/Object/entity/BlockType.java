package com.beyound.dsem.meta.Object.entity;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum BlockType {
    Door(Material.IRON_DOOR),
    LED(Material.REDSTONE_LAMP),
    Switch(Material.LEVER),
    Actuator(Material.REDSTONE_LAMP),
    Sensor(Material.OBSERVER);

    private final Material material;

    BlockType(Material material) {
        this.material = material;
    }
}
