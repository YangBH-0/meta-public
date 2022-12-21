package com.beyound.dsem.meta.mysql.DTO;

import lombok.Getter;

@Getter
public class Module {
    private final String type;
    private final String property;
    private final String modelName;
    private final String unitOfMeasure;

    public Module(String type,String property, String modelName) {
        this.type = type;
        this.modelName = modelName;
        this.property=property;
        unitOfMeasure="unknown"; // "none val"
    }

    public Module(String type, String property,String model, String unitOfMeasure) {
        this.type = type;
        this.modelName = model;
        this.property = property;
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public String toString() {
        String str = "model type: "+this.type+
                ", model property: "+this.property+
                ", model name: "+this.modelName+
                ", model UoM"+this.unitOfMeasure;
        return str;
    }
}
