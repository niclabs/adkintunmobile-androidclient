package cl.niclabs.adkintunmobile.views.settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import cl.niclabs.adkintunmobile.BuildConfig;
import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.IpLocation;
import cl.niclabs.adkintunmobile.services.SetupSystem;
import cl.niclabs.adkintunmobile.services.sync.Synchronization;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.status.DayOfRechargeDialog;
import cl.niclabs.adkintunmobile.views.status.DataQuotaDialog;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private final String TAG = "AdkM:Settings";
    private Context context;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();

        /* Load the preferences from xml */
        addPreferencesFromResource(R.xml.preferences);

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        updateSummaries();

        /* Remove preferences available only in debug mode */
        if (!BuildConfig.DEBUG_MODE) {
            PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.settings_sampling_category_key));
            ArrayList<Preference> debugPreferences = new ArrayList<>();
            debugPreferences.add(findPreference(getString(R.string.settings_sampling_hostname_key)));
            debugPreferences.add(findPreference(getString(R.string.settings_sampling_frequency_key)));
            debugPreferences.add(findPreference(getString(R.string.settings_sampling_startsync_key)));

            for (Preference preferenceToRemove : debugPreferences)
                preferenceCategory.removePreference(preferenceToRemove);

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(this.context.getString(R.string.settings_sampling_frequency_key))){
            SetupSystem.schedulleBroadcastReceivers(this.context);
        }

        updateSummary(key);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.settings_app_data_quota_total_key))) {
            /* Dialog for choose plan quota */
            FragmentManager fm = ((SettingsActivity) getActivity()).getSupportFragmentManager();
            DataQuotaDialog.showDialogPreference(fm, null);
        }
        if (key.equals(getString(R.string.settings_app_day_of_recharge_key))) {
            /* Dialog for choose recharge data plan quota */
            FragmentManager fm = ((SettingsActivity) getActivity()).getSupportFragmentManager();
            DayOfRechargeDialog.showDialogPreference(fm, null);
        }
        if (key.equals(getString(R.string.settings_sampling_startsync_key))){
            /* Start Sync process: create new report and try to send it */
            context.startService(new Intent(context, Synchronization.class));
        }
        if (key.equals(getString(R.string.settings_sampling_lastsync_key)) && BuildConfig.DEBUG_MODE){
            /* Dialog to show details of last sync process */
            FragmentManager fm = ((SettingsActivity) getActivity()).getSupportFragmentManager();
            SynchronizationLogDialog.showDialog(fm);
        }
        if (key.equals(getString(R.string.settings_app_data_clean_ip_location_cache_key))){
            /* Delete ipLocation records cached */
            IpLocation.cleanDB();
            Toast.makeText(this.context, getString(R.string.settings_app_data_clean_ip_location_cache_message), Toast.LENGTH_SHORT).show();
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
