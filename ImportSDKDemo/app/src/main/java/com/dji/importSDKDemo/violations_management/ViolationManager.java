package com.dji.importSDKDemo.violations_management;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ViolationManager {
    public ArrayList<Violation> violations;
    private Cursor query;

    public ViolationManager(Cursor query){
        this.query = query;
        violations = new ArrayList<Violation>();
    }

    public void getViolationsFromDB(){
        while(query.moveToNext()){
            String zoneType = query.getString(0);
            String zoneNumber = query.getString(1);
            String date = query.getString(2);
            String time = query.getString(3);

            Violation violation = new Violation(zoneType, zoneNumber, date, time);
            violations.add(violation);
        }
    }
}
