package com.beyound.dsem.meta.mysql.repository;

import com.beyound.dsem.meta.mysql.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActuatorRepository {

    private DBManager dbmanager;
    public ActuatorRepository(DBManager dbmanager) {
        this.dbmanager = dbmanager;
    }

    public Map<String,String> getActuatorStates(String tableName, List<String> actuators){
        Map<String,String> states = new HashMap();
        try{
            dbmanager.connectDB();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("select timestamp,");
            for(String actuator: actuators){
                sqlBuilder.append(actuator+",");
            }
            sqlBuilder.setLength(sqlBuilder.length()-1);
            sqlBuilder.append(" from "+dbmanager.getSensorDB()+"."+tableName+" order by timestamp desc limit 1");
            //System.out.println(sqlBuilder.toString());
            ResultSet resultSet = dbmanager.executeQuery(sqlBuilder.toString());
            while(resultSet.next()) {
                states.put("timestamp",resultSet.getString(1));
                for (int i = 0; i < resultSet.getMetaData().getColumnCount()-1; i++) {
                    states.put(actuators.get(i), resultSet.getString(i+2));
                }
            }
            //System.out.println(states.get("timestamp"));
            dbmanager.disconnectDB();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return states;
    }
    public Map<String,String> getActuatorState(String tableName, String actuator){
        Map<String,String> states = new HashMap();
        try{
            dbmanager.connectDB();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("select timestamp,");
            sqlBuilder.append(actuator+" ");

            sqlBuilder.append(" from "+dbmanager.getSensorDB()+"."+tableName+" order by timestamp desc limit 1");
            System.out.println(sqlBuilder.toString());
            ResultSet resultSet = dbmanager.executeQuery(sqlBuilder.toString());
            while(resultSet.next()) {
                states.put("timestamp",resultSet.getString(1));
                System.out.println(resultSet.getString(2));
                states.put(actuator,resultSet.getString(2));
            }
            //System.out.println(states.get("timestamp"));
            dbmanager.disconnectDB();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return states;
    }

}
