package com.beyound.dsem.meta.Object.data;

import lombok.Getter;
import org.bukkit.util.Vector;

@Getter
public enum Direction {
    //동,서,남,북 ,위, 아래
    East(new Vector(1,0,0)),
    West(new Vector(-1,0,0)),
    South(new Vector(0,0,1)),
    North(new Vector(0,0,-1)),
    Up(new Vector(0,1,0)),
    Down(new Vector(0,-1,0));
    private final Vector vector;
    Direction(Vector vector){
        this.vector = vector;
    }
}
