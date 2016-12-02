package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Map;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.services.SetupSystem;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersTask;
import cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest.AddSiteDialog;
import cl.niclabs.adkintunmobile.utils.files.FileManager;

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

        if (key.equals(getString(R.string.settings_speed_test_server_key))) {
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
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


}