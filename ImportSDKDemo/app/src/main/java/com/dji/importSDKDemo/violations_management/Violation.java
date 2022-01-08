package com.dji.importSDKDemo.violations_management;

public class Violation {
    public String ZoneType;
    public String ZoneNumber;
    public String Date;
    public String Time;

    public Violation(String zoneType,
                     String zoneNumber,
                     String date,
                     String time) {
        ZoneType = zoneType;
        ZoneNumber = zoneNumber;
        Date = date;
        Time = time;
    }
}
