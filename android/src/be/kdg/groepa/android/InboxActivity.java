package be.kdg.groepa.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import be.kdg.groepa.android.dto.TextMessageDTO;
import be.kdg.groepa.android.task.SetMessageReadTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Tim on 27/02/14.
 */
public class InboxActivity extends Activity implements AsyncResponse {

    /**
     * Called when the activity is first created.
     */
    private static boolean received = true;
    private List<TextMessageDTO> sentMessages;
    private List<TextMessageDTO> receivedMessagesRead;
    private List<TextMessageDTO> receivedMessagesUnRead;
    private ListView messagesListViewRead;
    private ListView messagesListViewUnRead;
    private static ArrayAdapter<TextMessageDTO> adapterRead;
    private static ArrayAdapter<TextMessageDTO> adapterUnread;
    private static ArrayAdapter<TextMessageDTO> adapterSent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                    sentMessages.add(new TextMessageDTO(obj.getInt("id"), obj.getString("senderUsername"), obj.getString("receiverUsername"), obj.getString("messageBody"), obj.getString("messageSubject"), obj.getBoolean("read")));
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
                        receivedMessagesRead.add(new TextMessageDTO(obj.getInt("id"), obj.getString("senderUsername"), obj.getString("receiverUsername"), obj.getString("messageBody"), obj.getString("messageSubject"), obj.getBoolean("read")));
                    } else {
                        receivedMessagesUnRead.add(new TextMessageDTO(obj.getInt("id"), obj.getString("senderUsername"), obj.getString("receiverUsername"), obj.getString("messageBody"), obj.getString("messageSubject"), obj.getBoolean("read")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // Create the listviews & configure them
        this.messagesListViewRead = (ListView) findViewById(R.id.lvInboxViewRead);
        this.messagesListViewUnRead = (ListView) findViewById(R.id.lvInboxViewUnread);
        adapterRead = new ArrayAdapter<>(this, R.layout.textmessagetextview, receivedMessagesRead);
        adapterUnread = new ArrayAdapter<>(this, R.layout.textmessagetextview, receivedMessagesUnRead);
        messagesListViewRead.setAdapter(adapterRead);
        messagesListViewUnRead.setAdapter(adapterUnread);
        final AsyncResponse inboxActivity = this;
        // Create an object OnItemClickListener to reuse
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextMessageDTO msg = (TextMessageDTO)adapterView.getAdapter().getItem(i);
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
        TextView viewRead = (TextView)findViewById(R.id.tvReadMessagesTextview);
        TextView viewUnread = (TextView)findViewById(R.id.tvUnreadMessagesTextview);
        ListView listViewRead = (ListView)findViewById(R.id.lvInboxViewRead);
        // Switch to inbox
        if(!received){
            viewUnread.setText(R.string.UnreadMessages);
            adapterUnread = new ArrayAdapter<>(this, R.layout.textmessagetextview, receivedMessagesUnRead);

            listViewRead.setVisibility(View.VISIBLE);
            viewRead.setVisibility(View.VISIBLE);

            received = true;
        // Switch to sent messages
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
