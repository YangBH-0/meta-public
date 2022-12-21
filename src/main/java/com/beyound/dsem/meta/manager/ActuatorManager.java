package com.beyound.dsem.meta.manager;

import java.util.Queue;
// mqtt message process class
public class ActuatorManager {
    private  final static ActuatorManager actuatorManager = new ActuatorManager();
    private Queue<Object> queue;
    public static ActuatorManager getInstance(){
        return actuatorManager;
    }
    public void addData(Object arg){
        queue.add(arg);
    }
    public boolean tasking(){
        try{
            while(queue.isEmpty()){

            }
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
