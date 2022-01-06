package com.dji.importSDKDemo.zones_management;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class Zone {
    public Polygon ZonePolygon;
    public String Type;
    public String Number;
    public boolean InZone;

    public Zone(Polygon zonePolygon,
                String type,
                String number,
                boolean inZone){
        ZonePolygon = zonePolygon;
        Type = type;
        Number = number;
        InZone = inZone;
    }
}
