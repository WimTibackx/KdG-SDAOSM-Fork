package be.kdg.groepa.android.task;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.LoginActivity;
import be.kdg.groepa.android.R;
import be.kdg.groepa.android.SendMessageActivity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 5/03/14.
 */
public class LogOutTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private AsyncResponse delegate=null;

    public LogOutTask(Context context, AsyncResponse asyncResponse) {
        super();
        this.context = context;
        this.delegate = asyncResponse;
    }

    @Override
    protected String doInBackground(Void... v)
    {
        System.out.println("CONSOLE -- STARTING LOGOUTTASK");
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;

        SharedPreferences privPref = context.getSharedPreferences("CarpoolPreferences",Context.MODE_PRIVATE);
        System.out.println("CONSOLE -- LOGOUT -- GOT SHAREDPREFERENCES");
        SharedPreferences.Editor privPrefEditor = privPref.edit();
        privPrefEditor.remove("Token");
        privPrefEditor.remove("UserId");
        privPrefEditor.remove("Username");
        System.out.println("CONSOLE -- LOGOUT -- DONE EDITING SHAREDPREFERENCES");

        return "Logged out";
    }

    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        Intent goToMyActivity = new Intent(context, LoginActivity.class);
        goToMyActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(goToMyActivity);
}
}
