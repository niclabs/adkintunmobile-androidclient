package cl.niclabs.adkintunmobile.views.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.Map;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.services.SetupSystem;

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

        // Load the preferences from xml
        addPreferencesFromResource(R.xml.preferences);

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        updateSummaries();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(this.context.getString(R.string.settings_sampling_frequency_key))){
            SetupSystem.schedulleBroadcastReceivers(this.context);
        }

        updateSummary(key);
    }

    private void updateSummaries(){
        Map<String, ?> prefs = getPreferenceManager().getSharedPreferences().getAll();
        for (Map.Entry<String, ?> entry : prefs.entrySet()) {
            // Log.d(TAG,entry.getKey() + ": " + entry.getValue().toString() + " ___ ");
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
            pref.setSummary(getPreferenceManager().getSharedPreferences().getString(pref.getKey(), "-"));
            Log.d(TAG, "Cambiada las preferencia de sistema");
        }
    }
}
