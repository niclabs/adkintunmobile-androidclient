package cl.niclabs.adkintunmobile.utils.activemeasurements.VideoTest;

import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.MediaTestDialog;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.ActiveMeasurementsSettingsActivity;

public class MediaTest {
    private MediaTestDialog mainTest;
    private WebView webView;
    private long previousRxBytes = -1;
    private long previousTxBytes = -1;

    public MediaTest(MediaTestDialog mainTest, WebView webView) {
        this.mainTest = mainTest;
        this.webView = webView;
    }

    public void start() {

        webView.setWebViewClient(new WebViewClient());

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.addJavascriptInterface(new MediaTestJavascriptInterface(this, getContext()), "JSInterface");
        webView.loadUrl("file:///android_asset/video_test.js");
    }

    public void onVideoEnded(String quality, int timesBuffering, float loadedFraction) {

        long currentRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
        long currentTxBytes = TrafficStats.getUidTxBytes(Process.myUid());
        long totalBytes = (currentRxBytes - previousRxBytes) + (currentTxBytes - previousTxBytes);
        previousRxBytes = -1;

        if (mainTest != null)
            mainTest.onVideoEnded(quality, timesBuffering, loadedFraction, totalBytes);
    }


    public Context getContext() {
        return webView.getContext();
    }

    public void finish() {
        if (mainTest != null)
            mainTest.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("about:blank");
                }
            });
    }

    public void startCountingBytes() {
        if (previousRxBytes == -1) {
            previousRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
            previousTxBytes = TrafficStats.getUidTxBytes(Process.myUid());
        }
    }

    public void noneSelectedQuality() {
        Intent myIntent = new Intent(getContext(), ActiveMeasurementsSettingsActivity.class);
        myIntent.putExtra(getContext().getString(R.string.settings_active_measurements_key), R.string.settings_video_test_category_key);
        getContext().startActivity(myIntent);
        if (mainTest != null)
            mainTest.dismiss();
    }
}
