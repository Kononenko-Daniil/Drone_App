package com.dji.importSDKDemo.zones_management;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ZoneManager {
    public ArrayList<ZonePolygon> zonesPolygon;
    public ArrayList<ZoneCircle> zonesCircle;
    public GoogleMap googleMap;
    public Context context;

    public ZoneManager(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
    }

    public void setZoneArrays(){
        zonesPolygon = new ArrayList<ZonePolygon>();
        zonesCircle = new ArrayList<ZoneCircle>();
        JsonZoneParser.googleMap = googleMap;
        JsonZoneParser.polygonZones = zonesPolygon;
        JsonZoneParser.circleZones = zonesCircle;
        try {
            JsonZoneParser.readZonesJSONFiles(context);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
