package cl.niclabs.adkintunmobile.utils.activemeasurements;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cz.msebera.android.httpclient.Header;

public abstract class CheckServerTask extends AsyncTask<String, Void, Void> {
    private ArrayList<String> serversUrlList;
    Context context;

    public CheckServerTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(String... params) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String serverUrl = sharedPreferences.getString(context.getString(R.string.settings_speed_test_server_key), "0");

        //final String serverUrl = context.getString(R.string.speed_test_server) + ":5000/activeServers/";

        URL url;
        HttpURLConnection urlConnection = null;
        int responseCode = -1;
        try {
            url = new URL("http://" + serverUrl + ":5000/status");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("HEAD");
            urlConnection.setConnectTimeout(1000);
            responseCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            handleResponse(responseCode);
        }

        return null;
    }

    public abstract void handleResponse(int responseCode);


}