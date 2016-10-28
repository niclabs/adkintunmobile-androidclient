package cl.niclabs.adkintunmobile.views.activemeasurements;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.webpagestest.WebPagesTest;

public class WebPagesTestDialog extends DialogFragment{

    private View view;
    private TextView urlsTime;
    private WebView webView;
    private int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        view = inflater.inflate(R.layout.fragment_web_pages_test_dialog, container, false);
        getDialog().setTitle("SpeedTest");

        // Get visual elements
        urlsTime = (TextView) view.findViewById(R.id.urls);
        webView = (WebView) view.findViewById(R.id.webView);

        new WebPagesTest(this, webView).start();

        return view;
    }

    public void onWebPageLoaded(String url, long loadingTime, long sizeByte){
        urlsTime.setText(urlsTime.getText() + "\n" + url + ": " + loadingTime + "ms " + Formatter.formatFileSize(getContext(), sizeByte));
    }
}
