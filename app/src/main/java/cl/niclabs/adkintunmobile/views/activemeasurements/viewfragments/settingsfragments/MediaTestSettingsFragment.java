package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.MediaTestReport;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;

public class MediaTestSettingsFragment extends ActiveMeasurementsSettingsFragment{

    public MediaTestSettingsFragment() {
        this.title = "Media";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        LinearLayout view = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

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
                ((ActiveMeasurementsActivity) getActivity()).onMediaTestClick();
            }
        });

        return view;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
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
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.settings_video_test_reports_delete_key))){
            MediaTestReport.deleteAllReports();
            Toast.makeText(context, getString(R.string.settings_active_measurements_reports_delete_toast), Toast.LENGTH_SHORT).show();
        }

        return super.onPreferenceTreeClick(preference);
    }
}