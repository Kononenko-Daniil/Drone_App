package com.dji.importSDKDemo.zones_management;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class ZonePolygon {
    public Polygon ZonePolygon;
    public String Type;
    public String Number;
    public boolean InZone = false;

    public ZonePolygon(Polygon zonePolygon,
                       String type,
                       String number){
        ZonePolygon = zonePolygon;
        Type = type;
        Number = number;
    }
}
