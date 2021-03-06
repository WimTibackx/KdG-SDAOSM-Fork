package be.kdg.groepa.android.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.R;
import be.kdg.groepa.android.service.BackgroundService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Tim on 5/03/14.
 */
/*
    The GoogleMessageActivity-class is the central class for registering the device for GMS.
    It checks if the device is compatible with GMS and if it has a Registration Id.
    If it does not have a Registration Id yet, it will register the device.
 */
public class GoogleMessageActivity extends Activity implements AsyncResponse {
    private GoogleCloudMessaging gcm;
    private String regId;
    private Context context;
    private static final String reg_id = "287216615243";


    @Override
    public void onCreate(Bundle savedInstances) {
        finish();
        startService(new Intent(GoogleMessageActivity.this, BackgroundService.class));
        super.onCreate(savedInstances);
        this.context = getApplicationContext();
        if (checkPlayServices()) {
            this.gcm = GoogleCloudMessaging.getInstance(this);
            this.regId = getRegistrationId(context);
            if (regId.isEmpty()) {
                registerInBackground();
            }
        } else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }


    @Override
    public void processFinish(String output) {

    }

    // Checks if the device is compatible with Google Play Services
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();

            } else {
            }
            return false;
        }
        return true;
    }

    // Get the app's registrationId from preferences
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(reg_id, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = prefs.getInt("app_version", 0);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    // Get the GCM preferences from shared preferences
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(GoogleMessageActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    // Get the app's version
    private static int getAppVersion(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode;
    }


    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object[] objects) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = new GoogleCloudMessaging().getInstance(context);
                    }
                    regId = gcm.register("287216615243");
                    msg = "Succesfully registered with id " + regId;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regId);

                } catch (IOException e) {
                    msg = "An error occured: " +
                            e.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences privPref = context.getSharedPreferences("CarpoolPreferences", Context.MODE_PRIVATE);

        PreferenceManager.setDefaultValues(this.context, R.xml.preferences, false);
        String serverAddr = preferences.getString("carpoolServer", "");
        String username = privPref.getString("Username", "");
        String url = "http://" + serverAddr + "/BackEnd/authorized/user/registerandroid";
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", regId);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Cookie", "Token=" + privPref.getString("Token", ""));

        try {
            httpPost.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        int appVersion = getAppVersion(context);
        editor.putString("registration_id", regId);
        editor.putInt("app_version", appVersion);
        editor.commit();
    }
}
