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


    @Override
    public void onCreate(Bundle savedInstances) {
        finish();
        System.out.println("CONSOLE -- CREATING GCMACTIVITY");
        startService(new Intent(GoogleMessageActivity.this, BackgroundService.class));
        System.out.println("CONSOLE -- GCMACTIVITY STARTED");
        super.onCreate(savedInstances);
        this.context = getApplicationContext();
        if (checkPlayServices()) {
            System.out.println("CONSOLE -- CHECKPLAYSERVICES SUCCESS. NOW GETTING INSTANCE OF GCM");
            this.gcm = GoogleCloudMessaging.getInstance(this);
            System.out.println("CONSOLE -- GCM -- GETTING REGISTRATIONID");
            this.regId = getRegistrationId(context);
            System.out.println("CONSOLE -- REGISTRATION ID: " + regId);
            // Commented to test registering  if (regId.isEmpty()) {
                System.out.println("CONSOLE -- REGISTRATIONID WAS EMPTY. REGISTERING IN BACKGROUND");
                registerInBackground();
            // }
        /*} else {
            Log.i("ServicesAPKError", "No valid Google Play Services APK found");         */
        }
    }

    @Override
    public void onResume() {
        System.out.println("CONSOLE -- GCM -- RESUMING");
        super.onResume();
        checkPlayServices();
    }


    @Override
    public void processFinish(String output) {
        System.out.println("CONSOLE -- GCM -- PROCESSING FINISH");

    }

    // Checks if the device is compatible with Google Play Services
    private boolean checkPlayServices() {
        System.out.println("CONSOLE -- CHECKING PLAY SERVICES");
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();

            } else {
                Log.i("NotSupported", "This device is not supported");
            }
            return false;
        }
        System.out.println("CONSOLE -- GCM -- PLAY SERVICES SUCCESS");
        return true;
    }

    // Get the app's registrationId from preferences
    private String getRegistrationId(Context context) {
        System.out.println("CONSOLE -- GCM -- EXECUTING GETREGISTRATIONID");
        String registrationId = PreferenceManager.getDefaultSharedPreferences(this).getString("registration_id", "");
        System.out.println("CONSOLE -- GCM -- FOUND GETREGISTRATIONID: " + registrationId);
        if (registrationId.isEmpty()) {
            Log.i("RegistrationFindError", "Registration not found.");
            return "";
        }
        int registeredVersion = PreferenceManager.getDefaultSharedPreferences(this).getInt("app_version", 0);
        int currentVersion = getAppVersion(context);
        System.out.println("CURRENT VERSION: " + currentVersion + " // REGISTERED VERSION: " + registeredVersion);
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
        System.out.println("CONSOLE -- REGISTRING IN BACKGROUND");
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
                    System.out.println("CONSOLE -- GCM -- ERROR OCCURED AT REGISTERING IN BACKGROUND");
                    System.out.println("CONSOLE -- ERROR -- IOEXCEPTION: " + e.getMessage());
                    msg = "An error occured: " +
                            e.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        System.out.println("CONSOLE -- GCM -- SENDING REGISTRATION ID TO BACKGROUND");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences privPref = context.getSharedPreferences("CarpoolPreferences", Context.MODE_PRIVATE);

        PreferenceManager.setDefaultValues(this.context, R.xml.preferences, false);
        String serverAddr = preferences.getString("carpoolServer", "");
        String username = privPref.getString("Username", "");
        String url = "http://" + serverAddr + "/BackEnd/authorized/user/registerandroid";
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        System.out.println("URL: " + url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", regId);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Cookie", "Token=" + privPref.getString("Token", ""));
        System.out.println("SENDING REGISTRATION -- COOKIE: " + privPref.getString("Token", ""));
        System.out.println("SENDING REGISTRATION -- JSON: " + jsonObject.toString());

        try {
            httpPost.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
            System.out.println("REGISTERTOBG: JSON: " + jsonObject.toString());
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("STATUSLINE OK");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                Log.i("Success", out.toString());
            } else {
                //Closes the connection.
                System.out.println("STATUSLINE REGISTERTOBG NOT OK ERROR: " + statusLine.getReasonPhrase());
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            Log.e("IOExc at SendMessage", e.getMessage());
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        System.out.println("CONSOLE -- GCM -- STORING REGISTRATIONID " + regId + " IN PREFERENCES");
        int appVersion = getAppVersion(context);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("registration_id",
                regId).commit();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("app_version",
                appVersion).commit();
    }
}
