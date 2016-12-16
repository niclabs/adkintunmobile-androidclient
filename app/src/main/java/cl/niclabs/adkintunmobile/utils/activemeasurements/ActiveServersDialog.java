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

    private String selectedServerHost;
    private String selectedServerPort;

    public ActiveServersDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int count = bundle.getInt("count");
        String[] serverHosts = new String[count];
        String[] serverPorts = new String[count];
        for (int i=0; i<count; i++){
            serverHosts[i] = bundle.getString("serverHost"+i);
            serverPorts[i] = bundle.getString("serverPort"+i);
        }
        final boolean shouldExecute = bundle.getBoolean("shouldExecute");
        final String[] finalServerHosts = serverHosts;
        final String[] finalServerPorts = serverPorts;
        selectedServerHost = finalServerHosts[0];
        selectedServerPort = finalServerPorts[0];

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Seleccionar servidor")
                .setSingleChoiceItems(serverHosts, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                selectedServerHost = finalServerHosts[item];
                                selectedServerPort = finalServerPorts[item];
                            }
                        }
                    );
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getActivity().getString(R.string.settings_speed_test_server_host_key), selectedServerHost);
                editor.putString(getActivity().getString(R.string.settings_speed_test_server_port_key), selectedServerPort);
                editor.apply();
                if (shouldExecute)
                    ((ActiveMeasurementsActivity) getActivity()).startSpeedTest();
            }
        });
        return builder.create();
    }
}
