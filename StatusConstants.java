package com.example.HRMS.utils;

import com.example.HRMS.model.Status;

public class StatusConstants {

    public static final String success_code = "200";
    public static final String success_header = "Success";
    public static final String success_description = "The request was successfull.";

    public static final Status success() {
        return new Status(success_code, success_header, success_description);
    }


}
