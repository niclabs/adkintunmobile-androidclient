package cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;

public abstract class CheckServerTask extends AsyncTask<String, Void, Void> {
    private ArrayList<String> serversUrlList;
    Context context;

    public CheckServerTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(String... params) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String serverHost = sharedPreferences.getString(context.getString(R.string.settings_speed_test_server_host_key), "0");
        String serverPort = sharedPreferences.getString(context.getString(R.string.settings_speed_test_server_port_key), "0");

        //final String serverUrl = context.getString(R.string.speed_test_server) + ":5000/activeServers/";

        URL url;
        HttpURLConnection urlConnection = null;
        int responseCode = -1;
        try {
            url = new URL(serverHost + ":" + serverPort + "/status/");
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
