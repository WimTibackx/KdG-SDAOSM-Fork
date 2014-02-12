package be.kdg.groepa.android;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
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

    public LoginRequestTask(String username, String password, Context context) {
        super();
        this.username = username;
        this.password = password;
        this.context = context;
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

        HttpGet httpPost = new HttpGet("http://192.168.1.2:8080/BackEnd/login");
        //try {
        //httpPost.setEntity(new UrlEncodedFormEntity(param));
        HttpParams p = new BasicHttpParams();
        p.setParameter("data",jsonObject.toString());
        httpPost.setParams(p);
                /*} catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/

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
            //TODO Handle problems..
        }
        Log.d("Login","ResponseString = "+responseString);
        return responseString;
    }

    protected void onPostExecute(String result) {
        Toast toast = Toast.makeText(this.context, result, 500);
        toast.show();
    }
}
