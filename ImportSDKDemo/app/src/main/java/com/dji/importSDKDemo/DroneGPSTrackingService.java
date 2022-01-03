package com.dji.importSDKDemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DroneGPSTrackingService extends Service {
    public DroneGPSTrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}