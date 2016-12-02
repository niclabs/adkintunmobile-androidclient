package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.Map;

public class ActiveMeasurementsSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private final String TAG = "AdkM:Settings";
    private Context context;

    public ActiveMeasurementsSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        //updateSummaries();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isAdded()) {
            updateSummary(key);
        }
    }

    protected void updateSummaries(){
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
        } else if (pref instanceof CheckBoxPreference) {
            return;
        } else {
            if (pref != null){
                if (getPreferenceManager().getSharedPreferences().getAll().get(pref.getKey()) instanceof String) {
                    pref.setSummary(getPreferenceManager().getSharedPreferences().getString(pref.getKey(), "-"));
                } else if (getPreferenceManager().getSharedPreferences().getAll().get(pref.getKey()) instanceof Integer){
                    //pref.setSummary(getPreferenceManager().getSharedPreferences().getInt(pref.getKey(), 0));
                }
                Log.d(TAG, "Cambiada las preferencia de sistema");
            }
        }
    }
}