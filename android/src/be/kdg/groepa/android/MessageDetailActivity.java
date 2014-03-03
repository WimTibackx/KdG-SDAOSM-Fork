package be.kdg.groepa.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import be.kdg.groepa.model.TextMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageDetailActivity extends Activity implements AsyncResponse {

    TextView senderText;
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmessage);
        Bundle b = getIntent().getExtras();
        System.out.println("CONSOLE: GETTING EXTRAS (BUNDLE)");
        TextView bodyText = (TextView)findViewById(R.id.messageDetailBody);
        TextView subjectText = (TextView)findViewById(R.id.messageDetailSubject);
        senderText = (TextView)findViewById(R.id.messageDetailSenderName);
        senderText.setText(b.getString("senderUsername"));
        bodyText.setText(b.getString("messageBody"));
        subjectText.setText("messageSubject");
    }
        @Override
    public void processFinish(String output) {

    }

    public void sendReply(View view){
        Intent goToMyActivity = new Intent(this, SendMessageActivity.class);
        Bundle b = new Bundle();
        b.putString("receiverUsername", senderText.getText().toString());
        goToMyActivity.putExtras(b);
        startActivity(goToMyActivity);
    }
}
