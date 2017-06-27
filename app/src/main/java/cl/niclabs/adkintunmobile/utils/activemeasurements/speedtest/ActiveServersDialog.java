package cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.SpeedTestPreferenceFragment;

public class ActiveServersDialog extends DialogFragment {

    private String selectedServerHost;
    private String selectedServerPort;
    private String selectedServerName;

    public ActiveServersDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int count = bundle.getInt("count");
        String[] serverHosts = new String[count];
        String[] serverPorts = new String[count];
        final String[] serverNames = new String[count];
        for (int i=0; i<count; i++){
            serverHosts[i] = bundle.getString("serverHost"+i);
            serverPorts[i] = bundle.getString("serverPort"+i);
            serverNames[i] = bundle.getString("serverName"+i);
        }
        final boolean shouldExecute = bundle.getBoolean("shouldExecute");
        final String[] finalServerHosts = serverHosts;
        final String[] finalServerPorts = serverPorts;
        final String[] finalServerNames = serverNames;
        selectedServerHost = finalServerHosts[0];
        selectedServerPort = finalServerPorts[0];
        selectedServerName = finalServerNames[0];

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Seleccionar servidor")
                .setSingleChoiceItems(serverNames, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                selectedServerHost = finalServerHosts[item];
                                selectedServerPort = finalServerPorts[item];
                                selectedServerName = finalServerNames[item];
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
                editor.putString(getActivity().getString(R.string.settings_speed_test_server_name_key), selectedServerName);
                editor.apply();
                FragmentActivity activity = getActivity();
                if (activity != null && activity instanceof ActiveMeasurementsActivity) {
                    Fragment fragment = ((ActiveMeasurementsActivity) activity).getViewPagerItem(0);
                    if (fragment != null && fragment instanceof SpeedTestPreferenceFragment)
                        ((SpeedTestPreferenceFragment) fragment).onSharedPreferenceChanged(sharedPreferences,
                                getActivity().getString(R.string.settings_speed_test_server_name_key));
                    if (shouldExecute)
                        ((SpeedTestPreferenceFragment) fragment).startSpeedTest();
                }
            }
        });
        return builder.create();
    }


}
