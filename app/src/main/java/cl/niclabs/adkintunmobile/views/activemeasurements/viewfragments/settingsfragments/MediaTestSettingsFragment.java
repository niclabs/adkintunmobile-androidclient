package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.MediaTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport;

public class MediaTestSettingsFragment extends ActiveMeasurementsSettingsFragment{

    private Context context;

    public MediaTestSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        /* Load the preferences from xml */
        addPreferencesFromResource(R.xml.media_test_preferences);
        String maxQuality = sharedPreferences.getString(getString(R.string.settings_video_test_max_quality_key), "240p");
        PreferenceCategory mediaCategory = (PreferenceCategory) findPreference(getString(R.string.settings_video_test_category_key));
        Preference preference = findPreference(getString(R.string.settings_video_test_max_quality_key));
        mediaCategory.removePreference(preference);

        for (int i=mediaCategory.getPreferenceCount()-1; i>0; i--){
            preference = mediaCategory.getPreference(i);
            if (preference.getTitle().equals(maxQuality))
                break;
            mediaCategory.removePreference(preference);
        }

        updateSummaries();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.settings_video_test_reports_delete_key))){
            MediaTestReport.deleteAllReports();
            Toast.makeText(context, getString(R.string.settings_active_measurements_reports_delete_toast), Toast.LENGTH_SHORT).show();
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}