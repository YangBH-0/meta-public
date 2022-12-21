package com.beyound.dsem.meta.mysql;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.data.State;
import com.beyound.dsem.meta.mysql.DTO.*;
import com.beyound.dsem.meta.mysql.DTO.Module;
import com.beyound.dsem.meta.mysql.repository.ActuatorRepository;
import com.beyound.dsem.meta.mysql.repository.DeviceRegistryRepository;
import com.beyound.dsem.meta.mysql.repository.SensorRepository;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlService {
    private static MysqlService instance;
    private static DeviceRegistryRepository DRRepository;
    private static SensorRepository sensorRepository;
    private static ActuatorRepository actuatorRepository;
    private final String config = Meta.getInstance().getDataFolder()+"\\SensorDBConfig.json";

    public static MysqlService getInstance() {
        if(instance==null)
            try {
                return new MysqlService();
            }catch(IOException e){
                System.out.println("config is not found");
        }
        return instance;
    }
    public MysqlService() throws IOException {
        DBManager temp =new DBManager();
        DRRepository=new DeviceRegistryRepository(temp);
        sensorRepository = new SensorRepository(temp);
        actuatorRepository = new ActuatorRepository(temp);
    }
    public List<Device> getDevices(Player p){

        return DRRepository.getDevices();
    }
    public void getItems(){
        List<Item> items = DRRepository.getItems();
        for(Item i:items){
            //System.out.println(i.getItemId()+","+i.getModelName()+","+i.getDeviceType());
        }
    }
    public List<Module> getModules(int itemId){
        List<Module> modules=DRRepository.getModules(itemId);
        /*
        for(Module m: modules){
            System.out.println(m.getType()+","+m.getProperty()+","+m.getModelName()+","+ m.getUnitOfMeasure());
        }
         */
        return modules;
    }
    public List<Sensor> getSensors(String systemId, int itemId){
        List<Sensor> sensors = new ArrayList<>();
        List<Module> modules = this.getModules(itemId);
        for(Module module:modules){
            if(module.getType().equals("sensor")){
                //System.out.println(module);
                sensors.add(DRRepository.getSensor(systemId,itemId,module.getModelName(),module.getProperty()));
            }
        }
        return sensors;
    }
    public Actuator getActuatorModule(String systemId,int itemId, String actuatorModel){
        return DRRepository.getActuator(systemId,itemId,actuatorModel);
    }
    public Sensor getSensorModule(String systemId,int itemId,String sensorModel,String property){
        return DRRepository.getSensor(systemId,itemId,sensorModel,property);
    }
    public State getSensorData(String tblname, List<String> sensors){
        Map<String,String> temp= sensorRepository.getSensor(tblname, sensors);
        long timestamp = Timestamp.valueOf(temp.get("timestamp")).getTime();
        ArrayList<String> list = new ArrayList<>();
        //System.out.println(sensors);
        //System.out.println(temp);
        for(String sensor:sensors){
            list.add(temp.get(sensor));
        }
        //System.out.println(list);
        /*
        String[] data=new String[temp.size()];
        int count=0;
        for(String key:temp.keySet()){
            data[count++]=temp.get(key);
        }*/
        String[] strarr = new String[list.size()];
        for(int i=0;i<strarr.length;i++){
            strarr[i]= list.get(i);
        }
        return new State(timestamp, strarr);
    }
    public State getActuatorState(String tableName, List<String> actuators){
        Map<String, String> temp = actuatorRepository.getActuatorStates(tableName,actuators);
        //Map<String, State> states = new HashMap<>();
        long timestamp = Timestamp.valueOf(temp.get("timestamp")).getTime();
        ArrayList<String> list = new ArrayList<>();
        for(String actuator: actuators){
            list.add(temp.get(actuator));
        }
        return new State(timestamp,list.toArray(new String[0]));
    }
    public State getActuatorState(String tableName, String actuator){

        Map<String, String> temp = actuatorRepository.getActuatorState(tableName,actuator);
        //Map<String, State> states = new HashMap<>();
        long timestamp = Timestamp.valueOf(temp.get("timestamp")).getTime();
        ArrayList<String> list = new ArrayList<>();
        //System.out.println("map: "+temp.get(actuator));
        list.add(temp.get(actuator));
        return new State(timestamp,list.toArray(new String[0]));
    }

    public boolean compareSensorsCount(int item_id,int compareVal){
        int count = DRRepository.getSensorCount(item_id);
        return count<compareVal;
    }
}
