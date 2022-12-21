package com.beyound.dsem.meta.mysql.DTO;

import lombok.Getter;

import java.util.Date;

@Getter
public class Item {
    private final int itemId;
    private final String modelName;
    private final Date registrationTime;
    private final String deviceType;
    private final String manufacturer;
    private final String category;
    public static class Builder{
        private final int itemId;
        private final String modelName;
        private Date registrationTime= new Date(); // if taken no val, init current time
        private final String deviceType;
        private String manufacturer="unknown"; // "none val"
        private String category="unknown"; // "none val"
        public Builder(int itemId, String modelName, String deviceType) {
            this.itemId = itemId;
            this.modelName = modelName;
            this.deviceType = deviceType;
        }
        public Builder registrationTime(Date val){
            registrationTime=val;
            return this;
        }
        public Builder manufacturer(String val){
            manufacturer=val;
            return this;
        }
        public Builder category(String val){
            category=val;
            return this;
        }
        public Item build(){
            return new Item(this);
        }

    }

    public Item(Builder builder) {
        this.itemId = builder.itemId;
        this.modelName = builder.modelName;
        this.registrationTime = builder.registrationTime;
        this.deviceType = builder.deviceType;
        this.manufacturer = builder.manufacturer;
        this.category = builder.category;
    }
}
