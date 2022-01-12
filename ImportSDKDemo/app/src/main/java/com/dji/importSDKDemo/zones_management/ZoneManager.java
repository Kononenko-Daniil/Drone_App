package com.dji.importSDKDemo.zones_management;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class ZoneManager {
    public ZonePolygon[] zonesPolygon;
    public ZoneCircle[] zonesCircle;
    public GoogleMap googleMap;

    public ZoneManager(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public void setZoneArrays(){
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
                        false),
                new ZonePolygon(
                        googleMap.addPolygon(new PolygonOptions()
                                .add(new LatLng(54.000550,27.438888),
                                        new LatLng(53.977497,27.509998),
                                        new LatLng(53.925557, 27.594717),
                                        new LatLng(53.889441, 27.580003),
                                        new LatLng(53.884995, 27.549162),
                                        new LatLng(53.903335, 27.524713),
                                        new LatLng(53.917504, 27.518882),
                                        new LatLng(53.943613,27.431945),
                                        new LatLng(53.983608, 27.408050))),
                        "RED",
                        "UMP 175",
                        false)
        };

        zonesCircle = new ZoneCircle[]{
                new ZoneCircle(
                        googleMap.addCircle(new CircleOptions()
                                .center(new LatLng(52.039868, 29.252625))
                                .radius(40)),
                        "YELLOW",
                        "4",
                        false),
                new ZoneCircle(
                        googleMap.addCircle(new CircleOptions()
                                .center(new LatLng(52.042759, 29.252559))
                                .radius(40)),
                        "ORANGE",
                        "4",
                        false),
        };
    }

    public void setZoneStyle(){
        ZoneStyle zonePolygonStyle;
        ZoneStyle zoneCircleStyle;

        for(ZonePolygon zonePolygon : zonesPolygon){
            zonePolygonStyle = choseZoneStyle(zonePolygon.Type);
            zonePolygon.ZonePolygon.setFillColor(zonePolygonStyle.fillColor);
            zonePolygon.ZonePolygon.setStrokeColor(zonePolygonStyle.strokeColor);
            zonePolygon.ZonePolygon.setStrokeWidth(zonePolygonStyle.strokeWidth);
        }
        for(ZoneCircle zoneCircle : zonesCircle){
            zoneCircleStyle = choseZoneStyle(zoneCircle.Type);
            zoneCircle.ZoneCircle.setFillColor(zoneCircleStyle.fillColor);
            zoneCircle.ZoneCircle.setStrokeColor(zoneCircleStyle.strokeColor);
            zoneCircle.ZoneCircle.setStrokeWidth(zoneCircleStyle.strokeWidth);
        }
    }

    public ZoneStyle choseZoneStyle(String zoneType){
        int fillColor = 0;
        int strokeColor = 0;
        float strokeWidth = 3;
        switch(zoneType){
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
        ZoneStyle zoneStyle = new ZoneStyle(fillColor, strokeColor, strokeWidth);

        return zoneStyle;
    }
}
