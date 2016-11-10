package cl.niclabs.adkintunmobile.utils.activemeasurements.webpagestest;

import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebPagesTestTask extends AsyncTask<String, Void, Void> {
    WebPagesTest webPagesTest;
    protected static long previousRxBytes;
    protected static long previousTxBytes;

    public WebPagesTestTask(WebPagesTest webPagesTest) {
        this.webPagesTest = webPagesTest;
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
            webPagesTest.onResponseReceived(responseCode);
        }
        return null;
    }


}