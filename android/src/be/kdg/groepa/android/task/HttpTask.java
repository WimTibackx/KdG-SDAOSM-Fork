package be.kdg.groepa.android.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.helper.PreferencesHelper;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by delltvgateway on 3/3/14.
 */
public class HttpTask extends AsyncTask<Void, Void, HttpResponse> {

    private static final String APIROOT="http://%s/BackEnd%s";

    private Context context;
    private String entrypoint;
    private boolean addCookie;
    private HttpTaskUser httpTaskUser;
    private static String result;

    public HttpTask(Context context, boolean addCookie, String entrypoint, HttpTaskUser httpTaskUser) {
        this.context = context;
        this.addCookie = addCookie;
        this.entrypoint = entrypoint;
        this.httpTaskUser = httpTaskUser;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        System.out.println("HTTPTASK DOINBACKGROUND STARTED");
        HttpGet request = new HttpGet(this.getFullUrl());
        this.addCookieIfNeeded(request);
        HttpResponse response = null;

        try {
            response = new DefaultHttpClient().execute(request);
        } catch (IOException e) {
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            response.getEntity().writeTo(out);
        } catch (IOException e) {
        }
        result = out.toString();
        return response;
    }

    @Override
    protected void onPostExecute(HttpResponse response) {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            return;
        }
        this.httpTaskUser.responseSuccess(result);
    }

    private String getFullUrl() {
        return String.format(APIROOT, PreferencesHelper.getServerAddr(this.context), this.entrypoint);
    }

    private void addCookieIfNeeded(HttpRequest request) {
        if (!this.addCookie) return;
        request.setHeader("Cookie", "Token="+PreferencesHelper.getLocalUser(this.context).getToken());
    }
}