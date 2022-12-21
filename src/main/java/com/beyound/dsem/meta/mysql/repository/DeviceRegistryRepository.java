package com.beyound.dsem.meta.mysql.repository;



import com.beyound.dsem.meta.mysql.DBManager;
import com.beyound.dsem.meta.mysql.DTO.*;
import com.beyound.dsem.meta.mysql.DTO.Module;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceRegistryRepository {
    DBManager dbManager;

    public DeviceRegistryRepository(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Device> getDevices(){
        try {
            dbManager.connectDB();
            String sql = "SELECT item_id,system_id,deployment_location,device_name FROM "+dbManager.getDeviceTableName();
            System.out.println(sql);
            ResultSet resultSet = dbManager.executeQuery(sql);
            List<Device> devices= new ArrayList<>();
            while(resultSet.next()){
                devices.add(new Device.Builder(resultSet.getInt("item_id"),resultSet.getString("system_id"),resultSet.getString("deployment_location"))
                        .deviceName(resultSet.getString("device_name")).build());
            }
            dbManager.disconnectDB();
            return devices;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql error");
        }
        return new ArrayList<>();
    }
    public List<Item> getItems(){

        try {
            dbManager.connectDB();
            String sql = "SELECT item_id,model_name,device_type FROM "+dbManager.getItemGlobalTableName();
            System.out.println(sql);
            ResultSet resultSet = dbManager.executeQuery(sql);
            List<Item> items= new ArrayList<>();
            while(resultSet.next()){
                items.add(new Item.Builder(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)).build());
            }
            dbManager.disconnectDB();
            return items;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql error");
        }
        return new ArrayList<>();
    }
    public Item getItem(int itemId){
        try {
            dbManager.connectDB();
            String sql = "SELECT item_id,model_name,device_type FROM "+dbManager.getItemGlobalTableName();
            System.out.println(sql);
            ResultSet resultSet = dbManager.executeQuery(sql);
            Item item = null;
            while(resultSet.next()){
                item=new Item.Builder(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)).build();
            }
            dbManager.disconnectDB();
            return item;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql error");
        }
        return null;
    }
    public List<Module> getModules(int itemId){
        try {
            dbManager.connectDB();
            String option="order by "+"sequence"+" asc";
            String group_col = "md_group";
            String key_col = "md_key";
            String value_col = "md_value";
            String sql = "SELECT "+group_col+","+key_col+","+value_col+" FROM "+dbManager.getItemSpecificTableName()+" where item_id="+itemId+" "+option;
            //System.out.println(sql);
            ResultSet resultSet = dbManager.executeQuery(sql);
            List<Module> modules= new ArrayList<>();
            int groupNum = 1;
            int count=0;
            String type = null;
            String property = null;
            String moduleName = null;
            String unitOfMeasure = null;
            int colCount = resultSet.getMetaData().getColumnCount();

            while(resultSet.next()){
                if(groupNum!=resultSet.getInt(group_col)){
                    groupNum=resultSet.getInt(group_col);
                    modules.add(new Module(type,property,moduleName));
                }
                if(groupNum==resultSet.getInt(group_col)) {
                    if(resultSet.getString(key_col).indexOf("model")!=-1){
                        moduleName=resultSet.getString(value_col);
                    }
                    if(resultSet.getString(key_col).equals("sensor")){
                        type="sensor";
                        property=resultSet.getString(value_col);
                    }
                    if(resultSet.getString(key_col).equals("actuator")){
                        type="actuator";
                        property=resultSet.getString(value_col);
                    }
                    if(resultSet.getString(key_col).indexOf("unit")!=-1){
                        unitOfMeasure=resultSet.getString(value_col);
                    }
                }
            }
            modules.add(new Module(type,property,moduleName));

            dbManager.disconnectDB();
            return modules;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql error");
        }
        return new ArrayList<>();
    }
    public Actuator getActuator(String systemId, int itemId, String actuator_model){
        try {
            dbManager.connectDB();
            String option="order by "+"sequence"+" asc";
            int group_index=1;
            int key_index=2;
            int value_index=3;
            String sql = "SELECT md_group,md_key,md_value FROM "+dbManager.getItemSpecificTableName()+" where item_id="+itemId+" "+option;
            System.out.println(sql);
            ResultSet resultSet = dbManager.executeQuery(sql);
            String groupNum = "";
            String actuator= "unknown";
            String requestType="unknown";
            String min_value="unknown";
            String max_value="unknown";
            while(resultSet.next()) {
                if (resultSet.getString(key_index).equals("actuator_model") || resultSet.getString(key_index).equals("actuator")) {
                    if (actuator_model.equals(resultSet.getString(value_index))) {
                        groupNum = resultSet.getString(group_index);
                        break;
                    }
                }
            }
            resultSet = dbManager.executeQuery(sql);

            while(resultSet.next()){
                if(groupNum.equals(resultSet.getString(group_index))) {
                    switch(resultSet.getString(key_index)){
                        case ("actuator")->{
                            actuator=resultSet.getString(value_index);
                        }
                        case ("request_type")->{
                            System.out.println(resultSet.getString(value_index));
                            requestType=resultSet.getString(value_index).trim().split(" ")[0].split("_")[0];
                        }
                        case ("min_value")->{
                            min_value=resultSet.getString(value_index).trim().split(" ")[0].split("_")[0];
                        }
                        case ("max_value")->{
                            max_value = resultSet.getString(value_index).trim().split(" ")[0].split("_")[0];
                        }

                    }
                }
            }
            Actuator temp = null;
            String table_name= this.getTableName(systemId);
            System.out.println("request Type : " + requestType);
            System.out.println(actuator_model+" is " +actuator+" & " + systemId+" . ");
            if(requestType.indexOf(RequestType.binary_number.getVal())!=-1){
                temp=new Actuator.Builder(actuator_model,actuator,systemId)
                        .requestType(requestType)
                        .tableName(table_name)
                        .build();
            }else if(requestType.indexOf(RequestType.range_of_numbers.getVal())!=-1){
                temp=new Actuator.Builder(actuator_model,actuator,systemId)
                        .requestType(requestType)
                        .tableName(table_name)
                        .minValue(Integer.parseInt(min_value))
                        .maxValue(Integer.parseInt(max_value))
                        .build();
            }

            dbManager.disconnectDB();
            return temp == null ?
                    new Actuator.Builder("unknown","unknown","unknown").build()
                    : temp;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql error");
        }
        return new Actuator.Builder("unknown","unknown","unknown").build();
    }
    public Sensor getSensor(String systemId, int itemId, String sensor_model, String property){
        try {
            dbManager.connectDB();
            String option = "order by " + "sequence" + " asc";
            String group_col = "md_group";
            String key_col = "md_key";
            String value_col = "md_value";
            String subquery="(select "+group_col+" from "+dbManager.getItemSpecificTableName()+" where item_id="+itemId+" and md_value=\""+property+"\" limit 1) ";
            String sql = "SELECT "+group_col+","+key_col+","+value_col+" FROM " + dbManager.getItemSpecificTableName() + " where item_id=" + itemId + " and "+group_col+"="+subquery +" "+ option;
            System.out.println(sql);
            ResultSet resultSet = dbManager.executeQuery(sql);
            String sensor="";
            String groupNum = "";
            String UoM="";
            while(resultSet.next()){
                if(resultSet.getString(2).equals(sensor_model)){
                    groupNum=resultSet.getString(1);
                    break;
                }
            }
            while (resultSet.next()) {
                if (groupNum.equals(resultSet.getString(1))) {
                    switch(resultSet.getString(2)){
                        case("unit_of_measure")->{
                            UoM+= resultSet.getString(3)+" ";
                        }
                        case("sensor")->{
                            sensor=resultSet.getString(3);
                        }
                    }
                }
            }
            String table_name= this.getTableName(systemId);
            Sensor temp = Sensor.builder()
                    .model(sensor_model)
                    .sensor(sensor.equals("")?property:sensor)
                    .unit_of_measure(UoM)
                    .system_id(systemId)
                    .table_name(table_name)
                    .build();
            System.out.println(temp.toString());
            dbManager.disconnectDB();
            return temp;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql error");
        }
        return Sensor.builder()
                .model(sensor_model)
                .sensor("unknown")
                .unit_of_measure("unknown")
                .system_id(systemId)
                .build();

    }

    private String getTableName(String systemId){
        String answer= "404";
        try{

            String sql = "select table_name FROM "+dbManager.getDeviceTableName()+" where system_id=\""+systemId+"\"";
            System.out.println(sql);
            ResultSet resultSet=dbManager.executeQuery(sql);
            String table_name="";
            while(resultSet.next()){
                table_name=resultSet.getString(1);
            }
            return table_name;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return answer;
    }
    public int getSensorCount(int itemId){
        try {
            dbManager.connectDB();
            int count=0;
            //String option = "order by " + "sequence" + " asc";
            String sql = "SELECT count(*) FROM " + dbManager.getItemSpecificTableName() + " where item_id=" + itemId +" and md_key="+"\"sensor\"";
            // System.out.println(sql);
            ResultSet resultSet = dbManager.executeQuery(sql);
            while (resultSet.next()) {
                count= resultSet.getInt("count(*)");
            }

            return count;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql error");
        }
        return -1;

    }

}
