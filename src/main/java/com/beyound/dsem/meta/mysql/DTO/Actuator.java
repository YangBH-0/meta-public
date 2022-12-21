package com.beyound.dsem.meta.mysql.DTO;


import com.beyound.dsem.meta.Object.data.State;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Actuator implements Serializable {
    private final String model;
    private final String actuator;
    private final String system_id;
    private final String request_type;
    private final int min_value;
    private final int max_value;
    private final String unit_of_measure;
    private final String data_type;
    private final String tableName;
    private State state;


    public static class Builder {
        private final String model;
        private final String actuator;
        private final String system_id;
        private String request_type = "unknown";        // "none val"
        private int min_value = -1;                     // none = -1
        private int max_value = -1;                     // none = -1
        private String unit_of_measure = "unknown";     // "none val"
        private String data_type = "unknown";           // "none val"
        private String tableName = "unknown";

        public Builder(String model, String actuator, String system_id) {
            this.model = model;
            this.actuator = actuator;
            this.system_id = system_id;
        }

        public Builder requestType(String val) {
            request_type = val;
            return this;
        }

        public Builder minValue(int val) {
            min_value = val;
            return this;
        }

        public Builder maxValue(int val) {
            max_value = val;
            return this;
        }

        public Builder unitOfMeasure(String val) {
            unit_of_measure = val;
            return this;
        }

        public Builder dataType(String val) {
            data_type = val;
            return this;
        }
        public Builder tableName(String val){
            this.tableName=val;
            return this;
        }

        public Actuator build() {
            return new Actuator(this);
        }
    }

    public Actuator(Builder builder) {
        this.model = builder.model;
        this.actuator = builder.actuator;
        this.system_id = builder.system_id;
        this.request_type = builder.request_type;
        this.min_value = builder.min_value;
        this.max_value = builder.max_value;
        this.unit_of_measure = builder.unit_of_measure;
        this.data_type = builder.data_type;
        this.tableName = builder.tableName;
        this.state = new State(0, new String[]{"0"});
    }

    public void setState(State val) {
        this.state = val;
    }
    public void setState(long val1,String ... val2){
        this.state.setTimestamp(val1);
        this.state.setStatus(val2);
    }
    public long getStateTime(){return this.state.getTimestamp();}
}
