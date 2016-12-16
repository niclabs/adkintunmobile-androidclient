package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.ConnectivityTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SiteResult;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class ConnectivityTestReportDialog extends DialogFragment {

    public int value;
    public TableLayout tlSiteResults;

    public ConnectivityTestReportDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_activemeasurement_connectivitytest, null);

        tlSiteResults = (TableLayout) view.findViewById(R.id.tl_site_results);

        // get data to populate
        String timestamp = bundle.getString("value");
        ConnectivityTestReport report = ConnectivityTestReport.findFirst(ConnectivityTestReport.class, "timestamp = ?", timestamp);

        // populate view with report data
        TextView tvInternetNetwork = (TextView) view.findViewById(R.id.tv_internet_network);
        setupNetworkInterface(tvInternetNetwork, report);

        ((TextView)view.findViewById(R.id.tv_date)).setText(DisplayDateManager.getDateString(report.timestamp));

        for (SiteResult result : report.getSiteResults()){
            createSiteResult(
                    result.loaded,
                    result.url,
                    result.loadingTime,
                    result.downloadedBytes
            );
        }

        // set builder
        builder.setView(view);
        builder.setIcon(R.drawable.ic_link_black);
        builder.setTitle(R.string.view_active_measurements_history_title_connectivitytest_report);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        // create
        return builder.create();
    }

    private void createSiteResult(boolean loaded, String url, long loadingTime, long downloadedBytes) {
        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));

        TextView tvURL, tvLoadingTime, tvDownloadedBytes;
        tvURL = createResultTextView(url, 4);

        String time;
        if (loadingTime > 1000)
            time = String.format(Locale.getDefault(), "%.2f s", loadingTime/1000.0);
        else
            time = String.format(Locale.getDefault(), "%d ms", loadingTime);
        tvLoadingTime = createResultTextView(time, 2);
        tvDownloadedBytes = createResultTextView(Network.formatBytes(downloadedBytes), 2);

        Drawable img;
        if (loaded)
            img = getContext().getResources().getDrawable(R.drawable.ic_done_black);
        else
            img = getContext().getResources().getDrawable(R.drawable.ic_clear_black);
        img.setBounds( 0, 0, 60, 60);
        tvURL.setCompoundDrawables(img, null, null, null);


        tr.addView(tvURL);
        tr.addView(tvLoadingTime);
        tr.addView(tvDownloadedBytes);

        tlSiteResults.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public TextView createResultTextView(String customText, int layoutWeight){
        TextView resTv = new TextView(getContext());
        resTv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, layoutWeight));
        resTv.setText(customText);
        resTv.setTextAppearance(getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Subhead);
        return resTv;
    }

    private void setupNetworkInterface(TextView tvInternetNetwork, ConnectivityTestReport report) {
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
}
