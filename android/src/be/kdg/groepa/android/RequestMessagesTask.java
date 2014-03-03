package be.kdg.groepa.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Tim on 28/02/14.
 */
public class RequestMessagesTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private AsyncResponse delegate=null;

    private String username;

    public RequestMessagesTask(Context context, AsyncResponse response){
        this.context = context;
        this.delegate = response;
    }

    @Override
    protected String doInBackground(Void... voids) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences privPref = context.getSharedPreferences("CarpoolPreferences",Context.MODE_PRIVATE);

        PreferenceManager.setDefaultValues(this.context, R.xml.preferences, false);
        String serverAddr = preferences.getString("carpoolServer","127.0.0.1:8080");
        int userId = privPref.getInt("UserId", -1);
        String url = "http://"+serverAddr+"/BackEnd/authorized/textmessage/get/"+userId;
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", "Token="+privPref.getString("Token", ""));
        //httpPost

        // Execute HTTP Post Request
        String responseString = null;

        try {
            response = httpclient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            Log.e("IOExc at SendMessage",e.getMessage());
        }
        return responseString;
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(result);
    }
}
