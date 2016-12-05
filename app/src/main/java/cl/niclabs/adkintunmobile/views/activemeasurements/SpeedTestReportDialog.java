package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;

public class SpeedTestReportDialog extends DialogFragment {

    public int value;

    public SpeedTestReportDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_activemeasurement_speedtest, null);

        // populate view with report data
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvDate.setText(bundle.getString("value"));

        // set builder
        builder.setView(view);
        builder.setIcon(R.drawable.ic_speedometer_black);
        builder.setTitle(bundle.getInt("position") + " pos");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        // create
        return builder.create();
    }


}
