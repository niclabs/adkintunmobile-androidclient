package cl.niclabs.adkintunmobile.views.settings;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Map;

import cl.niclabs.adkintunmobile.BuildConfig;
import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.IpLocation;
import cl.niclabs.adkintunmobile.services.SetupSystem;
import cl.niclabs.adkintunmobile.services.sync.Synchronization;
import cl.niclabs.adkintunmobile.utils.files.FileManager;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.status.DataQuotaDialog;
import cl.niclabs.adkintunmobile.views.status.DayOfRechargeDialog;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private final String TAG = "AdkM:Settings";
    private Context context;
    private static final int REQUEST_READ_PHONE_STATE = 1;

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
            PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference(getString(R.string.settings_main_screen_key));
            PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.settings_sampling_category_key));
            preferenceScreen.removePreference(preferenceCategory);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isAdded()) {

            if (key.equals(getString(R.string.settings_sampling_frequency_key))) {
                SetupSystem.schedulleBroadcastReceivers(this.context);
            }

            if (key.equals(getString(R.string.settings_sampling_compression_type_key))) {
                int deletedFiles = FileManager.deleteStoredReports(context);
                Toast.makeText(context, "Borrados " + deletedFiles + " reportes almacenados", Toast.LENGTH_SHORT).show();
            }

            if (key.equals(getString(R.string.settings_app_daily_notification_key))){
                SetupSystem.setupDailyNotifications(this.context);
            }

            updateSummary(key);
        }
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
        if (key.equals(getString(R.string.settings_adkintun_web_qr_scanner_key))) {
            /* Init CaptureCodeActivity */
            checkReadPhonePermission();
        }
        if (key.equals(getString(R.string.settings_sampling_startsync_key))){
            /* Start Sync process: create new report and try to send it */
            context.startService(new Intent(context, Synchronization.class));
        }
        if (key.equals(getString(R.string.settings_sampling_delete_backup_key))){
            /* Delete Local Reports */
            int deletedFiles = FileManager.deleteStoredReports(context);
            Toast.makeText(context, "Borrados " + deletedFiles + " reportes almacenados", Toast.LENGTH_SHORT).show();
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

    private void checkReadPhonePermission() {
        final IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            scanIntegrator.setPrompt("");
            scanIntegrator.setBeepEnabled(false);
            scanIntegrator.setCaptureActivity(CaptureCodeActivity.class);
            scanIntegrator.initiateScan();
        }
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
        } else if (pref instanceof SwitchPreference){
            Log.d(TAG, "Cambiada las preferencia de Switch");
        } else {
            if (pref != null){
                boolean inActivity = getActivity() != null;
                if (inActivity && pref.getKey().equals(getActivity().getString(R.string.settings_app_data_quota_total_key))){
                    int selectedOption = Integer.parseInt(getPreferenceManager().getSharedPreferences().getString(pref.getKey(), "0"));
                    long quota = Long.parseLong(getResources().getStringArray(R.array.data_quotas)[selectedOption]);
                    pref.setSummary(Network.formatBytes(quota));
                }
                else if (inActivity && pref.getKey().equals(getActivity().getString(R.string.settings_app_day_of_recharge_key))){
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
