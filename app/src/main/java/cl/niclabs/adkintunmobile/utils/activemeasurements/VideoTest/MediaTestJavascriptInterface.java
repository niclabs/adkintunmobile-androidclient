package cl.niclabs.adkintunmobile.utils.activemeasurements.VideoTest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;

public class MediaTestJavascriptInterface {
    private MediaTest mediaTest;
    private Context context;

    public MediaTestJavascriptInterface(MediaTest mediaTest) {
        this.mediaTest = mediaTest;
        this.context = mediaTest.getContext();
    }

    @JavascriptInterface
    public void doEchoTest(String echo) {
        Log.d("VideoView", echo);
    }

    @JavascriptInterface
    public void playVideo() {
        //emulateClick(webView);
    }

    @JavascriptInterface
    public void onVideoEnded(String quality, int timesBuffering, float loadedFraction) {
        mediaTest.onVideoEnded(quality, timesBuffering, loadedFraction);
    }

    @JavascriptInterface
    public void onVideoTestFinish() {
        mediaTest.finish();
    }

    @JavascriptInterface
    public void makeToast(String quality) {
        String text = mediaTest.getQuality(quality);
        Toast.makeText(context, text,
                Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void startCountingBytes() {
        mediaTest.startCountingBytes();
    }

    @JavascriptInterface
    public boolean getQuality(String quality) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        switch (quality){
            case "tiny":
                return sharedPreferences.getBoolean(context.getString(R.string.settings_video_test_quality_tiny_key), false);
            case "small":
                return sharedPreferences.getBoolean(context.getString(R.string.settings_video_test_quality_small_key), false);
            case "medium":
                return sharedPreferences.getBoolean(context.getString(R.string.settings_video_test_quality_medium_key), false);
            case "large":
                return sharedPreferences.getBoolean(context.getString(R.string.settings_video_test_quality_large_key), false);
            case "hd720":
                return sharedPreferences.getBoolean(context.getString(R.string.settings_video_test_quality_hd720_key), false);
            default:
                return false;
        }
    }
}
