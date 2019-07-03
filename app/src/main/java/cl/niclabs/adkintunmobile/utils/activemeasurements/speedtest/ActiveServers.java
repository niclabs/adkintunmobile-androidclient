package cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import cl.niclabs.adkintunmobile.R;

public class ActiveServers {
    private ArrayList<String> serverPortsList;
    private ArrayList<String> serverHostsList;
    private ArrayList<String> serverNamesList;
    private Context context;

    public ActiveServers(Context cxt) {
        this.context = cxt;
    }

    public void getActiveServers()  {
        try {
            String activeServersJSON = getActiveServersJSON();
            testActiveServers(activeServersJSON);

            Log.i("SERVERS PORTS", Arrays.toString(serverPortsList.toArray()));
            Log.i("SERVERS HOSTS", Arrays.toString(serverHostsList.toArray()));
            Log.i("SERVERS NAMES", Arrays.toString(serverNamesList.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void testActiveServers(String serversJSON) {
        try {
            JSONObject response = new JSONObject(serversJSON);
            JSONArray activeServers = response.getJSONArray("data");
            serverHostsList = new ArrayList<>();
            serverPortsList = new ArrayList<>();
            serverNamesList = new ArrayList<>();

            for (int i =0; i < activeServers.length(); i++) {
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
        } catch (JSONException e) {
            Log.d("JSON", "API JSONException");
            e.printStackTrace();
        }
    }

    private String getActiveServersJSON() throws IOException {
        InputStream is = null;
        String response = "";
        String host = context.getString(R.string.speed_test_server_host);
        String port = context.getString(R.string.speed_test_server_port);
        Log.i("Saved values:", host + " " + port);
        final String address = host + ":" + port + "/active_servers/";
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000 /* milliseconds */);
            conn.setConnectTimeout(5000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            response = readStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return response;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}

