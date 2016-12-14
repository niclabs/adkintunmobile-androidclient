package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
        TextView tvInternetNetwork = (TextView) view.findViewById(R.id.tv_internet_network);
        TextView tvVideoID = (TextView) view.findViewById(R.id.tv_host);

        tvDate.setText(DisplayDateManager.getDateString(report.timestamp));
        setupNetworkInterface(tvInternetNetwork, report);

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
        for (VideoResult vr : report.getVideoResults()){
            createVideoResult(
                    vr.quality,
                    Network.formatBytes(vr.downloadedBytes),
                    (vr.loadedFraction*100) + "%",
                    vr.bufferingTime + " ms");
            AddTableSeparation();
        }
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
        TableRow tr1 = new TableRow(getContext());
        tr1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT));

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
        tr2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT));

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
        resTv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0));
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

    public void AddTableSeparation(){
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(8, 8, 8, 8);

        TextView resTv = new TextView(getContext());
        resTv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
        resTv.setBackgroundColor(getResources().getColor(R.color.doughnut_no_info));

        ll.addView(resTv);

        tlVideoResults.addView(ll, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));;
    }
}
