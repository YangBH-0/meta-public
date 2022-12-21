package com.beyound.dsem.meta.mysql.DTO;

import com.beyound.dsem.meta.Object.data.State;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Sensor {
    private final String model;
    private final String sensor;
    private final String system_id;
    private int min_value;
    private int max_value;
    private String unit_of_measure;
    private String data_type;
    private String table_name;
    private State state;

    public void setState(State state) {
        this.state =state;
    }

    public void setState(long val1,String ... val2){
        this.state.setTimestamp(val1);
        this.state.setStatus(val2);
    }
    public long getStateTime(){return this.state.getTimestamp();}

    @Override
    public String toString() {
        StringBuilder stringBuilder =new StringBuilder();
        stringBuilder.append("model : "+this.model+"\n");
        stringBuilder.append("sensor : "+this.sensor+"\n");
        stringBuilder.append("system_id : "+this.system_id+"\n");
        if(state != null)
            stringBuilder.append(this.state.toString());
        return stringBuilder.toString();
    }
}
