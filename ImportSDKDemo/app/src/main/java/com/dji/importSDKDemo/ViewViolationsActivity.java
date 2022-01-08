package com.dji.importSDKDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dji.importSDKDemo.violations_management.Violation;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewViolationsActivity extends AppCompatActivity {
    private ArrayList<Violation> violations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_violations);
        violations = new ArrayList<Violation>();
        getViolationsFromDB();
        showViolations();
    }

    public void showViolations(){
        TextView violationsView = (TextView) findViewById(R.id.violations);
        violationsView.setText("");
        if(!violations.isEmpty()){
            for(Violation violation : violations){
                violationsView.append("Zone type: " + violation.ZoneType +
                        "; Zone number: " + violation.ZoneNumber +
                        "; Date: " + violation.Date +
                        "; Time: " + violation.Time +
                        "\n\n");
            }
        }else{
            violationsView.setText("You don`t have any violations");
        }
    }

    public void getViolationsFromDB(){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("droneApp.db",
                MODE_PRIVATE,
                null);
        Cursor query = db.rawQuery("SELECT * FROM violations;",
                null);

        while(query.moveToNext()){
            String zoneType = query.getString(0);
            String zoneNumber = query.getString(1);
            String date = query.getString(2);
            String time = query.getString(3);

            Violation violation = new Violation(zoneType, zoneNumber, date, time);
            violations.add(violation);
        }

        query.close();
        db.close();
    }
}