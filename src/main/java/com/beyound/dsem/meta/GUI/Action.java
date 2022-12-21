package com.beyound.dsem.meta.GUI;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum Action {
    PLUS("+", Material.RED_CONCRETE),
    MINUS("-", Material.BLUE_CONCRETE),
    ON("1",Material.GREEN_DYE),
    OFF("0",Material.RED_DYE),
    OK("OK",Material.GREEN_WOOL),
    CANCEL("CANCEL",Material.RED_WOOL);
    private final String action;
    private final Material material;
    Action(String operator, Material material){
        this.action = operator;
        this.material = material;
    }
}
