package com.dji.importSDKDemo.zones_management;

import android.content.Context;

import com.dji.importSDKDemo.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JsonZoneParser {
    public static GoogleMap googleMap;
    public static ArrayList<ZoneCircle> circleZones;
    public static ArrayList<ZonePolygon> polygonZones;

    public static void readZonesJSONFiles(Context context) throws IOException, JSONException {
        String jsonTextCircleGrey = readText(context, R.raw.circle);
        String jsonTextPolygonGrey = readText(context, R.raw.polygons);

        setCircleGreyList(jsonTextCircleGrey);
        setPolygonGreyList(jsonTextPolygonGrey);
    }

    private static void setCircleGreyList(String jsonText) throws IOException, JSONException {
        JSONObject jsonRoot = new JSONObject(jsonText);

        JSONArray circles = jsonRoot.getJSONArray("circles");
        getZoneCircleAndAddToList(circles, "circles");
        JSONArray circlesDanger = jsonRoot.getJSONArray("circlesDanger");
        getZoneCircleAndAddToList(circlesDanger, "circlesDanger");
        JSONArray circlesForbidden = jsonRoot.getJSONArray("circlesForbidden");
        getZoneCircleAndAddToList(circlesForbidden, "circlesForbidden");
    }

    private static void getZoneCircleAndAddToList(JSONArray circles, String zonesType) throws JSONException{
        int listLength = circles.length();
        String type;
        switch(zonesType){
            case "circles":
                type = "BANNED";
                break;
            case "circlesDanger":
                type = "DANGER";
                break;
            case "circlesForbidden":
                type = "FORBIDDEN";
                break;
            default:
                type = "";
                break;
        }

        for (int i = 0; i < listLength; i++) {
            String zoneNumber = Integer.toString(circles.getJSONObject(i).getInt("id"));
            Double radius = circles.getJSONObject(i).getDouble("radius");
            Double lat = circles.getJSONObject(i).getJSONObject("coordinates").getDouble("lat");
            Double lng = circles.getJSONObject(i).getJSONObject("coordinates").getDouble("lng");

            Circle circle = googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lng))
                    .radius(radius * 1000));
            ZoneCircle zoneCircle = new ZoneCircle(circle, type, zoneNumber);
            circleZones.add(zoneCircle);
        }
    }

    private static void setPolygonGreyList(String jsonText) throws IOException, JSONException {
        JSONObject jsonRoot = new JSONObject(jsonText);

        JSONArray polygons = jsonRoot.getJSONArray("polygons");
        getZonePolygonAndAddToList(polygons, "polygons");
        JSONArray polygonsDanger = jsonRoot.getJSONArray("polygonsDanger");
        getZonePolygonAndAddToList(polygonsDanger, "polygonsDanger");
        JSONArray polygonsForbidden = jsonRoot.getJSONArray("polygonsForbidden");
        getZonePolygonAndAddToList(polygonsForbidden, "polygonsForbidden");
    }

    private static void getZonePolygonAndAddToList(JSONArray polygons, String zonesType) throws JSONException {
        int listLength;
        String type;
        switch(zonesType){
            case "polygons":
                listLength = 105;
                type = "BANNED";
                break;
            case "polygonsDanger":
                listLength = polygons.length();
                type = "DANGER";
                break;
            case "polygonsForbidden":
                listLength = polygons.length();
                type = "FORBIDDEN";
                break;
            default:
                listLength = 0;
                type = "";
                break;
        }

        for(int i = 0; i < listLength; i++){
            String zoneNumber = Integer.toString(polygons.getJSONObject(i).getInt("id"));

            JSONArray coordinates = polygons.getJSONObject(i).getJSONArray("coordinates");
            PolygonOptions polygonOptions = new PolygonOptions();

            for(int j = 0; j < coordinates.length(); j++){
                LatLng point = new LatLng(coordinates.getJSONArray(j).getDouble(0), coordinates.getJSONArray(j).getDouble(1));
                polygonOptions.add(point);
            }

            Polygon polygon = googleMap.addPolygon(polygonOptions);
            ZonePolygon zonePolygon = new ZonePolygon(polygon, type, zoneNumber);
            polygonZones.add(zonePolygon);
        }
    }

    private static String readText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        StringBuilder sb= new StringBuilder();
        String s= null;
        while((  s = br.readLine())!=null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
