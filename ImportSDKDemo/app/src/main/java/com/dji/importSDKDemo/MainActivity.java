package com.dji.importSDKDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dji.importSDKDemo.zones_management.Zone;
import com.dji.importSDKDemo.zones_management.ZoneManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import dji.common.flightcontroller.FlightControllerState;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Intent intent;
    private GoogleMap gMap;
    private Handler mHandler;
    private double droneLocationLat = 181, droneLocationLng = 181;
    private Marker droneMarker = null;
    private FlightController mFlightController;
    private Polygon polygon;
    private Circle circle;
    private ZoneManager zoneManager;
    private Zone[] zones;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DJIDemoApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.zones_map);
        mapFragment.getMapAsync(this);

        mHandler = new Handler(Looper.getMainLooper());
    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onProductConnectionChange();
        }
    };

    private void onProductConnectionChange()
    {
        initFlightController();
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
                    updateDroneLocation();
                }
            });
        }
    }

    public static boolean validDroneGPSCoordinates(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    public void checkDroneGPSCoordinates(LatLng droneLocation){
        boolean inZone = false;

        for(Zone zone : zoneManager.zones){
            if(!zone.InZone){
                inZone = PolyUtil.containsLocation(droneLocation,
                        zone.ZonePolygon.getPoints(), false);
                if(inZone){
                    zone.InZone = true;
                    String inZoneMessage = "Drone is in zone";
                    Toast toast = Toast.makeText(getApplicationContext(),
                            inZoneMessage, Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
            }
        }
    }

    private void updateDroneLocation(){
        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        //Create MarkerOptions object
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
        zoneManager = new ZoneManager(gMap);
        zoneManager.setZonesArray();
        zoneManager.setZoneStyle();
    }

    public void OnButtonClick(View view){
        switch(view.getId()){
            case R.id.go_fly_button:
                intent = new Intent(this, GoFlyActivity.class);
                break;
            default:
                break;
        }

        startActivity(intent);
    }
}