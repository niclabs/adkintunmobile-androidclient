package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Map;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;

public abstract class ActiveMeasurementsSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private final String TAG = "AdkM:Settings";
    protected Context context;
    protected String title;

    public ActiveMeasurementsSettingsFragment() {
    }

    public String getTitle() {
        return title;
    }

    protected void addStartButton(LinearLayout view, Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(120, 50, 120, 50);
        Button button = new Button(this.context);
        button.setText(getString(R.string.view_active_measurements_start_test));
        button.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorAccent),
                PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.WHITE);
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
        if(context != null && context instanceof ActiveMeasurementsActivity)
            ((ActiveMeasurementsActivity) context).makeNoConnectionToast();
    }
}