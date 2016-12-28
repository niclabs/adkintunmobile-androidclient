package cl.niclabs.adkintunmobile.views.activemeasurements;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

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
    private Button positiveButton;
    private Button negativeButton;

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

    public void onVideoEnded(final String quality, final int bufferingTime, final float loadedFraction, final long totalBytes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(index);
                index++;
                tableRow.findViewById(R.id.progress_bar).setVisibility(View.GONE);
                ImageView status = (ImageView) tableRow.findViewById(R.id.ic_done);
                status.setVisibility(View.VISIBLE);
                if (Math.round(loadedFraction) < 1){
                    status.setImageResource(R.drawable.ic_clear_black);
                }
                else {
                    status.setImageResource(R.drawable.ic_done_black);
                }
                String loadedPercentage = String.format(Locale.getDefault(), "%.1f%%", loadedFraction * 100);
                ((TextView) tableRow.findViewById(R.id.loaded_percentage)).setText(loadedPercentage);
                String time;
                if (bufferingTime > 1000)
                    time = String.format(Locale.getDefault(), "%.2f s", bufferingTime / 1000.0);
                else
                    time = String.format(Locale.getDefault(), "%d ms", bufferingTime);
                ((TextView) tableRow.findViewById(R.id.loading_time)).setText(time);
                ((TextView) tableRow.findViewById(R.id.downloaded_size)).setText(Network.formatBytes(totalBytes));

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
                TableRow tableRow = (TableRow) inflater.inflate(R.layout.video_qualities_dialog_row, null);
                ((TextView) tableRow.findViewById(R.id.quality)).setText(qualitiesPixels[i]);

                tableLayout.addView(tableRow, index++);
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mediaTest.cancelTask();
            }
        });
        builder.setPositiveButton(android.R.string.ok, null);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_video_test_dialog, null);

        // Get visual elements
        webView = (WebView) view.findViewById(R.id.webView);
        tableLayout = (TableLayout) view.findViewById(R.id.video_qualities_table_layout);
        setCancelable(false);

        // Prepare Test
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String maxQuality = sharedPreferences.getString(getString(R.string.settings_video_test_max_quality_key), "None");
        if (maxQuality.equals("None"))
            getMaxQuality();
        else {
            mediaTest = new MediaTest(this, webView);
            setUpTextView();
            mediaTest.start();
        }

        // Create View
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setVisibility(View.GONE);
            }
        });
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        ActiveMeasurementsActivity.setEnabledButtons(true);
    }

    public void onMediaTestFinish() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.setVisibility(View.GONE);
                positiveButton.setVisibility(View.VISIBLE);
                negativeButton.setVisibility(View.GONE);
            }
        });
    }
}
