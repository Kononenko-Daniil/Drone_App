package com.dji.importSDKDemo.zones_management;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Polygon;

public class ZoneCircle {
    public Circle ZoneCircle;
    public String Type;
    public String Number;
    public boolean InZone = false;

    public ZoneCircle(Circle zoneCircle,
                       String type,
                       String number){
        ZoneCircle = zoneCircle;
        Type = type;
        Number = number;
    }
}
