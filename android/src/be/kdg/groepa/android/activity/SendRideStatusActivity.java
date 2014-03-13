package be.kdg.groepa.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.HomePageActivity;
import be.kdg.groepa.android.R;
import be.kdg.groepa.android.task.SendMessageTask;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tim on 27/02/14.
 */
public class SendRideStatusActivity extends Activity implements AsyncResponse {

    private EditText editBodyText;
    private EditText editReceiverText;
    private EditText editSubjectText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmessage);
    }

    @Override
    public void processFinish(String output) {
        JSONObject outputJson = null;
        Intent goToMyActivity = null;
        try {
            outputJson = new JSONObject(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (outputJson.has("result")) {
            try {
                Toast.makeText(getApplicationContext(), outputJson.getString("result"), Toast.LENGTH_LONG).show();
                goToMyActivity = new Intent(getApplicationContext(), HomePageActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (outputJson.has("error")) {
            try {
                Toast.makeText(getApplicationContext(), outputJson.getString("error"), Toast.LENGTH_LONG).show();
                goToMyActivity = new Intent(getApplicationContext(), SendRideStatusActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        startActivity(goToMyActivity);
    }
}
