package be.kdg.groepa.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {

    private TextView userToken;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postlogin);
        SharedPreferences privPref = getApplicationContext().getSharedPreferences("CarpoolPreferences",MODE_PRIVATE);
        ((TextView) this.findViewById(R.id.userToken)).setText(privPref.getString("Token",""));
        //((TextView) this.findViewById(R.id.userToken)).invalidate();
        Toast.makeText(getApplicationContext(),"Token = "+privPref.getString("Token",""),Toast.LENGTH_LONG).show();
    }


}
