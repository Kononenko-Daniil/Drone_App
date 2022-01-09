package com.dji.importSDKDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dji.importSDKDemo.violations_management.Violation;
import com.dji.importSDKDemo.violations_management.ViolationManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewViolationsActivity extends AppCompatActivity {
    private ViolationManager violationManager;
    private SQLiteDatabase db;
    private Cursor query;

    @Override
    public void onDestroy(){
        super.onDestroy();
        query.close();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_violations);
        db = getBaseContext().openOrCreateDatabase("droneApp.db",
                MODE_PRIVATE,
                null);
        query = db.rawQuery("SELECT * FROM violations;",
                null);
        violationManager = new ViolationManager(query);
        violationManager.getViolationsFromDB();
        showViolations();
    }

    public void showViolations(){
        TextView violationsView = (TextView) findViewById(R.id.violations);
        violationsView.setText("");
        if(!violationManager.violations.isEmpty()){
            int violationIndex = 1;
            for(Violation violation : violationManager.violations){
                violationsView.append(violationIndex + ". Zone type: " + violation.ZoneType +
                        "; Zone number: " + violation.ZoneNumber +
                        "; \nDate: " + violation.Date +
                        "; Time: " + violation.Time +
                        "\n\n");
                violationIndex++;
            }
        }else{
            violationsView.setText("You don`t have any violations");
        }
    }

    public void onBackClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}