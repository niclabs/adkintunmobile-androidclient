package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsHistoryActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ActiveMeasurementsSettingsActivity;

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

        ((TextView) view.findViewById(R.id.download_size)).setText(Network.formatBytes(downloadSize));
        ((TextView) view.findViewById(R.id.upload_size)).setText(Network.formatBytes(uploadSize));

        return view;
    }

    // TODO: Implementar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()){
            case R.id.menu_history_btn:
                myIntent = new Intent(getContext(), ActiveMeasurementsHistoryActivity.class);
                myIntent.putExtra(getString(R.string.settings_active_measurements_key), getString(R.string.settings_speed_test_category_key));
                startActivity(myIntent);
                return true;
            case R.id.menu_settings_btn:
                myIntent = new Intent(getContext(), ActiveMeasurementsSettingsActivity.class);
                myIntent.putExtra(getString(R.string.settings_active_measurements_key), R.string.settings_speed_test_category_key);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
