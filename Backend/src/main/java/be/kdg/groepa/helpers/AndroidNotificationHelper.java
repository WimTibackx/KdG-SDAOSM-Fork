package be.kdg.groepa.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.android.gcm.server.*;

/**
 * Created by Tim on 6/03/14.
 */
public class AndroidNotificationHelper {
    private final static String server_address = "https://android.googleapis.com/gcm/send";
    private final static String authorizationKey = "AIzaSyChKxGqQ2UnMZvyHn73SRVL7fqCwGx3-f0";
    private static int messageId = 1;

    public static void sendNotificationToDevice(String regId, String message) throws Exception {
        Sender sender = new Sender(authorizationKey);
        Message gcmmsg = new Message.Builder()
                .collapseKey("message")
                .timeToLive(3)
                .delayWhileIdle(true)
                .addData("message", message) //you can get this message on client side app
                .build();
        Result res = sender.send(gcmmsg, regId, 1);
    }
}
