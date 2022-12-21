package com.beyound.dsem.meta.registry.sync;

import com.beyound.dsem.meta.Object.entity.ActuatorObject;
import com.beyound.dsem.meta.Object.entity.SensorObject;
import com.beyound.dsem.meta.Object.VirtualObject;
import com.beyound.dsem.meta.Object.data.State;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import com.beyound.dsem.meta.mysql.DTO.Sensor;
import com.beyound.dsem.meta.mysql.MysqlService;
import com.beyound.dsem.meta.registry.MetaRegistry;

import java.util.ArrayList;
import java.util.List;

public class SyncManager implements Runnable{
    private static SyncManager instance;

    public static SyncManager getInstance(){
        if(instance==null){
            instance= new SyncManager();
        }
        return instance;
    }
    @Override
    public void run() {
        while(true) {
            try {
                //System.out.println("sync manager awaken");
                if (MetaRegistry.getInstance().getActuatorObjects().size() != 0) {
                    System.out.println("current Actuator= "+ MetaRegistry.getInstance().getActuatorObjects().size());

                    //System.out.println("sync manager doing");
                    for (VirtualObject virtualObject : MetaRegistry.getInstance().getActuatorObjects()) {
                        ActuatorObject actuatorObject= (ActuatorObject) virtualObject;
                        Actuator actuator= actuatorObject.getActuator();
                        String tableName = actuator.getTableName();
                        //List<String> actuatorList = new ArrayList<>();
                        //actuatorList.add(actuator.getActuator());
                        State state = MysqlService.getInstance().getActuatorState(tableName, actuator.getActuator());
                        actuator.setState(state);
                        actuatorObject.actuating();
                    }
                }
                if(MetaRegistry.getInstance().getSensorObjects().size()!=0){
                    System.out.println("current Sensor= "+MetaRegistry.getInstance().getSensorObjects().size());
                    for(VirtualObject virtualObject:MetaRegistry.getSensorObjects()){
                        SensorObject sensorObject= (SensorObject)virtualObject;
                        ArrayList<String> sensors = new ArrayList<>();
                        if(sensorObject.getMax_status()==1) {
                            sensors.add(sensorObject.getSensor().getSensor());
                            sensorObject.update(MysqlService.getInstance().getSensorData(sensorObject.getSensor().getTable_name(),sensors));

                        }else{
                            for(Sensor sensor:sensorObject.getSensors()){
                                sensors.add(sensor.getSensor());
                            }
                            sensorObject.update(MysqlService.getInstance().getSensorData(sensorObject.getSensor().getTable_name(),sensors));
                        }
                        sensorObject.actuating();
                    }
                }
                //System.out.println("sync manager sleep");
                Thread.sleep(500);
            } catch (Exception E) {
                E.printStackTrace();
                break;
            }
        }
    }
}
