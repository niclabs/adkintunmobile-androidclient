package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Map;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.services.SetupSystem;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersTask;
import cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest.AddSiteDialog;
import cl.niclabs.adkintunmobile.utils.files.FileManager;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;

public class ConnectivityTestSettingsFragment extends ActiveMeasurementsSettingsFragment{

    private Context context;
    MenuItem addSiteButton;
    MenuItem deleteButton;
    MenuItem confirmButton;

    public ConnectivityTestSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();

        /* Load the preferences from xml */
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        setHasOptionsMenu(true);
        int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);
        addPreferencesFromResource(R.xml.connectivity_test_preferences);
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.settings_connectivity_test_category_key));
        PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference(getString(R.string.settings_main_screen_key));
        preferenceScreen.removePreference(preferenceScreen.getPreference(0));

        for (int i=1; i<=sitesCount; i++){
            String siteTitle = sharedPreferences.getString(getString(R.string.settings_connectivity_test_site_) + i, "");
            Preference sitePreference = new Preference(context);
            sitePreference.setKey(getString(R.string.settings_connectivity_test_site_) + i);
            sitePreference.setSummary(siteTitle);
            preferenceCategory.addPreference(sitePreference);
        }
        updateSummaries();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (null != view) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            view.setOnKeyListener( new View.OnKeyListener()
            {
                @Override
                public boolean onKey( View v, int keyCode, KeyEvent event )
                {
                    if( keyCode == KeyEvent.KEYCODE_BACK ){
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            if (confirmButton.isVisible()){
                                PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.settings_connectivity_test_category_key));
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                                preferenceCategory.removeAll();

                                int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);

                                for (int i=1; i<=sitesCount; i++){
                                    String siteTitle = sharedPreferences.getString(getString(R.string.settings_connectivity_test_site_) + i, "");
                                    Preference sitePreference = new Preference(context);
                                    sitePreference.setKey(getString(R.string.settings_connectivity_test_site_) + i);
                                    sitePreference.setSummary(siteTitle);
                                    preferenceCategory.addPreference(sitePreference);
                                }
                                addSiteButton.setVisible(true);
                                deleteButton.setVisible(true);
                                confirmButton.setVisible(false);
                            }
                            else
                                getActivity().onBackPressed();
                            return true;
                        }
                    }
                    return false;
                }
            } );
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.settings_connectivity_test_category_key));

        switch (item.getItemId()){
            case R.id.menu_add_btn:
                FragmentManager fm = ((ActiveMeasurementsSettingsActivity) getActivity()).getSupportFragmentManager();
                AddSiteDialog dialog = new AddSiteDialog();
                dialog.setPreferenceFragment(this);
                dialog.show(fm, null);
                return true;
            case R.id.menu_delete_btn:
                if (preferenceCategory.getPreferenceCount() == 0)
                    return true;
                for (int i=0; i<preferenceCategory.getPreferenceCount(); i++){
                    Preference preference = preferenceCategory.getPreference(i);
                    preferenceCategory.removePreference(preference);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                    CheckBoxPreference newPreference = new CheckBoxPreference(context);
                    newPreference.setSummary(preference.getSummary());
                    newPreference.setKey(preference.getKey()+"_check");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(newPreference.getKey(), false);
                    editor.apply();

                    newPreference.setOrder(preference.getOrder());
                    preferenceCategory.addPreference(newPreference);
                }
                addSiteButton.setVisible(false);
                deleteButton.setVisible(false);
                confirmButton.setVisible(true);
                return true;
            case R.id.menu_confirm_btn:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int realIndex = 1;
                for (int i=0; i<preferenceCategory.getPreferenceCount(); i++) {
                    CheckBoxPreference preference = (CheckBoxPreference) preferenceCategory.getPreference(i);
                    if (preference.isChecked()){
                        editor.remove(getString(R.string.settings_connectivity_test_site_) + (i+1));
                    }
                    else {
                        if (realIndex < (i+1)) {
                            editor.putString(getString(R.string.settings_connectivity_test_site_) + realIndex, preference.getSummary().toString());
                            Log.d("COUNT", getString(R.string.settings_connectivity_test_site_) + realIndex);

                            editor.remove(getString(R.string.settings_connectivity_test_site_) + (i+1));
                        }
                        realIndex++;
                    }
                    editor.remove(getString(R.string.settings_connectivity_test_site_) + (i+1) + "_check");
                }

                preferenceCategory.removeAll();

                editor.putInt(getString(R.string.settings_connectivity_sites_count_key), realIndex-1);
                editor.apply();

                int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);

                for (int i=1; i<=sitesCount; i++){
                    String siteTitle = sharedPreferences.getString(getString(R.string.settings_connectivity_test_site_) + i, "");
                    Preference sitePreference = new Preference(context);
                    sitePreference.setKey(getString(R.string.settings_connectivity_test_site_) + i);
                    sitePreference.setSummary(siteTitle);
                    preferenceCategory.addPreference(sitePreference);
                }
                addSiteButton.setVisible(true);
                deleteButton.setVisible(true);
                confirmButton.setVisible(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.connectivity_test_settings, menu);
        addSiteButton = menu.getItem(0);
        deleteButton = menu.getItem(1);
        confirmButton = menu.getItem(2);
    }
}