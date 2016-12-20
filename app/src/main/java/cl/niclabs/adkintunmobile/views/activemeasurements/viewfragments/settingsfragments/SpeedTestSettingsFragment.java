package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersTask;

public class SpeedTestSettingsFragment extends ActiveMeasurementsSettingsFragment{

    private Context context;

    public SpeedTestSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();

        /* Load the preferences from xml */
        addPreferencesFromResource(R.xml.speed_test_preferences);

        updateSummaries();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.settings_speed_test_server_host_key))) {
            ActiveServersTask activeServersTask = new ActiveServersTask(getActivity()) {
                @Override
                public void handleActiveServers(Bundle bundle) {
                    FragmentManager fm = ((ActiveMeasurementsSettingsActivity) getActivity()).getSupportFragmentManager();
                    ActiveServersDialog dialog = new ActiveServersDialog();
                    dialog.setArguments(bundle);
                    dialog.show(fm, null);
                }
            };
            activeServersTask.execute();
        }

        if (key.equals(getString(R.string.settings_speed_test_reports_delete_key))){
            SpeedTestReport.deleteAllReports();
            Toast.makeText(context, getString(R.string.settings_active_measurements_reports_delete_toast), Toast.LENGTH_SHORT).show();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


}