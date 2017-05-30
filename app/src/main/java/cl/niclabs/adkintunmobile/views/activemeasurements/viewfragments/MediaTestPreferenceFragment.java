package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.MediaTestReport;
import cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest.SitePreference;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.MediaTestDialog;

public class MediaTestPreferenceFragment extends ActiveMeasurementsPreferenceFragment {

    public MediaTestPreferenceFragment() {
        this.title = "Media";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        LinearLayout view = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);
        addStartButton(view, context);

        return view;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        this.context = getActivity();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String maxQuality = sharedPreferences.getString(getString(R.string.settings_video_test_max_quality_key), "None");

         /* Load the preferences from xml */
        addPreferencesFromResource(R.xml.media_test_preferences);
        PreferenceCategory mediaCategory = (PreferenceCategory) findPreference(getString(R.string.settings_video_test_quality_category_key));
        Preference preference = findPreference(getString(R.string.settings_video_test_max_quality_key));
        mediaCategory.removePreference(preference);

        for (int i = mediaCategory.getPreferenceCount() - 1; i >= 0; i--) {
            preference = mediaCategory.getPreference(i);
            if (preference.getTitle().equals(maxQuality))
                break;
            mediaCategory.removePreference(preference);
        }

        if (maxQuality.equals("None")) {
            Preference addSitePreference = new Preference(context) {
                @Override
                public void onBindViewHolder(PreferenceViewHolder holder) {
                    super.onBindViewHolder(holder);
                    Button button = (Button) holder.findViewById(R.id.add_site_btn);
                    button.setText(R.string.settings_video_test_get_qualities_preference);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onStartTestClick();
                        }
                    });
                }
            };
            addSitePreference.setLayoutResource(R.layout.button_preference);
            mediaCategory.addPreference(addSitePreference);
        }
        updateSummaries();
    }

    public void refreshView(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        PreferenceCategory mediaCategory = (PreferenceCategory) findPreference(getString(R.string.settings_video_test_quality_category_key));
        String maxQuality = sharedPreferences.getString(getString(R.string.settings_video_test_max_quality_key), "None");

        Preference preference = findPreference(getString(R.string.settings_video_test_max_quality_key));
        mediaCategory.removePreference(preference);


        for (int i = mediaCategory.getPreferenceCount() - 1; i >= 0; i--) {
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

    @Override
    public void onStartTestClick(){
        if (!ActiveMeasurementsActivity.enabledButtons)
            return;
        ActiveMeasurementsActivity.setEnabledButtons(false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("videoTestDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        MediaTestDialog newFragment = new MediaTestDialog();
        newFragment.show(ft, "videoTestDialog");
    }
}