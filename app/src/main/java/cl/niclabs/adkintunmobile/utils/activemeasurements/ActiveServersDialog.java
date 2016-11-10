package cl.niclabs.adkintunmobile.utils.activemeasurements;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;

public class ActiveServersDialog extends DialogFragment {

    static public final String TAG = "AdkM:DataQuotaDialog";

    private String selectedServer;

    private DialogInterface.OnDismissListener onDismissListener;

    public ActiveServersDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] activeServers = ActiveMeasurementsActivity.getServers();
        selectedServer = activeServers[0];

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Seleccionar servidor")
                .setSingleChoiceItems(activeServers, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                selectedServer = activeServers[item];
                            }
                        }
                    );
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ActiveMeasurementsActivity) getActivity()).startSpeedTest(5, selectedServer);
            }
        });
        return builder.create();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    static public void showDialogPreference(FragmentManager fm, DialogInterface.OnDismissListener onDismissListener){
        ActiveServersDialog editNameDialog = new ActiveServersDialog();
        editNameDialog.setOnDismissListener(onDismissListener);
        editNameDialog.show(fm, TAG);
    }

}
