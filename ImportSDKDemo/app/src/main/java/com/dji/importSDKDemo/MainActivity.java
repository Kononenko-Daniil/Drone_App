package com.dji.importSDKDemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnButtonCategoryClick(View view){
        switch(view.getId()){
            case R.id.go_fly_button:
                intent = new Intent(this, GoFlyActivity.class);
                break;
            case R.id.zones_map_button:
                intent = new Intent(this, ZonesMapActivity.class);
                break;
            case R.id.about_app_button:
                intent = new Intent(this, AboutAppActivity.class);
                break;
            default:
                break;
        }

        startActivity(intent);
    }
}