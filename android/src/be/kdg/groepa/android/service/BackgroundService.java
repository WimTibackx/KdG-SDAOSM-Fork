package be.kdg.groepa.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;

/**
 * Created by Tim on 6/03/14.
 */
// Allow activities to be run on the background.
public class BackgroundService extends Service {
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        BackgroundService getService(){
            return BackgroundService.this;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
