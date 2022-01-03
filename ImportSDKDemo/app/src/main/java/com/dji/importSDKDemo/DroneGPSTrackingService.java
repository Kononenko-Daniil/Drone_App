package com.dji.importSDKDemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class DroneGPSTrackingService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Service is running", Toast.LENGTH_SHORT);
        toast.show();

        return START_STICKY;
    }
}