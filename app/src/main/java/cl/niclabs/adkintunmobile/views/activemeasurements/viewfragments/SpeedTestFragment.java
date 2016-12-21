package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class SpeedTestFragment extends ActiveMeasurementViewFragment {

    public SpeedTestFragment() {
        this.title = "Velocidad";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_active_measurements_speedtest, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        long downloadSize = Long.parseLong(sharedPreferences.getString(getString(R.string.settings_speed_test_download_size_key), "1000000"));
        long uploadSize = Long.parseLong(sharedPreferences.getString(getString(R.string.settings_speed_test_upload_size_key), "1000000"));
        String serverName = sharedPreferences.getString(getString(R.string.settings_speed_test_server_name_key), "");

        ((TextView) view.findViewById(R.id.download_size)).setText(Network.formatBytes(downloadSize));
        ((TextView) view.findViewById(R.id.upload_size)).setText(Network.formatBytes(uploadSize));
        ((TextView) view.findViewById(R.id.server_name)).setText(serverName);

        return view;
    }
}
