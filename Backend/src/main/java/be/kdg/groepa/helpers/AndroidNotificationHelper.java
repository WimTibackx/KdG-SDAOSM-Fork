package be.kdg.groepa.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tim on 6/03/14.
 */
public class AndroidNotificationHelper {
    private final static String server_address = "https://android.googleapis.com/gcm/send";
    private final static String authorizationKey = "AIzaSyChKxGqQ2UnMZvyHn73SRVL7fqCwGx3-f0";

    public static void sendNotificationToDevice(String regId, String message) throws Exception {
        JSONObject json = new JSONObject();
        JSONArray data = new JSONArray();
        JSONObject dataMsg = new JSONObject();
        dataMsg.put("msg", message);
        data.put(dataMsg);
        json.put("registration_id", regId);
        json.put("data", data);

        URL url = null;
        HttpsURLConnection conn = null;
        String result;
        url = new URL(server_address);
        conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", authorizationKey);
        conn.setDoOutput(true);

        OutputStream output = conn.getOutputStream();
        output.write(json.toString().getBytes());
        output.close();

        conn.connect();

        int responsecode = conn.getResponseCode();
        System.out.println(responsecode);


    }
}
