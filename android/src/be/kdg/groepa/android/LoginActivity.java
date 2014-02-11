package be.kdg.groepa.android;

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
public class LoginActivity extends MyActivity {

    EditText txtUsername;
    EditText txtPassword;
    Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        txtUsername = (EditText) this.findViewById(R.id.txtUname);
        txtPassword = (EditText) this.findViewById(R.id.txtPwd);
        btnLogin = (Button) this.findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response;
                String responseString = null;

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", txtUsername.getText().toString());
                    jsonObject.put("password",txtPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<NameValuePair> param = new ArrayList<>(1);
                param.add(new BasicNameValuePair("data",jsonObject.toString()));

                HttpGet httpPost = new HttpGet("http://localhost/BackEnd/login");
                //try {
                    //httpPost.setEntity(new UrlEncodedFormEntity(param));
                    HttpParams p = new BasicHttpParams();
                    p.setParameter("data",jsonObject.toString());
                    httpPost.setParams(p);
                /*} catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/

                try {
                    response = httpclient.execute(httpPost);
                    StatusLine statusLine = response.getStatusLine();
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        out.close();
                        responseString = out.toString();
                    } else{
                        //Closes the connection.
                        response.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());
                    }
                } catch (IOException e) {
                    //TODO Handle problems..
                }
                Toast toast = Toast.makeText(getApplicationContext(), responseString, 500);
                toast.show();
            }
        });
    }
}