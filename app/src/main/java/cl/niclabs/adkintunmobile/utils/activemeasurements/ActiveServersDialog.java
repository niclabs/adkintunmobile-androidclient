package cl.niclabs.adkintunmobile.utils.activemeasurements;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;

public class ActiveServersDialog extends DialogFragment {

    static public final String TAG = "AdkM:DataQuotaDialog";

    private String selectedServer;

    public ActiveServersDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int count = bundle.getInt("count");
        String[] activeServers = new String[count];
        for (int i=0; i<count; i++){
            activeServers[i] = bundle.getString("serverUrl"+i);
        }
        final boolean shouldExecute = bundle.getBoolean("shouldExecute");
        final String[] finalServers = activeServers;
        selectedServer = finalServers[0];

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Seleccionar servidor")
                .setSingleChoiceItems(activeServers, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                selectedServer = finalServers[item];
                            }
                        }
                    );
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getActivity().getString(R.string.settings_speed_test_server_key), selectedServer);
                editor.apply();
                if (shouldExecute)
                    ((ActiveMeasurementsActivity) getActivity()).startSpeedTest();
            }
        });
        return builder.create();
    }
}
