package be.kdg.groepa.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePageActivity extends Activity implements AsyncResponse{

    private TextView userToken;
    private Button inboxButton;
    private JSONArray sentMessages, receivedMessages;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        SharedPreferences privPref = getApplicationContext().getSharedPreferences("CarpoolPreferences",MODE_PRIVATE);
        //((TextView) this.findViewById(R.id.userToken)).invalidate();
        RequestMessagesTask task = new RequestMessagesTask(getApplicationContext(), this);
        task.execute();    }


    @Override
    public void processFinish(String output) {
        JSONObject outputJson = null;
        int unreadMessages = 0;
        try {
            outputJson = new JSONObject(output);
            receivedMessages = outputJson.getJSONArray("receivedMessages");
            sentMessages = outputJson.getJSONArray("sentMessages");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < receivedMessages.length(); i++){
            try {
                JSONObject obj = new JSONObject(receivedMessages.get(i).toString());
                if(!obj.getBoolean("read")){
                    unreadMessages++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ((Button) this.findViewById(R.id.buttonInbox)).setText(String.valueOf(unreadMessages));
    }


    public void goToSendMessage(View view) {
        Intent goToMyActivity = new Intent(this, SendMessageActivity.class);
        startActivity(goToMyActivity);
    }

    public void logOut(View view) {
    }

    public void goToInbox(View view){
        Intent goToMyActivity = new Intent(this, InboxActivity.class);
        Bundle b = new Bundle();
        b.putString("sentMessages", sentMessages.toString());
        b.putString("receivedMessages", receivedMessages.toString());
        goToMyActivity.putExtras(b);
        startActivity(goToMyActivity);

    }
}
