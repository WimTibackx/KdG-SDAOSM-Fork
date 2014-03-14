package be.kdg.groepa.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import be.kdg.groepa.android.notification.GoogleMessageActivity;
import be.kdg.groepa.android.task.LoginRequestTask;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thierry on 11/02/14.
 */
public class LoginActivity extends Activity implements AsyncResponse {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private LoginRequestTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txtUsername = (EditText) this.findViewById(R.id.txtUname);
        txtPassword = (EditText) this.findViewById(R.id.txtPwd);
        btnLogin = (Button) this.findViewById(R.id.login);
        final AsyncResponse loginActivity = this;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         task = new LoginRequestTask(txtUsername.getText().toString(), txtPassword.getText().toString(),getApplicationContext(), loginActivity);
                         task.execute();
            }
        });
    }

    @Override
    public void processFinish(String output) {
        if (output == null || output.isEmpty() || output.equals("IOException")) {
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(output);
            if (jsonObject.has("Token")) {
                String token = jsonObject.getString("Token");
                int id = jsonObject.getInt("UserId");
                SharedPreferences privPref = getApplicationContext().getSharedPreferences("CarpoolPreferences",MODE_PRIVATE);
                SharedPreferences.Editor privPrefEditor = privPref.edit();
                privPrefEditor.putString("Token",token);
                privPrefEditor.putInt("UserId", id);
                privPrefEditor.putString("Username", txtUsername.getText().toString());
                privPrefEditor.commit();
                Intent goToMyActivity = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(goToMyActivity);
            } else {
                String error = jsonObject.getString("error");
                switch (error) {
                    case "LoginComboWrong":
                        new AlertDialog.Builder(this).setTitle(R.string.LoginComboWrong_ErrorTitle)
                            .setMessage(R.string.LoginComboWrong_ErrorMsg)
                            .setNegativeButton(R.string.OK, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    txtUsername.setText("");
                                    txtPassword.setText("");
                                    txtUsername.requestFocus();
                                }
                            }).show();
                        break;
                    case "ParseError":
                        Toast.makeText(getApplicationContext(),"We had an error: "+error,Toast.LENGTH_LONG).show();
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}