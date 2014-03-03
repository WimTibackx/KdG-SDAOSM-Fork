package be.kdg.groepa.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private List<TextMessage> receivedMessagesRead;
    private List<TextMessage> receivedMessagesUnRead;
    private ListView messagesListViewRead;
    private ListView messagesListViewUnRead;
    private static ArrayAdapter<TextMessage> adapterRead;
    private static ArrayAdapter<TextMessage> adapterUnread;
    private static ArrayAdapter<TextMessage> adapterSent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("CONSOLE: EXECUTING INBOX ONCREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        // The messages come from the main page, where the messages were already loaded. This is to improve performance.
        Bundle b = getIntent().getExtras();
        this.sentMessages = new ArrayList<>();
        this.receivedMessagesRead = new ArrayList<>();
        this.receivedMessagesUnRead = new ArrayList<>();
        JSONArray sentMessagesJson = null, receivedMessagesJson = null;
        try {
            sentMessagesJson = new JSONArray(b.getString("sentMessages"));
            receivedMessagesJson = new JSONArray(b.getString("receivedMessages"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Load the messages from the JSONs into objects that can be displayed in a listview
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
                    if(obj.getBoolean("read")){
                        receivedMessagesRead.add(new TextMessage(obj.getInt("id"), obj.getString("senderUsername"), obj.getString("receiverUsername"), obj.getString("messageBody"), obj.getString("messageSubject"), obj.getBoolean("read")));
                    } else {
                        receivedMessagesUnRead.add(new TextMessage(obj.getInt("id"), obj.getString("senderUsername"), obj.getString("receiverUsername"), obj.getString("messageBody"), obj.getString("messageSubject"), obj.getBoolean("read")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // Create the listviews & configure them
        this.messagesListViewRead = (ListView) findViewById(R.id.inboxViewRead);
        this.messagesListViewUnRead = (ListView) findViewById(R.id.inboxViewUnread);
        adapterRead = new ArrayAdapter<>(this, R.layout.textmessagetextview, receivedMessagesRead);
        adapterUnread = new ArrayAdapter<>(this, R.layout.textmessagetextview, receivedMessagesUnRead);
        messagesListViewRead.setAdapter(adapterRead);
        messagesListViewUnRead.setAdapter(adapterUnread);
        final AsyncResponse inboxActivity = this;
        // Create an object OnItemClickListener to reuse
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextMessage msg = (TextMessage)adapterView.getAdapter().getItem(i);
                Intent goToMyActivity = new Intent(getApplicationContext(), MessageDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("senderUsername", msg.getSenderUsername());
                b.putString("messageSubject", msg.getMessageSubject());
                b.putString("messageBody", msg.getMessageBody());
                SetMessageReadTask task = new SetMessageReadTask(msg.getId(), getApplicationContext(), inboxActivity);
                task.execute();
                goToMyActivity.putExtras(b);
                startActivity(goToMyActivity);
            }
        };
        messagesListViewRead.setOnItemClickListener(listener);
        messagesListViewUnRead.setOnItemClickListener(listener);


    }

    @Override
    public void processFinish(String output) {

    }

    public void goToSendMessage(View view) {
        Intent goToMyActivity = new Intent(this, SendMessageActivity.class);
        startActivity(goToMyActivity);
    }

    public void changeInboxMode(View view){
        TextView viewRead = (TextView)findViewById(R.id.readMessagesTextview);
        TextView viewUnread = (TextView)findViewById(R.id.unreadMessagesTextview);
        ListView listViewRead = (ListView)findViewById(R.id.inboxViewRead);
        if(!received){
            viewUnread.setText(R.string.UnreadMessages);
            adapterUnread = new ArrayAdapter<>(this, R.layout.textmessagetextview, receivedMessagesUnRead);

            listViewRead.setVisibility(View.VISIBLE);
            viewRead.setVisibility(View.VISIBLE);

            received = true;
        } else {
            // On switching to sent messages, we'll use the unread listview to display the sent messages and hide the received messages listview.
            // On changing back to received messages, set everything back to what it was.
            adapterUnread = new ArrayAdapter<>(this, R.layout.textmessagetextview, sentMessages);
            viewUnread.setText(R.string.SentMessages);

            listViewRead.setVisibility(View.INVISIBLE);
            viewRead.setVisibility(View.INVISIBLE);

            received = false;
        }
        messagesListViewUnRead.setAdapter(adapterUnread);
    }
}
