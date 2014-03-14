package be.kdg.groepa.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import be.kdg.groepa.android.activity.UpcomingTrajectsActivity;
import be.kdg.groepa.android.notification.GoogleMessageActivity;
import be.kdg.groepa.android.service.BackgroundService;
import be.kdg.groepa.android.task.LogOutTask;
import be.kdg.groepa.android.task.RequestMessagesTask;
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
        Intent gcmActivity = new Intent(this, GoogleMessageActivity.class);
        startActivity(gcmActivity);
        RequestMessagesTask task = new RequestMessagesTask(getApplicationContext(), this);
        task.execute();
    }

    @Override
    public void onResume(){
        super.onResume();
        RequestMessagesTask task = new RequestMessagesTask(getApplicationContext(), this);
        task.execute();
    }

    @Override
    public void processFinish(String output) {
        System.out.println("CONSOLE -- HOMEPAGE: PROCESSING FINISH");
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
        ((Button) this.findViewById(R.id.btnGoToInbox)).setText(String.valueOf(unreadMessages));
    }


    public void goToSendMessage(View view) {
        Intent goToMyActivity = new Intent(this, SendMessageActivity.class);
        startActivity(goToMyActivity);
    }

    public void logOut(View view) {
        LogOutTask task = new LogOutTask(getApplicationContext(), this);
        task.execute();
    }

    public void goToInbox(View view){
        Intent goToMyActivity = new Intent(this, InboxActivity.class);
        Bundle b = new Bundle();
        b.putString("sentMessages", sentMessages.toString());
        b.putString("receivedMessages", receivedMessages.toString());
        goToMyActivity.putExtras(b);
        startActivity(goToMyActivity);

    }

    public void goToUpcomingTrajects(View view) {
        Intent gotoUpcomingTrajects = new Intent(this, UpcomingTrajectsActivity.class);
        super.startActivity(gotoUpcomingTrajects);
    }
}
