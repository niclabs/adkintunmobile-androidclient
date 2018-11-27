package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Map;

import cl.niclabs.adkintunmobile.R;

public abstract class ActiveMeasurementsPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private final String TAG = "AdkM:Settings";
    protected Context context;
    protected String title;

    public ActiveMeasurementsPreferenceFragment() {
    }

    public String getTitle() {
        return title;
    }

    protected void addStartButton(LinearLayout view, Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(120, 50, 120, 50);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        Button button = (Button) inflater.inflate(R.layout.button_start_test, null);

        view.addView(button, params);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onStartTestClick();
            }
        });
    }

    protected abstract void onStartTestClick();

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

    public void makeNoConnectionToast(){
        if(getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, getString(R.string.view_active_measurements_error_network_connection),
                            Toast.LENGTH_SHORT).show();
                }
            });
    }
}