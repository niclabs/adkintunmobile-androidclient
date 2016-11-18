package cl.niclabs.adkintunmobile.views.activemeasurements;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.VideoTest.MediaTest;
import cl.niclabs.adkintunmobile.utils.activemeasurements.VideoTest.MediaTestJavascriptInterface;

public class MediaTestDialog extends DialogFragment{

    private View view;
    private TextView urlsTime;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        view = inflater.inflate(R.layout.fragment_video_test_dialog, container, false);
        getDialog().setTitle("MediaTest");

        // Get visual elements
        urlsTime = (TextView) view.findViewById(R.id.urls);
        webView = (WebView) view.findViewById(R.id.webView);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String maxQuality = sharedPreferences.getString(getString(R.string.settings_video_test_max_quality_key), "None");
        if (maxQuality.equals("None"))
            getMaxQuality();
        else
            new MediaTest(this, webView).start();

        return view;
    }

    private void getMaxQuality() {
        webView.setVisibility(View.INVISIBLE);
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

        webView.addJavascriptInterface(new MediaTestJavascriptInterface(new MediaTest(this, webView), getContext()), "JSInterface");
        webView.loadUrl("file:///android_asset/get_max_quality.js");
    }

    public void onWebPageLoaded(String url, long loadingTime, long sizeByte){
        urlsTime.setText(urlsTime.getText() + "\n" + url + ": " + loadingTime + "ms " + Formatter.formatFileSize(getContext(), sizeByte));
    }

    public void onVideoEnded(final String quality, final int timesBuffering, final float loadedFraction, final long totalBytes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                urlsTime.setText(urlsTime.getText() + "\nFor " + quality + ": "
                        + timesBuffering + "ms buffering, "
                        + (int) (loadedFraction * 100) + "% loaded, "
                        + Formatter.formatFileSize(getContext(), totalBytes));
            }
        });
    }
}
