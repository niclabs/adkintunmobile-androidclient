package cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

public abstract class ActiveServersTask extends AsyncTask<String, Void, Void> {
    private ArrayList<String> serverPortsList;
    private ArrayList<String> serverHostsList;
    private ArrayList<String> serverNamesList;
    private Context context;

    public ActiveServersTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(String... params) {
        String host = context.getString(R.string.speed_test_server_host);
        String port = context.getString(R.string.speed_test_server_port);
        final String url = host + ":" + port + "/activeServers/";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray activeServers = response.getJSONArray("data");
                    serverHostsList = new ArrayList<>();
                    serverPortsList = new ArrayList<>();
                    serverNamesList = new ArrayList<>();

                    for (int i=0; i<activeServers.length(); i++){
                        JSONObject server = activeServers.getJSONObject(i);
                        String serverHost = server.getString("host");
                        String serverPort = server.getString("port");
                        String serverName = server.getString("name") + ", " + server.getString("country");

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
                            if (responseCode >= 200 && responseCode<400){
                                serverHostsList.add(serverHost);
                                serverPortsList.add(serverPort);
                                serverNamesList.add(serverName);
                            }
                        }
                    }
                    Bundle activeServersBundle = new Bundle();
                    for (int i = 0; i< serverHostsList.size(); i++){
                        activeServersBundle.putString("serverHost" + i, serverHostsList.get(i));
                        activeServersBundle.putString("serverPort" + i, serverPortsList.get(i));
                        activeServersBundle.putString("serverName" + i, serverNamesList.get(i));
                    }
                    activeServersBundle.putInt("count", serverHostsList.size());
                    activeServersBundle.putBoolean("shouldExecute", false);
                    handleActiveServers(activeServersBundle);

                } catch (JSONException e) {
                    Log.d("JSON", "API JSONException");
                    e.printStackTrace();
                }
            };
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
        return null;
    }

    public abstract void handleActiveServers(Bundle bundle);


}
