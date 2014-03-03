package be.kdg.groepa.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import be.kdg.groepa.model.TextMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends Activity implements AsyncResponse {

    /**
     * Called when the activity is first created.
     */
    private static boolean received = true;
    private List<TextMessage> sentMessages;
    private List<TextMessage> receivedMessages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("CONSOLE: CREATING INBOXACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        SharedPreferences privPref = getApplicationContext().getSharedPreferences("CarpoolPreferences", MODE_PRIVATE);
        Bundle b = getIntent().getExtras();
        System.out.println("CONSOLE: GETTING EXTRAS (BUNDLE)");
        this.sentMessages = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
        JSONArray sentMessagesJson = null, receivedMessagesJson = null;

        System.out.println("CONSOLE: STARTING CREATING JSONARRAYS");
        try {
            sentMessagesJson = new JSONArray(b.getString("sentMessages"));
            receivedMessagesJson = new JSONArray(b.getString("receivedMessages"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("CONSOLE: JSONARRAYS DONE, CREATING NEW TEXTMESSAGES");
        System.out.println("CONSOLE: LENGTH SENT MESSAGES: " + sentMessagesJson.length());
        System.out.println("CONSOLE: LENGTH RECEIVED MESSAGES: " + receivedMessagesJson.length());
        if (sentMessagesJson.length() > 0) {
            for (int i = 0; i < sentMessagesJson.length(); i++) {
                try {
                    JSONObject obj = new JSONObject(sentMessagesJson.get(i).toString());
                    sentMessages.add(new TextMessage(obj.getInt("id"), obj.getString("senderUsername"), obj.getString("receiverUsername"), obj.getString("messageBody"), obj.getString("messageSubject"), obj.getBoolean("read")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (receivedMessagesJson.length() > 0) {
            for (int i = 0; i < receivedMessagesJson.length(); i++) {
                try {
                    JSONObject obj = new JSONObject(receivedMessagesJson.get(i).toString());
                    receivedMessages.add(new TextMessage(obj.getInt("id"), obj.getString("senderUsername"), obj.getString("receiverUsername"), obj.getString("messageBody"), obj.getString("messageSubject"), obj.getBoolean("read")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void processFinish(String output) {

    }


    public void goToSendMessage(View view) {
        Intent goToMyActivity = new Intent(this, SendMessageActivity.class);
        startActivity(goToMyActivity);
    }

    public void changeInboxMode(View view){

    }
}
