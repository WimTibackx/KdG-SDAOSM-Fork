package be.kdg.groepa.android.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.R;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
 * Created by delltvgateway on 2/11/14.
 */
public class LoginRequestTask extends AsyncTask<Void, Void, String> {

    private String username;
    private String password;
    private Context context;
    private AsyncResponse delegate=null;
    private static boolean looped = false;

    public LoginRequestTask(String username, String password, Context context, AsyncResponse asyncResponse) {
        super();
        this.username = username;
        this.password = password;
        this.context = context;
        this.delegate = asyncResponse;
    }

    @Override
    protected String doInBackground(Void... v) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;

        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println("CONSOLE -- USERNAME: " + this.username);
            System.out.println("CONSOLE -- PASSWORD: " + this.password);
            jsonObject.put("username", this.username);
            jsonObject.put("password",this.password);
        } catch (JSONException e) {
            System.out.println("CONSOLE -- JSONEXCEPTION ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        List<NameValuePair> param = new ArrayList<>(1);
        param.add(new BasicNameValuePair("data",jsonObject.toString()));
        System.out.println("CONSOLE -- PARAMETERS ADDED");


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        System.out.println("CONSOLE --- GOT SHARED PREFS");
        // For mysterious reasons, threading errors seem to occur on hardware devices because the Looper is not prepared.
        // This serves as a workaround and prevents it being called twice.
        if(!looped){
            Looper.prepare();
            looped = true;
        }

        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);

        System.out.println("CONSOLE -- SET DEFAULT VALUES IN PREFMANAGER");
        String serverAddr = preferences.getString("carpoolServer","127.0.0.1:8080");

        String url = "http://"+serverAddr+"/BackEnd/login";
        System.out.println("CONSOLE -- URL: " + url);

        HttpPost httpPost = new HttpPost(url);
        System.out.println("CONSOLE -- HTTPPOST CREATED");
        //httpPost.


        // Execute HTTP Post Request

        try {
            httpPost.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
            System.out.println("CONSOLE -- EXECUTING HTTPPOST");
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                System.out.println("CONSOLE -- HTTPPOST OK");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                System.out.println("CONSOLE -- HTTPSTATUS NOT OK");
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            System.out.println("IOEXCEP AT LOGIN" + e.getMessage());
            return "IOException";
        }
        return responseString;
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(result);
    }
}
