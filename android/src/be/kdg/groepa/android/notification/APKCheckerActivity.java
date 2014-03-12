package be.kdg.groepa.android.notification;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import javax.naming.NameNotFoundException;

/**
 * Created by Tim on 5/03/14.
 */
public class APKCheckerActivity extends Activity implements AsyncResponse{
    private GoogleCloudMessaging gcm;
    private String regId;


    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);

        setContentView(R.layout.main);
        if(checkPlayServices()){
            this.gcm = GoogleCloudMessaging.getInstance(this);
            this.regId = getRegistrationId(getApplicationContext());
        if(regId.isEmpty()){
            registerInBackground();
        }
        } else  {
            Log.i("ServicesAPKError", "No valid Google Play Services APK found");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        checkPlayServices();
    }


    @Override
    public void processFinish(String output) {

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();
            } else {
                Log.i("NotSupported", "This device is not supported");
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context){
        SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString("registration_id", "");
        if(registrationId.isEmpty()){
            Log.i("RegistrationFindError", "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt("appVersion", Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion){
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context){
        return getSharedPreferences(APKCheckerActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context){
            PackageInfo info = null;
            try {
                info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return info.versionCode;
    }
}
