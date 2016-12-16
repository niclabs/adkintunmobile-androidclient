package cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.ConnectivityTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SiteResult;
import cl.niclabs.adkintunmobile.views.activemeasurements.ConnectivityTestDialog;

public class ConnectivityTest {
    private ConnectivityTestDialog mainTest;
    private WebView webView;
    private ArrayList<String> urls = new ArrayList<>();
    private int i = 0;
    private AsyncTask currentTask;

    private ConnectivityTestReport report;

    public ConnectivityTest(ConnectivityTestDialog mainTest, WebView webView) {
        this.mainTest = mainTest;
        this.webView = webView;
    }

    public void start() {
        this.report = new ConnectivityTestReport();
        this.report.setUpReport(mainTest.getContext());
        this.report.save();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainTest.getContext());
        int sitesCount = sharedPreferences.getInt(mainTest.getString(R.string.settings_connectivity_sites_count_key), 0);
        for (int i=1; i<=sitesCount; i++){
            urls.add("http://" + sharedPreferences.getString(mainTest.getString(R.string.settings_connectivity_test_site_) + i, ""));
        }
        startLoading();
    }

    private void loadNextPage(){
        if (i<urls.size()) {
            currentTask = new ConnectivityTestTask(this).execute(urls.get(i));
        }
        else {
            //mainTest.onWebPageTestFinish();
        }
    }

    private void startLoading() {
        if (mainTest.getActivity()!=null)
            mainTest.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setUpWebView();
                    mainTest.setUpTextView(urls);
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
            private String lastFinished;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url=="about:blank"){
                    return;
                }
                if (startTime == -1) {
                    startTime = System.currentTimeMillis();
                    mainTest.onWebPageStarted(i);
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d("Error View", request.toString() + " " + error.toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals("about:blank")){
                    return;
                }
                if (url.equals(lastFinished)){
                    return;
                }
                lastFinished = url;
                webView.loadUrl("about:blank");
                finishTime = System.currentTimeMillis();
                long loadingTime = finishTime - startTime;

                long currentRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
                long currentTxBytes = TrafficStats.getUidTxBytes(Process.myUid());

                long sizeBytes = (currentRxBytes - ConnectivityTestTask.previousRxBytes) + (currentTxBytes - ConnectivityTestTask.previousTxBytes);

                mainTest.onWebPageLoaded(i, loadingTime, sizeBytes);
                SiteResult r = new SiteResult();
                r.setUpSiteResult(urls.get(i), true, loadingTime, sizeBytes);
                r.report = report;
                r.save();
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
        if (mainTest.getActivity()!=null)
            mainTest.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (responseCode >= 200 && responseCode < 400) {
                        ConnectivityTestTask.previousRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
                        ConnectivityTestTask.previousTxBytes = TrafficStats.getUidTxBytes(Process.myUid());
                        webView.loadUrl(urls.get(i));
                    }
                    else{
                        mainTest.onWebPageLoaded(i, 0, 0);
                        SiteResult r = new SiteResult();
                        r.setUpSiteResult(urls.get(i), false, 0, 0);
                        r.report = report;
                        r.save();
                        i++;
                        loadNextPage();
                    }
                }
            });
    }

    public void cancelTask() {
        currentTask.cancel(true);
    }
}


