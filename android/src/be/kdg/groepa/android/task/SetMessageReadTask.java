package be.kdg.groepa.android.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.R;
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
 * Created by Tim on 28/02/14.
 */
public class SetMessageReadTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private AsyncResponse delegate=null;

    private int id;

    public SetMessageReadTask(int id, Context context, AsyncResponse response){
        this.id = id;
        this.context = context;
        this.delegate = response;
    }

    @Override
    protected String doInBackground(Void... voids) {
        System.out.println("CONSOLE -- DOING SETMESSAGEREAD IN BACKGROUND");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences privPref = context.getSharedPreferences("CarpoolPreferences",Context.MODE_PRIVATE);

        PreferenceManager.setDefaultValues(this.context, R.xml.preferences, false);
        String serverAddr = preferences.getString("carpoolServer","127.0.0.1:8080");

        String url = "http://"+serverAddr+"/BackEnd/authorized/textmessage/read";
        System.out.println("CONSOLE -- SETMESSAGEREAD URL: " + url);
        // cookieManager.setCookie(url, "Token="+preferences.getString("Token", "127.0.0.1:8080"));
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("messageId", this.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = null;
        httpPost = new HttpPost(url);
        httpPost.setHeader("Cookie", "Token="+privPref.getString("Token", ""));
        //httpPost

        // Execute HTTP Post Request
        String responseString = null;

        try {
            httpPost.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
            System.out.println("CONSOLE -- SETMESSAGEREAD -- EXECUTING HTTPPOST NOW");
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                System.out.println("CONSOLE -- SETMESSAGEREAD -- HTTPPOST OK");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = "Success";
            } else{
                System.out.println("CONSOLE -- SETMESSAGEREAD -- HTTPPOST NOT OK :( ");

                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            System.out.println("CONSOLE -- IOEXCEPTION AT SETMESSAGEREAD: " + e.getMessage());
            Log.e("IOExc at SetMessageRead",e.getMessage());
        }
        return responseString;
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(result);
    }
}
