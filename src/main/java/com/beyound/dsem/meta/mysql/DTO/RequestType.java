package com.beyound.dsem.meta.mysql.DTO;

import lombok.Getter;

@Getter
public enum RequestType {
    binary_number("binary"),
    ordered_values("ordered"),
    range_of_numbers("range");

    private final String val;
    RequestType(String str){
        val=str;
    }
}
