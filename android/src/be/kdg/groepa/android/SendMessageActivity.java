package be.kdg.groepa.android;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
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
 * Created by Tim on 27/02/14.
 */
public class SendMessageActivity extends Activity implements AsyncResponse{

    private EditText editBodyText;
    private EditText editReceiverText;
    private EditText editSubjectText;
    private Button btnSendMessage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmessage);
        editBodyText = (EditText)findViewById(R.id.editBodyText);
        editReceiverText = (EditText)findViewById(R.id.editReceiverText);
        editSubjectText = (EditText)findViewById(R.id.editSubjectText);
        btnSendMessage = (Button) this.findViewById(R.id.buttonSendMessage);
        SharedPreferences privPref = getApplicationContext().getSharedPreferences("CarpoolPreferences",MODE_PRIVATE);
        final String senderUsername = privPref.getString("Username", "");
        final AsyncResponse msgAc = this;
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked button",Toast.LENGTH_LONG).show();
                SendMessageTask task = new SendMessageTask(senderUsername, editReceiverText.getText().toString(), editSubjectText.getText().toString(), editBodyText.getText().toString(), getApplicationContext(), msgAc);
                task.execute();
            }
        });
    }

    @Override
    public void processFinish(String output) {
        System.out.println("CONSOLE: OUTPUT = "+ output);
        JSONObject outputJson = null;
        Intent goToMyActivity = null;
        try {
            outputJson = new JSONObject(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(outputJson.has("result")){
            try {
                Toast.makeText(getApplicationContext(),outputJson.getString("result") ,Toast.LENGTH_LONG).show();
                goToMyActivity = new Intent(getApplicationContext(), HomePageActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(outputJson.has("error")){
            try {
                Toast.makeText(getApplicationContext(),outputJson.getString("error") ,Toast.LENGTH_LONG).show();
                goToMyActivity = new Intent(getApplicationContext(), SendMessageActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        startActivity(goToMyActivity);
    }
}
