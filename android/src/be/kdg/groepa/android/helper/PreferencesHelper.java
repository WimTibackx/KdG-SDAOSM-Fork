package be.kdg.groepa.android.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import be.kdg.groepa.android.R;
import be.kdg.groepa.model.LocalUserData;

/**
 * Created by delltvgateway on 3/3/14.
 */
public class PreferencesHelper {

    public static String getServerAddr(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        return preferences.getString("carpoolServer","127.0.0.1:8080");
    }

    public static LocalUserData getLocalUser(Context context) {
        SharedPreferences privPref = context.getSharedPreferences("CarpoolPreferences", Context.MODE_PRIVATE);
        final String token = privPref.getString("Token", "");
        final int userId = privPref.getInt("UserId", -1);
        return new LocalUserData(userId, token);
    }
}