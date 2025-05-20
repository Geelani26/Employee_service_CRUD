package com.example.HRMS.utils;


import com.example.HRMS.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstantMessage {

     public static final String ADD = "Details Added";
     public static final String UPDATED = "Details Updated";
     public static final String DELETED = "Details DELETED";
     public static final String GET = "Details Retrieved";


    public static Status ADD() {
        return null;
    }
}
