package com.beyound.dsem.meta.Object.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class State {
    long timestamp;
    String[] status;

    public void setState(long val1,String... val2) {
        this.timestamp = val1;
        this.status = val2;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder= new StringBuilder();
        stringBuilder.append("time : "+this.timestamp+"\n");
        for(String str:status){
            stringBuilder.append("val : "+str+"\n");
        }
        return stringBuilder.toString();
    }
}
