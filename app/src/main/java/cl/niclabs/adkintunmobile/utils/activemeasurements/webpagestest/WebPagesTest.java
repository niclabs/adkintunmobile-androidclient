package cl.niclabs.adkintunmobile.utils.activemeasurements.webpagestest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.os.Process;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveMeasurementsTest;
import cl.niclabs.adkintunmobile.views.activemeasurements.WebPagesTestDialog;
import cz.msebera.android.httpclient.Header;

public class WebPagesTest {
    private WebPagesTestDialog mainTest;
    private WebView webView;
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private long loadingTime[];
    private long sizeBytes[];
    private int i = 0;

    public WebPagesTest(WebPagesTestDialog mainTest, WebView webView) {
        this.mainTest = mainTest;
        this.webView = webView;
    }

    public void start() {
        Log.d("JSON", "API STARTING...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://blasco.duckdns.org:5000/pingSites/", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray urlsArray = response.getJSONArray("data");
                    for (int i=0; i<urlsArray.length(); i++){
                        JSONObject server = urlsArray.getJSONObject(i);
                        urls.add(server.getString("url"));
                        names.add(server.getString("name"));
                    }
                    loadingTime = new long[urls.size()];
                    sizeBytes = new long[urls.size()];

                } catch (JSONException e) {
                    Log.d("JSON", "API JSONException");
                    e.printStackTrace();
                }
                startLoading();
            };
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //manejo en caso de falla
                Log.d("JSON", "API FAIL...");
                //mainTest.onWebPageTestFinish();
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    private void loadNextPage(){
        if (i<urls.size()) {
            new WebPagesTestTask(this).execute(urls.get(i));
        }
        else {
            //mainTest.onWebPageTestFinish();
        }
    }

    private void startLoading() {

        mainTest.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setUpWebView();
                loadNextPage();
            }
        });
    }

    private void setUpWebView() {
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            private long startTime = -1;
            private long finishTime;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (startTime == -1)
                    startTime = System.currentTimeMillis();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d("ERRORview", request.toString() + " " + error.toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                finishTime = System.currentTimeMillis();
                loadingTime[i] = finishTime - startTime;

                long currentRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
                long currentTxBytes = TrafficStats.getUidTxBytes(Process.myUid());
                Log.d("ASDASD", "FINAL "+url+" "+currentRxBytes);

                sizeBytes[i] = (currentRxBytes - WebPagesTestTask.previousRxBytes) + (currentTxBytes - WebPagesTestTask.previousTxBytes);

                mainTest.onWebPageLoaded(names.get(i), loadingTime[i], sizeBytes[i]);
                i++;
                startTime = -1;
                loadNextPage();
            }
        });

        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
    }

    protected void onResponseReceived(final int responseCode) {
        mainTest.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (responseCode >= 200 && responseCode < 400) {
                    WebPagesTestTask.previousRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
                    WebPagesTestTask.previousTxBytes = TrafficStats.getUidTxBytes(Process.myUid());
                    Log.d("ASDASD", "INICIAL  " + WebPagesTestTask.previousRxBytes);
                    webView.loadUrl(urls.get(i));
                }
                else{
                    loadingTime[i] = -1;
                    mainTest.onWebPageLoaded(names.get(i), loadingTime[i], sizeBytes[i]);
                    i++;
                    loadNextPage();
                }
            }
        });

    }

}


