package com.dji.importSDKDemo.zones_management;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class ZoneManager {
    public ZonePolygon[] zonesPolygon;
    public GoogleMap googleMap;

    public ZoneManager(GoogleMap googleMap){
        this.googleMap = googleMap;

    }

    public void setZonesArray(){
        zonesPolygon = new ZonePolygon[]{
                new ZonePolygon(
                        googleMap.addPolygon(new PolygonOptions()
                                .add(new LatLng(52.040351,29.250383),
                                        new LatLng(52.041945,29.250313),
                                        new LatLng(52.042195,29.252475),
                                        new LatLng(52.040549,29.251928))),
                        "GREY",
                        "1",
                        false),
                new ZonePolygon(
                        googleMap.addPolygon(new PolygonOptions()
                                .add(new LatLng(52.040387,29.253845),
                                        new LatLng(52.041621,  29.254344),
                                        new LatLng(52.041354,  29.256704))),
                        "RED",
                        "2",
                        false)
        };
    }

    public void setZoneStyle(){
        int fillColor = 0;
        int strokeColor = 0;
        float strokeWidth = 3;
        for(ZonePolygon zonePolygon : zonesPolygon){
            switch(zonePolygon.Type){
                case "RED":
                    fillColor = Color.argb(80, 255, 0, 0);
                    strokeColor = Color.argb(220, 255, 0, 0);
                    break;
                case "ORANGE":
                    fillColor = Color.argb(80, 255, 138, 0);
                    strokeColor = Color.argb(220, 255, 138, 0);
                    break;
                case "YELLOW":
                    fillColor = Color.argb(80, 250, 255, 0);
                    strokeColor = Color.argb(220, 250, 255, 0);
                    break;
                case "GREY":
                    fillColor = Color.argb(80, 0, 0, 0);
                    strokeColor = Color.argb(220, 0, 0, 0);
                    break;
                default:
                    break;
            }
            zonePolygon.ZonePolygon.setFillColor(fillColor);
            zonePolygon.ZonePolygon.setStrokeColor(strokeColor);
            zonePolygon.ZonePolygon.setStrokeWidth(strokeWidth);
        }
    }
}
