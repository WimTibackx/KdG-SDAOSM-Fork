package be.kdg.groepa.android.notification;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;
import be.kdg.groepa.android.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Tim on 6/03/14.
 */
/*
    The GcmIntentService service receives a GoogleMessage and makes a notification from it.
 */
public class GcmIntentService extends IntentService {
    private NotificationManager manager;
    private static int notifId = 0;

    public GcmIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error", extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted message on server", extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification("Received message", extras);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String type, Bundle msg) {

        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, GoogleMessageActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Carpooling Notification")
                .setSmallIcon(R.drawable.mail)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg.getString("message")))
                .setContentText(msg.getString("message"));
        mBuilder.setContentIntent(contentIntent);
        manager.notify(notifId++, mBuilder.build());
        generateAlertDialog(msg.getString("message"));
    }

    private void generateAlertDialog(final String title) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("You have received a new Carpooling message.");
                builder.setIcon(R.drawable.mail);
                builder.setMessage("Title: " + title);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alert.show();
            }
        });
    }
}
