package com.beyound.dsem.meta.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JsonUtils {
    private static final Gson gson = new Gson();
    private static JsonUtils instance;

    public static JsonUtils getInstance(){
        if(instance==null){
            instance = new JsonUtils();
        }
        return instance;
    }
    public JsonNode getJson(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File from = new File(filename);
        return mapper.readTree(from);
    }

    public static String createActuatorData(String system_id, HashMap<String,String> data){
        StringBuilder stringBuilder = new StringBuilder();
        JsonObject container= new JsonObject();
        container.addProperty("systemId",system_id);
        container.addProperty("type","command");
        JsonObject commands=new JsonObject();
        for (String key: data.keySet()){
            commands.addProperty(key,data.get(key));
        }
        container.add("command",commands);
        return container.toString();
    }

}
