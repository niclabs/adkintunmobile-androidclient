package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.services.SetupSystem;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersTask;
import cl.niclabs.adkintunmobile.utils.files.FileManager;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class ActiveMeasurementsSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private final String TAG = "AdkM:Settings";
    private Context context;

    public ActiveMeasurementsSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();

        /* Load the preferences from xml */
        Bundle bundle = this.getArguments();
        int activeMeasurementsKey = bundle.getInt(getString(R.string.settings_active_measurements_key));
        switch (activeMeasurementsKey){
            case R.string.settings_speed_test_category_key:
                addPreferencesFromResource(R.xml.speed_test_preferences);

        }

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        updateSummaries();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isAdded()) {

            if (key.equals(this.context.getString(R.string.settings_sampling_frequency_key))) {
                SetupSystem.schedulleBroadcastReceivers(this.context);
            }

            if (key.equals(getString(R.string.settings_sampling_compression_type_key))) {
                int deletedFiles = FileManager.deleteStoredReports(context);
                Toast.makeText(context, "Borrados " + deletedFiles + " reportes almacenados", Toast.LENGTH_SHORT).show();
            }

            updateSummary(key);
        }
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

    private void updateSummaries(){
        Map<String, ?> prefs = getPreferenceManager().getSharedPreferences().getAll();
        for (Map.Entry<String, ?> entry : prefs.entrySet()) {
            Log.d(TAG,entry.getKey() + ": " + entry.getValue().toString() + " ___ ");
            updateSummary(entry.getKey());
        }
    }

    private void updateSummary(String key){
        Preference pref = findPreference(key);

        if (pref instanceof ListPreference) {
            Log.d(TAG, "Cambiada las preferencia de Lista");
        } else if (pref instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) pref;
            pref.setSummary(editTextPreference.getText());
            Log.d(TAG, "Cambiada las preferencia de EditText");
        } else {
            if (pref != null){
                boolean inActivity = getActivity() != null;
                if (inActivity && pref.getKey() == getActivity().getString(R.string.settings_app_data_quota_total_key)){
                    int selectedOption = Integer.parseInt(getPreferenceManager().getSharedPreferences().getString(pref.getKey(), "0"));
                    long quota = Long.parseLong(getResources().getStringArray(R.array.data_quotas)[selectedOption]);
                    pref.setSummary(Network.formatBytes(quota));
                }
                else if (inActivity && pref.getKey() == getActivity().getString(R.string.settings_app_day_of_recharge_key)){
                    int selectedOption = Integer.parseInt(getPreferenceManager().getSharedPreferences().getString(pref.getKey(), "0"));
                    pref.setSummary(Integer.toString(selectedOption + 1));
                }else{
                    pref.setSummary(getPreferenceManager().getSharedPreferences().getString(pref.getKey(), "-"));
                }
                Log.d(TAG, "Cambiada las preferencia de sistema");
            }
        }
    }
}
