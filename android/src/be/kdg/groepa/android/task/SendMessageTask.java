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
public class SendMessageTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private AsyncResponse delegate=null;

    private String sender, receiver, subject, body;

    public SendMessageTask(String sender, String receiver, String subject, String body, Context context, AsyncResponse response){
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.context = context;
        this.delegate = response;
    }

    public SendMessageTask(String sender, String receiver, String subject, String body, Context context){
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences privPref = context.getSharedPreferences("CarpoolPreferences",Context.MODE_PRIVATE);

        PreferenceManager.setDefaultValues(this.context, R.xml.preferences, false);
        String serverAddr = preferences.getString("carpoolServer","127.0.0.1:8080");

        String url = "http://"+serverAddr+"/BackEnd/authorized/textmessage/send";
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;

        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println("SENDER: " + sender);
            jsonObject.put("senderUsername", sender);
            jsonObject.put("receiverUsername", receiver);
            jsonObject.put("messageSubject", subject);
            jsonObject.put("messageBody", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = null;
        httpPost = new HttpPost(url);
        httpPost.setHeader("Cookie", "Token="+privPref.getString("Token", ""));

        // Execute HTTP Post Request
        String responseString = null;

        try {
            httpPost.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
            System.out.println("SENDING TASK TO BACKEND");
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                System.out.println("STATUSLINE NOK: " + statusLine.getReasonPhrase());
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
        System.out.println("POSTEXECUTE TASK");

        if(delegate != null){
            this.delegate.processFinish(result);
        }
    }
}
