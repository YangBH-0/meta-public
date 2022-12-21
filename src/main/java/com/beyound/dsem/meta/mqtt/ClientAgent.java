package com.beyound.dsem.meta.mqtt;

import com.beyound.dsem.meta.Meta;
import com.beyound.dsem.meta.Object.entity.ActuatorObject;
import com.beyound.dsem.meta.Object.data.State;
import com.beyound.dsem.meta.mysql.DTO.Actuator;
import com.beyound.dsem.meta.registry.MetaRegistry;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.function.Consumer;

public class ClientAgent {
    private static final ClientAgent Instance = new ClientAgent();
    private static MinecraftMqttClient client;
    private static String uri;
    private static String id;
    private String setConnection;
    private String ip;
    private final Consumer<HashMap<Object, Object>> FNC; //

    public ClientAgent() {
        setConnection = "tcp://";
        ip = "203.234.62.115";
        id = "minecraft";
        uri = setConnection + ip;
        FNC = (arg) -> { // process if taken message
            arg.forEach((key, value) -> {
                Meta.getInstance().getLogger().info(String.format("대상, 키 -> %s, 값 -> %s ", key, value));
            });
            String[] sub_str = arg.get("topic").toString().split("/");
            String system_id = sub_str[0];
            System.out.println("system_id:" + system_id + "," + MetaRegistry.getInstance().checkActuatorSystemId(system_id));

            if (MetaRegistry.getInstance().checkActuatorSystemId(system_id)) {
                String value = arg.get("message").toString();
                System.out.println("checked");
                ActuatorObject actuatorObject = MetaRegistry.getInstance().getActuatorObjectBySystemId(system_id);
                Actuator actuator = MetaRegistry.getInstance().getActuatorBySystemId(system_id);
                switch (actuator.getRequest_type()) {
                    case ("binary") -> {
                        String status = value.split(":")[1];
                        State temp = new State(System.currentTimeMillis(),new String[]{status});
                        MetaRegistry.getInstance().changeActuatorState(actuatorObject,temp);
                    }
                    case ("ordered") -> { // 제어 정의 다시 해야함.
                    }
                    case ("range") -> {
                        String state = value.split(":")[1];
                        actuator.setState(System.currentTimeMillis(),state);
                        //MetaRegistry.getInstance().changeActuatorState(actuator);
                    }
                }
            }
        };
        client = new MinecraftMqttClient(FNC);
        getClient().init(uri, "test");
        //getClient().subscribe("test/#");
        System.out.println("init mqtt client");
    }

    public static ClientAgent getInstance() {
        return Instance;
    }

    public static MinecraftMqttClient getClient() {
        if (client == null) {
            client = client.getInstance();
            client.init(uri, id);
        }
        return client;
    }

    public static boolean sendActuate(Actuator a, String message) {
        String topic = a.getSystem_id() + "/COMMAND";
        try {
            client.getInstance().sendMessage(topic, message);
        } catch (MqttException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

}
