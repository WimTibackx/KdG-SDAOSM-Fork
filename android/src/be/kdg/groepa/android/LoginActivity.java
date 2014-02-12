package be.kdg.groepa.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thierry on 11/02/14.
 */
public class LoginActivity extends MyActivity implements AsyncResponse {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /*SharedPreferences privPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor privPrefEditor = privPref.edit();
        privPrefEditor.putString("carpoolServer")*/

        txtUsername = (EditText) this.findViewById(R.id.txtUname);
        txtPassword = (EditText) this.findViewById(R.id.txtPwd);
        btnLogin = (Button) this.findViewById(R.id.login);
        final AsyncResponse loginActivity = this;
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginRequestTask task = new LoginRequestTask(txtUsername.getText().toString(), txtPassword.getText().toString(),getApplicationContext(), loginActivity);
                task.execute();
            }
        });
    }

    @Override
    public void processFinish(String output) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(output);
            if (jsonObject.has("Token")) {
                String token = jsonObject.getString("Token");
                SharedPreferences privPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor privPrefEditor = privPref.edit();
                privPrefEditor.putString("Token",token);
                privPrefEditor.commit();
                Toast.makeText(getApplicationContext(),"We logged in: token is "+token,Toast.LENGTH_LONG).show();
                Intent goToMyActivity = new Intent(getApplicationContext(), MyActivity.class);
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