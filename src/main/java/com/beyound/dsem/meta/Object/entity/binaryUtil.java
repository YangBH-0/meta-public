package com.beyound.dsem.meta.Object.entity;

import com.beyound.dsem.meta.mysql.DTO.Actuator;
import com.beyound.dsem.meta.mysql.DTO.Sensor;

public class binaryUtil {
    public static int getStatus(Actuator actuator){
        String status= actuator.getState().getStatus()[0];
        if(status.equals("0")){
            return 0;
        }else if(status.equals("1")){
            return 1;
        }else{
            return 0;
        }
    }
    public static int getStatus(Sensor sensor){
        String status= sensor.getState().getStatus()[0];
        if(status.equals("0")){
            return 0;
        }else if(status.equals("1")){
            return 1;
        }else{
            return 0;
        }
    }
}
