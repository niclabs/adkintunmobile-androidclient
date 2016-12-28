package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class SpeedTestReportDialog extends DialogFragment {

    public int value;

    public SpeedTestReportDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_activemeasurement_speedtest, null);

        // get data to populate
        String timestamp = bundle.getString("value");
        SpeedTestReport report = SpeedTestReport.findFirst(SpeedTestReport.class, "timestamp = ?", timestamp);

        // populate view with report data
        TextView tvInternetNetwork = (TextView) view.findViewById(R.id.tv_internet_network);
        setupNetworkInterface(tvInternetNetwork, report);

        ((TextView)view.findViewById(R.id.tv_date)).setText(DisplayDateManager.getDateString(report.timestamp));
        ((TextView)view.findViewById(R.id.tv_host)).setText(report.host);
        ((TextView)view.findViewById(R.id.tv_download_rate)).setText(Network.transferenceBitsSpeed(report.downloadSpeed));
        ((TextView)view.findViewById(R.id.tv_download_size)).setText(Network.formatBytes(report.downloadSize));
        ((TextView)view.findViewById(R.id.tv_upload_rate)).setText(Network.transferenceBitsSpeed(report.uploadSpeed));
        ((TextView)view.findViewById(R.id.tv_upload_size)).setText(Network.formatBytes(report.uploadSize));

        // set builder
        builder.setView(view);
        builder.setIcon(R.drawable.ic_speedometer_black);
        builder.setTitle(R.string.view_active_measurements_history_title_speedtest_report);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        // create
        return builder.create();
    }

    private void setupNetworkInterface(TextView tvInternetNetwork, SpeedTestReport report) {
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
