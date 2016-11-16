package cl.niclabs.adkintunmobile.utils.activemeasurements;

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
    private ArrayList<String> serversUrlList;
    Context context;

    public ActiveServersTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(String... params) {
        final String url = context.getString(R.string.speed_test_server) + ":5000/activeServers/";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray activeServers = response.getJSONArray("data");
                    serversUrlList = new ArrayList<>();

                    for (int i=0; i<activeServers.length(); i++){
                        JSONObject server = activeServers.getJSONObject(i);
                        String serverUrl = server.getString("url");

                        URL url;
                        HttpURLConnection urlConnection = null;
                        int responseCode = -1;
                        try {
                            url = new URL(serverUrl + "status");
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
                                serverUrl = serverUrl.substring(7, serverUrl.indexOf(":5000"));
                                serversUrlList.add(serverUrl);
                            }
                        }
                    }
                    Bundle activeServersBundle = new Bundle();
                    for (int i=0; i<serversUrlList.size(); i++){
                        activeServersBundle.putString("serverUrl" + i, serversUrlList.get(i));
                    }
                    activeServersBundle.putInt("count", serversUrlList.size());
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
