package cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest;

import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Process;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectivityTestTask extends AsyncTask<String, Void, Void> {
    ConnectivityTest connectivityTest;
    protected static long previousRxBytes;
    protected static long previousTxBytes;

    public ConnectivityTestTask(ConnectivityTest connectivityTest) {
        this.connectivityTest = connectivityTest;
    }

    @Override
    protected Void doInBackground(String... params) {
        URL url;
        HttpURLConnection urlConnection = null;
        int responseCode = -1;
        previousRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
        previousTxBytes = TrafficStats.getUidTxBytes(Process.myUid());
        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            responseCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
            connectivityTest.onResponseReceived(responseCode);
        }
        return null;
    }


}