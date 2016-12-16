package cl.niclabs.adkintunmobile.views.activemeasurements;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.mediatest.MediaTest;
import cl.niclabs.adkintunmobile.utils.activemeasurements.mediatest.MediaTestJavascriptInterface;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class MediaTestDialog extends DialogFragment{

    private View view;
    private TableLayout tableLayout;
    private WebView webView;
    private MediaTest mediaTest;
    private int index = 0;

    public void onQualityTestStarted(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(index);
                tableRow.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            }
        });
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

    public void onVideoEnded(final String quality, final int timesBuffering, final float loadedFraction, final long totalBytes) {
        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                urlsTime.setText(urlsTime.getText() + "\nFor " + quality + ": "
                        + timesBuffering + "ms buffering, "
                        + (int) (loadedFraction * 100) + "% loaded, "
                        + Network.formatBytes(totalBytes));
            }
        });*/
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(index);
                index++;
                tableRow.findViewById(R.id.progress_bar).setVisibility(View.GONE);
                ImageView status = (ImageView) tableRow.findViewById(R.id.ic_done);
                status.setVisibility(View.VISIBLE);
                if ((int) (loadedFraction*100) < 100){
                    status.setImageResource(R.drawable.ic_clear_black);
                    return;
                }
                status.setImageResource(R.drawable.ic_done_black);
                ((TextView)tableRow.findViewById(R.id.loaded_percentage)).setText((int) (loadedFraction * 100) + "%");
                ((TextView)tableRow.findViewById(R.id.loading_time)).setText(timesBuffering + "ms");
                ((TextView)tableRow.findViewById(R.id.downloaded_size)).setText(Network.formatBytes(totalBytes));
            }
        });
    }


    public void setUpTextView() {
        String[] qualitiesNames = getResources().getStringArray(R.array.qualities_names);
        String[] qualitiesPixels = getResources().getStringArray(R.array.qualities_pixels);
        int index = tableLayout.getChildCount();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (int i=0; i<qualitiesNames.length; i++){
            if (mediaTest.getQuality(qualitiesNames[i])) {
                Log.d("hola", qualitiesPixels[i]);
                TableRow tableRow = (TableRow) inflater.inflate(R.layout.video_qualities_dialog_row, null);
                ((TextView) tableRow.findViewById(R.id.quality)).setText(qualitiesPixels[i]);

                tableLayout.addView(tableRow, index++);
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //mediaTest.cancelTask();
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_video_test_dialog, null);

        // Get visual elements
        webView = (WebView) view.findViewById(R.id.webView);
        tableLayout = (TableLayout) view.findViewById(R.id.video_qualities_table_layout);
        setCancelable(false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String maxQuality = sharedPreferences.getString(getString(R.string.settings_video_test_max_quality_key), "None");
        if (maxQuality.equals("None"))
            getMaxQuality();
        else {
            mediaTest = new MediaTest(this, webView);
            setUpTextView();
            mediaTest.start();
        }
        builder.setView(view);
        return builder.create();
    }
}
