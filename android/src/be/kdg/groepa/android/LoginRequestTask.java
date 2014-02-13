package be.kdg.groepa.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by delltvgateway on 2/11/14.
 */
public class LoginRequestTask extends AsyncTask<Void, Void, String> {

    private String username;
    private String password;
    private Context context;
    private AsyncResponse delegate=null;

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
            jsonObject.put("username", this.username);
            jsonObject.put("password",this.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<NameValuePair> param = new ArrayList<>(1);
        param.add(new BasicNameValuePair("data",jsonObject.toString()));


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        PreferenceManager.setDefaultValues(this.context, R.xml.preferences, false);
        String serverAddr = preferences.getString("carpoolServer","127.0.0.1:8080");

        String url = "http://"+serverAddr+"/BackEnd/login?";
        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("data", jsonObject.toString()));
        url += URLEncodedUtils.format(params, "utf-8");

        HttpGet httpPost = new HttpGet(url);

        try {
            response = httpclient.execute(httpPost);
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
            Log.e("IOExc at Login",e.getMessage());
        }
        return responseString;
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(result);
    }
}
