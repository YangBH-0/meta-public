package com.beyound.dsem.meta.Object.entity;

import com.beyound.dsem.meta.Object.entity.Actuator.FanObject;
import com.beyound.dsem.meta.Object.entity.Actuator.LEDObject;
import com.beyound.dsem.meta.Object.entity.Sensor.DoorObject;
import com.beyound.dsem.meta.Object.entity.Sensor.SensorFanObject;
import com.beyound.dsem.meta.Object.VirtualObject;
import com.beyound.dsem.meta.Object.entity.Sensor.SensorsObject;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import com.beyound.dsem.meta.mysql.DTO.Sensor;
import com.beyound.dsem.meta.mysql.MysqlService;

import java.util.List;

public class VirtualFactory {

    public static VirtualObject getObject(String type, String system_id, int item_id, String model,String property) {
        System.out.println("check factory");
        if ("actuator".equalsIgnoreCase(type)) {
            System.out.println("check actuator factory");
            Actuator temp = MysqlService.getInstance().getActuatorModule(system_id, item_id, model);

            switch (temp.getActuator()) {
                case "led" -> {
                    System.out.println("check led factory");
                    return new LEDObject(temp);
                }
                case "motor" ->{
                    System.out.println("check motor factory");
                    return new FanObject(temp);
                }
                default -> {
                    System.out.println("out of actuator list");
                }
            }
        } else if ("sensor".equalsIgnoreCase(type)) {
            System.out.println("check sensor factory");
            if(MysqlService.getInstance().compareSensorsCount(item_id,2)) {
                Sensor temp = MysqlService.getInstance().getSensorModule(system_id, item_id, model,property);
                System.out.println(temp.toString());
                System.out.println(temp.getSensor());
                switch (temp.getSensor()) {
                    case "infrared" -> {
                        System.out.println("check door factory");
                        return new DoorObject(temp);
                    }
                    case "motor" -> {
                        System.out.println("check sensor motor factory");
                        return new SensorFanObject(temp);
                    }

                    default -> {
                        System.out.println("out of sensor list");
                    }
                }
            }else{
                System.out.println("multi sensors");
                List<Sensor> sensorList = MysqlService.getInstance().getSensors(system_id,item_id);
                SensorsObject sensorsObjectInstance = new SensorsObject(sensorList.get(0), sensorList.size());
                sensorsObjectInstance.setSensors(sensorList);
                return sensorsObjectInstance;
            }
        }
        return null;
    }
}
