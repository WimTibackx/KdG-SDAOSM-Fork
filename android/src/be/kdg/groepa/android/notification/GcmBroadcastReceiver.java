package be.kdg.groepa.android.notification;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.google.android.gms.*;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Tim on 5/03/14.
 */
/*
    The GcmBroadcastReceiver class is the class that will process any incoming GoogleMessages.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                WakeLocker.acquire(context);
                ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
                setResultCode(Activity.RESULT_OK);
                WakeLocker.release();
            }
}
