package cl.niclabs.adkintunmobile.utils.activemeasurements.VideoTest;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Process;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveMeasurementsTest;

public class VideoTest {
    private ActiveMeasurementsTest mainTest;
    private WebView webView;
    private long previousRxBytes = -1;
    private long previousTxBytes = -1;

    public VideoTest(ActiveMeasurementsTest mainTest, WebView webView) {
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

        webView.addJavascriptInterface(new VideoTestJavascriptInterface(this), "JSInterface");
        webView.loadUrl("file:///android_asset/video_test.js");
    }

    public void onVideoEnded(String quality, int timesBuffering, float loadedFraction) {

        long currentRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
        long currentTxBytes = TrafficStats.getUidTxBytes(Process.myUid());
        long totalBytes = (currentRxBytes - previousRxBytes) + (currentTxBytes - previousTxBytes);
        Log.d("ASDASD", "Final Video " + currentRxBytes);
        previousRxBytes = -1;

        mainTest.onVideoEnded(getQuality(quality), timesBuffering, loadedFraction, totalBytes);
    }

    public String getQuality(String quality){
        String text;
        switch (quality){
            case "tiny":
                text = "144p";
                break;
            case "small":
                text = "240p";
                break;
            case "medium":
                text = "360p";
                break;
            case "large":
                text = "480p";
                break;
            case "hd720":
                text = "720p";
                break;
            default:
                text = "unknown";
                break;
        }
        return text;
    }


    public Context getContext() {
        return webView.getContext();
    }

    public void finish() {
        mainTest.onVideoTestFinish();
    }

    public void startCountingBytes() {
        if (previousRxBytes == -1) {
            previousRxBytes = TrafficStats.getUidRxBytes(Process.myUid());
            previousTxBytes = TrafficStats.getUidTxBytes(Process.myUid());
            Log.d("ASDASD", "INICIAL Video " + previousRxBytes);
        }
    }
}
