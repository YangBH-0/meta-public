package com.beyound.dsem.meta.mqtt;


import com.beyound.dsem.meta.mysql.DTO.Actuator;
import com.beyound.dsem.meta.Meta;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class MinecraftMqttClient implements MqttCallback {
    private static MinecraftMqttClient Instance;
    private static MqttClient client;
    private Consumer<HashMap<Object,Object>> FNC = null; // 메시지 도착 후 응답하는 함수
    private Consumer<HashMap<Object,Object>> FNC2 = null; // 커넥션이 끊 긴 후 응답하는 함수
    private Consumer<HashMap<Object,Object>> FNC3 = null; // 전송이 완료된 이후 응답하는 함수
   // MqttClient client;
    private Logger log;
    public MinecraftMqttClient getInstance(){
        if(Instance==null){
            new MinecraftMqttClient(Meta.getInstance().getLogger());
        }
        return Instance;}

    public MinecraftMqttClient(Logger log){
        this.log=log;
    }

    public MinecraftMqttClient(Consumer<HashMap<Object,Object>> fnc) {
        this.FNC = fnc;
    }
    public MinecraftMqttClient init(String serverURI,String clientId) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setKeepAliveInterval(30);
        try{
            client = new MqttClient(serverURI,clientId);
            client.setCallback(this);
            client.connect(options);
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }
    public MinecraftMqttClient init(String userName, String password, String serverURI,String clientId){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setKeepAliveInterval(30);
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        try{
            client = new MqttClient(serverURI,clientId);
            client.setCallback(this);
            client.connect(options);
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Lost Connection." + cause.getCause());

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception { // 엑추에이터 상태 변화 수신
        String str = new String(message.getPayload());
        String[] topics = topic.split("/");
        if(FNC!=null){
            HashMap<Object,Object> result = new HashMap<>();
            //result.put(topic,new String(message.getPayload(),"UTF-8"));
            result.put("topic",topic);
            result.put("message",new String(message.getPayload(),"UTF-8"));
            FNC.accept(result);
        }
        System.out.println("message check");
        //Actuator a = JsonToObject.getInstance().fromJson(str,Actuator.class);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
    public void close() throws MqttException {
        client.disconnect();
    }
    public void sendMessage(String topic, String message) throws MqttException { // 엑추에이터 상태 변화 전달
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes(StandardCharsets.UTF_8));
        client.publish(topic,mqttMessage);
    }
    public boolean subscribe(String... topics) {
        try{
            if(topics!=null){
                for(String topic : topics){
                    client.subscribe(topic,0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
