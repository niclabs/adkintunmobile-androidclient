package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.MediaTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.VideoResult;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class MediaTestReportDialog extends DialogFragment {

    public int value;
    public TableLayout tlVideoResults;

    public MediaTestReportDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_activemeasurement_mediatest, null);

        tlVideoResults = (TableLayout) view.findViewById(R.id.tl_video_results);

        // get data to populate
        String timestamp = bundle.getString("value");
        MediaTestReport report = MediaTestReport.findFirst(MediaTestReport.class, "timestamp = ?", timestamp);

        // populate view with report data
        TextView tvInternetNetwork = (TextView) view.findViewById(R.id.tv_internet_network);
        setupNetworkInterface(tvInternetNetwork, report);

        ((TextView)view.findViewById(R.id.tv_date)).setText(DisplayDateManager.getDateString(report.timestamp));
        TextView tvVideoID = (TextView) view.findViewById(R.id.tv_host);

        for (VideoResult vr : report.getVideoResults()){
            String time;
            if (vr.bufferingTime > 1000)
                time = String.format(Locale.getDefault(), "%.2f s", vr.bufferingTime/1000.0);
            else
                time = String.format(Locale.getDefault(), "%d ms", vr.bufferingTime);

            String loadedPercentage = String.format(Locale.getDefault(), "%.1f%%", vr.loadedFraction*100);

            createVideoResult(
                    vr.quality,
                    Network.formatBytes(vr.downloadedBytes),
                    loadedPercentage,
                    time);
        }

        // set builder
        builder.setView(view);
        builder.setIcon(R.drawable.ic_ondemand_video_black);
        builder.setTitle(R.string.view_active_measurements_history_title_mediatest_report);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        // create
        return builder.create();
    }

    private void setupNetworkInterface(TextView tvInternetNetwork, MediaTestReport report) {
        if(report.networkInterface.activeInterface == ConnectionModeSample.MOBILE){
            tvInternetNetwork.setText(R.string.view_connection_mode_mobile);
            Drawable img = getContext().getResources().getDrawable(R.drawable.ic_02_mobile);
            img.setBounds( 0, 0, 60, 60 );
            tvInternetNetwork.setCompoundDrawables(img,null,null,null);
        }else if(report.networkInterface.activeInterface == ConnectionModeSample.WIFI){
            tvInternetNetwork.setText(report.networkInterface.ssid);
            Drawable img = getContext().getResources().getDrawable(R.drawable.ic_01_wifi);
            img.setBounds( 0, 0, 60, 60 );
            tvInternetNetwork.setCompoundDrawables(img,null,null,null);
        }else {
            // None
        }
    }

    public void createVideoResult(String resolution, String size, String percentage, String buffering){
        if (tlVideoResults.getChildCount() > 1)
            addTableSeparation();

        TableRow tr1 = new TableRow(getContext());
        tr1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));

        TextView tvQualityTitle, tvQualityValue, tvSizeTitle,tvSizeValue;

        tvQualityTitle = createInfoTextView(R.string.view_active_measurements_history_quality);
        tvQualityValue = createResultTextView(resolution);
        tvSizeTitle = createInfoTextView(R.string.view_active_measurements_history_size);
        tvSizeValue = createResultTextView(size);

        tr1.addView(tvQualityTitle);
        tr1.addView(tvQualityValue);
        tr1.addView(tvSizeTitle);
        tr1.addView(tvSizeValue);

        tlVideoResults.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


        TableRow tr2 = new TableRow(getContext());
        tr2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));

        TextView tvPercentageTitle, tvPercentageValue, tvBufferingTitle,tvBufferingValue;

        tvPercentageTitle = createInfoTextView(R.string.view_active_measurements_history_percentage);
        tvPercentageValue = createResultTextView(percentage);
        tvBufferingTitle = createInfoTextView(R.string.view_active_measurements_history_buffering);
        tvBufferingValue = createResultTextView(buffering);

        tr2.addView(tvPercentageTitle);
        tr2.addView(tvPercentageValue);
        tr2.addView(tvBufferingTitle);
        tr2.addView(tvBufferingValue);

        tlVideoResults.addView(tr2, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public TextView createResultTextView(String customText){
        TextView resTv = new TextView(getContext());
        resTv.setGravity(Gravity.CENTER);
        resTv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0));
        resTv.setText(customText);
        resTv.setTextAppearance(getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Light_SearchResult_Subtitle);
        return resTv;
    }

    public TextView createInfoTextView(int customTextRes){
        TextView resTv = new TextView(getContext());
        resTv.setGravity(Gravity.LEFT);
        resTv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0));
        resTv.setText(customTextRes);
        return resTv;
    }

    public void addTableSeparation(){
        View separator = new View(getContext());
        separator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_gray));
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, height);
        params.setMargins(0, 20, 0, 20);
        tlVideoResults.addView(separator, params);
    }
}
