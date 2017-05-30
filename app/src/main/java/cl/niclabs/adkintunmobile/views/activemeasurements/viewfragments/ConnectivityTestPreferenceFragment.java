package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.ConnectivityTestReport;
import cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest.AddSiteDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest.SitePreference;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.ConnectivityTestDialog;

public class ConnectivityTestPreferenceFragment extends ActiveMeasurementsPreferenceFragment {

    public ConnectivityTestPreferenceFragment() {
        this.title = "Conectividad";
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
        /* Load the preferences from xml */
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        setHasOptionsMenu(true);
        int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);
        addPreferencesFromResource(R.xml.connectivity_test_preferences);
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.settings_connectivity_test_category_sites_key));
        PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference(getString(R.string.settings_main_screen_key));
        preferenceScreen.removePreference(preferenceScreen.getPreference(0));

        for (int i=1; i<=sitesCount; i++){
            String siteTitle = sharedPreferences.getString(getString(R.string.settings_connectivity_test_site_) + i, "");
            SitePreference sitePreference = new SitePreference(this);
            sitePreference.setKey(getString(R.string.settings_connectivity_test_site_) + i);
            sitePreference.setSummary(siteTitle);
            preferenceCategory.addPreference(sitePreference);
        }
        Preference addSitePreference = new Preference(context) {
            @Override
            public void onBindViewHolder(PreferenceViewHolder holder) {
                super.onBindViewHolder(holder);
                Button button = (Button) holder.findViewById(R.id.add_site_btn);
                button.setText(R.string.settings_connectivity_add_site_preference);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSitePreference();
                    }
                });
            }
        };
        addSitePreference.setLayoutResource(R.layout.button_preference);
        preferenceScreen.addPreference(addSitePreference);

        updateSummaries();
    }

    public void refreshView(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.settings_connectivity_test_category_sites_key));

        preferenceCategory.removeAll();
        int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);

        for (int i=1; i<=sitesCount; i++){
            String siteTitle = sharedPreferences.getString(getString(R.string.settings_connectivity_test_site_) + i, "");
            SitePreference sitePreference = new SitePreference(this);
            sitePreference.setKey(getString(R.string.settings_connectivity_test_site_) + i);
            sitePreference.setSummary(siteTitle);
            preferenceCategory.addPreference(sitePreference);
        }
        updateSummaries();
    }

    public void addSitePreference(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddSiteDialog dialog = new AddSiteDialog();
        dialog.setPreferenceFragment(this);
        dialog.show(fm, null);
    }

    public void deleteSitePreference(String key){
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.settings_connectivity_test_category_sites_key));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int realIndex = 1;
        for (int i=0; i<preferenceCategory.getPreferenceCount(); i++) {
            SitePreference preference = (SitePreference) preferenceCategory.getPreference(i);
            if (preference.getKey().equals(key)){
                editor.remove(key);
            }
            else {
                if (realIndex < (i+1)) {
                    editor.putString(getString(R.string.settings_connectivity_test_site_) + realIndex, preference.getSummary().toString());
                    editor.remove(getString(R.string.settings_connectivity_test_site_) + (i+1));
                }
                realIndex++;
            }
        }

        preferenceCategory.removeAll();

        editor.putInt(getString(R.string.settings_connectivity_sites_count_key), realIndex-1);
        editor.apply();

        int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);

        for (int i=1; i<=sitesCount; i++){
            String siteTitle = sharedPreferences.getString(getString(R.string.settings_connectivity_test_site_) + i, "");
            SitePreference sitePreference = new SitePreference(this);
            sitePreference.setKey(getString(R.string.settings_connectivity_test_site_) + i);
            sitePreference.setSummary(siteTitle);
            preferenceCategory.addPreference(sitePreference);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.settings_connectivity_reports_delete_key))){
            ConnectivityTestReport.deleteAllReports();
            Toast.makeText(context, getString(R.string.settings_active_measurements_reports_delete_toast), Toast.LENGTH_SHORT).show();
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onStartTestClick(){
        if (!ActiveMeasurementsActivity.enabledButtons)
            return;
        ActiveMeasurementsActivity.setEnabledButtons(false);
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (sitesCount > 0) {
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("webPagesTestDialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            ConnectivityTestDialog newFragment = new ConnectivityTestDialog();
            newFragment.show(ft, "webPagesTestDialog");
        }
        else{
            ActiveMeasurementsActivity.setEnabledButtons(true);
            AddSiteDialog dialog = new AddSiteDialog();
            dialog.setPreferenceFragment(this);
            dialog.setChecked(true);
            dialog.show(fm, null);
        }
    }
}