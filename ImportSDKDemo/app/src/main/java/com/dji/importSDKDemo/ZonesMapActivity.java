package com.dji.importSDKDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dji.importSDKDemo.zones_management.ZoneManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class ZonesMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private ZoneManager zoneManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zones_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.zones_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        if(gMap == null){
            gMap = googleMap;
        }
        LatLng countryCenter = new LatLng(53.896331, 27.565802);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(countryCenter, 6f));
        zoneManager = new ZoneManager(gMap, this);
        zoneManager.setZoneArrays();
        zoneManager.setZoneStyle();
    }

    public void onBackClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}