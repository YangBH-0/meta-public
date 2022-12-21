package com.beyound.dsem.meta.mysql.repository;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.mysql.DBManager;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorRepository {
    private DBManager dbmanager;
    public SensorRepository(DBManager dbmanager) {
        this.dbmanager = dbmanager;
    }
    public Map<String,String> getSensor(String tblName, List<String> sensors){
        Map<String,String> data=new HashMap<>();
        try {
            dbmanager.connectDB();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("select timestamp,");
            for(String sensor: sensors){
                sqlBuilder.append(sensor+",");
            }
            sqlBuilder.setLength(sqlBuilder.length()-1);
            sqlBuilder.append(" from "+dbmanager.getSensorDB()+"."+tblName+" order by timestamp desc limit 1");
            //System.out.println(sqlBuilder.toString());
            ResultSet resultSet = dbmanager.executeQuery(sqlBuilder.toString());
            ResultSetMetaData rsmd = resultSet.getMetaData();
            while(resultSet.next()){
                for(int i=1;i<=rsmd.getColumnCount();i++) {
                    //System.out.println(rsmd.getColumnName(i)+" : "+ resultSet.getString(rsmd.getColumnName(i)));
                    data.put(rsmd.getColumnName(i),resultSet.getString(rsmd.getColumnName(i)));
                }
            }
            dbmanager.disconnectDB();
        }catch(SQLException e){
            System.out.println(e);
        }
        return data;
    }
}
