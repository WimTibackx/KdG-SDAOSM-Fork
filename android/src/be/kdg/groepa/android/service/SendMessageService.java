package be.kdg.groepa.android.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.EditText;
import be.kdg.groepa.android.R;
import be.kdg.groepa.android.SendMessageActivity;
import be.kdg.groepa.android.notification.GcmBroadcastReceiver;
import be.kdg.groepa.android.task.SendMessageTask;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Tim on 14/03/14.
 */
public class SendMessageService extends IntentService {

    public SendMessageService() {
        super("SendMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        SharedPreferences privPref = getApplicationContext().getSharedPreferences("CarpoolPreferences", MODE_PRIVATE);
        String senderUsername = privPref.getString("Username", "");
        SendMessageTask task = new SendMessageTask(senderUsername, extras.getString("receiverUsername"), extras.getString("messageSubject"), extras.getString("messageBody"), getApplicationContext());
        task.execute();
    }
}
