package com.dji.importSDKDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dji.importSDKDemo.violations_management.Violation;
import com.dji.importSDKDemo.zones_management.ZoneCircle;
import com.dji.importSDKDemo.zones_management.ZonePolygon;
import com.dji.importSDKDemo.zones_management.ZoneManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dji.common.flightcontroller.FlightControllerState;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Intent intent;
    private GoogleMap gMap;
    private double droneLocationLat = 181, droneLocationLng = 181;
    private double droneLocationAlt = 0;
    private Marker droneMarker = null;
    private FlightController mFlightController;
    private ZoneManager zoneManager;
    private SQLiteDatabase db;
    private boolean giveInZoneOnStartAttentionMessage = false;
    private DatabaseReference ref;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFlightController();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.zones_map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://droneappdata-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = database.getReference();

        db = getBaseContext().openOrCreateDatabase("droneApp.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS violations (zoneType TEXT, " +
                "zoneNumber TEXT, " +
                "date TEXT, " +
                "time Text)");
    }

    private void initFlightController() {

        BaseProduct product = DJIDemoApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }

        if (mFlightController != null) {
            mFlightController.setStateCallback(new FlightControllerState.Callback() {

                @Override
                public void onUpdate(FlightControllerState djiFlightControllerCurrentState) {
                    droneLocationLat = djiFlightControllerCurrentState.getAircraftLocation().getLatitude();
                    droneLocationLng = djiFlightControllerCurrentState.getAircraftLocation().getLongitude();
                    droneLocationAlt = djiFlightControllerCurrentState.getAircraftLocation().getAltitude();
                    checkDroneAltitude();
                    updateDroneLocation();
                }
            });
        }
    }

    public void checkDroneAltitude(){
        if(droneLocationAlt > 2){
            giveInZoneOnStartAttentionMessage = true;
        }
    }

    public static boolean validDroneGPSCoordinates(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    public void checkDroneGPSCoordinates(LatLng droneLocation){
        boolean inZone = false;
        String inZoneOnStartMessage = "Drone is in zone! Don`t fly higher than 2 meters!";
        for(ZonePolygon zonePolygon : zoneManager.zonesPolygon){
            if(!zonePolygon.InZone){
                inZone = PolyUtil.containsLocation(droneLocation,
                        zonePolygon.ZonePolygon.getPoints(), false);
                if(inZone){
                    if(droneLocationAlt < 2){
                        if(!giveInZoneOnStartAttentionMessage){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    inZoneOnStartMessage, Toast.LENGTH_LONG);
                            toast.show();
                            giveInZoneOnStartAttentionMessage = true;
                        }
                    }else{
                        zonePolygon.InZone = true;
                        String inZoneMessage = "Drone is in zone. Register violation.";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                inZoneMessage, Toast.LENGTH_SHORT);
                        toast.show();
                        registerViolation(zonePolygon.Type, zonePolygon.Number);
                        break;
                    }
                }
            }
        }
        if(!inZone){
            for(ZoneCircle zoneCircle : zoneManager.zonesCircle){
                if(!zoneCircle.InZone){
                    float[] distanceComputeResults = new float[1];
                    LatLng zoneCenter = zoneCircle.ZoneCircle.getCenter();
                    Location.distanceBetween(zoneCenter.latitude,
                            zoneCenter.longitude,
                            droneLocation.latitude,
                            droneLocation.longitude,
                            distanceComputeResults);
                    if(zoneCircle.ZoneCircle.getRadius() > distanceComputeResults[0])
                        inZone = true;
                    if(inZone){
                        if(droneLocationAlt < 2){
                            if(!giveInZoneOnStartAttentionMessage){
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        inZoneOnStartMessage, Toast.LENGTH_LONG);
                                toast.show();
                                giveInZoneOnStartAttentionMessage = true;
                            }
                        }else {
                            zoneCircle.InZone = true;

                            String inZoneMessage = "Drone is in zone. Register violation.";
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    inZoneMessage, Toast.LENGTH_SHORT);
                            toast.show();
                            registerViolation(zoneCircle.Type, zoneCircle.Number);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void registerViolation(String zoneType, String zoneNumber){
        String violationDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        String violationTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        db.execSQL("INSERT OR IGNORE INTO violations VALUES ('" +
                zoneType + "', '" +
                zoneNumber + "', '" +
                violationDate + "', '" +
                violationTime + "')");
        Violation violation = new Violation(zoneType, zoneNumber, violationDate, violationTime);
        ref.child("violations").push().setValue(violation);
    }

    private void updateDroneLocation(){
        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.aircraft));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (droneMarker != null) {
                    droneMarker.remove();
                }

                if (validDroneGPSCoordinates(droneLocationLat, droneLocationLng)) {
                    droneMarker = gMap.addMarker(markerOptions);
                    LatLng droneLocation = new LatLng(droneLocationLat, droneLocationLng);
                    checkDroneGPSCoordinates(droneLocation);

                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        if(gMap == null){
            gMap = googleMap;
        }
        LatLng countryCenter = new LatLng(53.896331, 27.565802);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(countryCenter, 5.5f));
        zoneManager = new ZoneManager(gMap, this);
        zoneManager.setZoneArrays();
        zoneManager.setZoneStyle();
    }

    public void OnButtonClick(View view){
        switch(view.getId()){
            case R.id.zones_map_button:
                intent = new Intent(this, ZonesMapActivity.class);
                break;
            case R.id.view_violations_button:
                intent = new Intent(this, ViewViolationsActivity.class);
                break;
            default:
                break;
        }

        startActivity(intent);
    }
}