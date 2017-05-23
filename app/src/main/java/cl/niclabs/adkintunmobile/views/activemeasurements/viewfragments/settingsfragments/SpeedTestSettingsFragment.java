package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.HttpURLConnection;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.ActiveServersTask;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.CheckServerTask;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.SpeedTestDialog;

public class SpeedTestSettingsFragment extends ActiveMeasurementsSettingsFragment{

    public SpeedTestSettingsFragment() {
        this.title = "Velocidad";
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
        addPreferencesFromResource(R.xml.speed_test_preferences);
        updateSummaries();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.settings_speed_test_server_name_key))) {
            ActiveServersTask activeServersTask = new ActiveServersTask(this) {
                @Override
                public void handleActiveServers(Bundle bundle) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    ActiveServersDialog dialog = new ActiveServersDialog();
                    dialog.setArguments(bundle);
                    dialog.show(fm, null);
                }
            };
            activeServersTask.execute();
        }

        if (key.equals(getString(R.string.settings_speed_test_reports_delete_key))){
            SpeedTestReport.deleteAllReports();
            Toast.makeText(context, getString(R.string.settings_active_measurements_reports_delete_toast), Toast.LENGTH_SHORT).show();
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onStartTestClick(){
        if (!ActiveMeasurementsActivity.enabledButtons)
            return;
        ActiveMeasurementsActivity.setEnabledButtons(false);
        checkServer();
    }

    private void checkServer() {
        CheckServerTask checkServerTask = new CheckServerTask(context) {
            @Override
            public void handleResponse(int responseCode) {
                if (responseCode == HttpURLConnection.HTTP_OK)
                    startSpeedTest();
                else
                    selectServer();
            }
        };
        checkServerTask.execute();
    }

    private void selectServer(){
        ActiveServersTask activeServersTask = new ActiveServersTask(this) {
            @Override
            public void handleActiveServers(Bundle bundle) {
                bundle.putBoolean("shouldExecute", true);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ActiveServersDialog dialog = new ActiveServersDialog();
                dialog.setArguments(bundle);
                dialog.setCancelable(false);
                dialog.show(fm, null);
            }
        };
        activeServersTask.execute();
    }

    public void startSpeedTest() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("speedTestDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        SpeedTestDialog newFragment = new SpeedTestDialog();
        newFragment.show(ft, "speedTestDialog");
    }
}