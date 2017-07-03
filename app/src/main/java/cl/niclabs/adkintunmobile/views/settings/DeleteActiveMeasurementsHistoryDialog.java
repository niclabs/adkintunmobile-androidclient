package cl.niclabs.adkintunmobile.views.settings;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.ConnectivityTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.MediaTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport;

public class DeleteActiveMeasurementsHistoryDialog extends DialogFragment {

    private CheckBox speedTestCheckBox;
    private CheckBox mediaTestCheckBox;
    private CheckBox connectivityTestCheckBox;

    public DeleteActiveMeasurementsHistoryDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Aceptar", null);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_delete_active_measurements_history_dialog, null);

        // Get visual elements
        speedTestCheckBox = (CheckBox) view.findViewById(R.id.check_box_speed_test);
        mediaTestCheckBox = (CheckBox) view.findViewById(R.id.check_box_media_test);
        connectivityTestCheckBox = (CheckBox) view.findViewById(R.id.check_box_connectivity_test);
        setCancelable(false);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (speedTestCheckBox.isChecked()){
                            SpeedTestReport.deleteAllReports();
                        }
                        if (mediaTestCheckBox.isChecked()){
                            MediaTestReport.deleteAllReports();
                        }
                        if (connectivityTestCheckBox.isChecked()){
                            ConnectivityTestReport.deleteAllReports();
                        }
                        if (speedTestCheckBox.isChecked() || mediaTestCheckBox.isChecked() || connectivityTestCheckBox.isChecked())
                            Toast.makeText(getActivity(), getString(R.string.settings_active_measurements_reports_delete_toast), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

}
