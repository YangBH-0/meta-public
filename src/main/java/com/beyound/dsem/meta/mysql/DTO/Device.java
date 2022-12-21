package com.beyound.dsem.meta.mysql.DTO;

import lombok.Getter;

import java.util.Date;

@Getter
public class Device {
    private final int deviceId;
    private final int itemId;
    private final String systemId;
    private final String deviceName;
    private final String tableName;
    private final Date deploymentTime;
    private final String deploymentLocation;
    private final double latitude;
    private final double longitude;
    public static class Builder{
        private final int itemId;
        private final String systemId;
        private final String deploymentLocation;
        private int deviceId=-1; // none = -1
        private String deviceName="unknown"; // "none val"
        private String tableName="unknown"; // "none val"
        private Date deploymentTime=new Date(); // if taken no val, init current time
        private double latitude=-1; // none = -1
        private double longitude=-1; // none = -1

        public Builder(int itemId, String systemId, String deploymentLocation) {
            this.itemId = itemId;
            this.systemId = systemId;
            this.deploymentLocation = deploymentLocation;
        }
        public Builder deviceId(int val){
            deviceId=val;
            return this;
        }
        public Builder deviceName(String val){
            deviceName=val;
            return this;
        }
        public Builder tableName(String val){
            tableName=val;
            return this;
        }
        public Builder deploymentTime(Date val){
            deploymentTime=val;
            return this;
        }
        public Builder latitude(double val){
            latitude=val;
            return this;
        }
        public Builder longitude(double val){
            longitude=val;
            return this;
        }

        public Device build(){
            return new Device(this);
        }
    }

    public Device(Builder builder) {
        this.deviceId = builder.deviceId;
        this.itemId = builder.itemId;
        this.systemId = builder.systemId;
        this.deviceName = builder.deviceName;
        this.tableName = builder.tableName;
        this.deploymentTime = builder.deploymentTime;
        this.deploymentLocation = builder.deploymentLocation;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }
}
