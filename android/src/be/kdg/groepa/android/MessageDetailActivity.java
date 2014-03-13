package be.kdg.groepa.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
/**
 * Created by Tim on 3/03/14.
 */
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
        TextView bodyText = (TextView)findViewById(R.id.messageDetailBody);
        TextView subjectText = (TextView)findViewById(R.id.messageDetailSubject);
        senderText = (TextView)findViewById(R.id.messageDetailSenderName);
        senderText.setText("Sender: " + b.getString("senderUsername"));
        bodyText.setText("Message: " + b.getString("messageBody"));
        subjectText.setText("Subject: " + b.getString("messageSubject"));
    }

    @Override
    public void processFinish(String output) {

    }

    public void sendReply(View view){
        Intent goToMyActivity = new Intent(this, SendMessageActivity.class);
        Bundle b = new Bundle();
        // Don't copy the "Sender: "
        b.putString("receiverUsername", senderText.getText().subSequence(8, senderText.getText().length()).toString());
        goToMyActivity.putExtras(b);
        startActivity(goToMyActivity);
    }
}
