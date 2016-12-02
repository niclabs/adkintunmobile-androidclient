package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cl.niclabs.adkintunmobile.R;

public class ActiveMeasurementsSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToolBar();

        Bundle bundle = getIntent().getExtras();
        int activeMeasurementsKey = bundle.getInt(getString(R.string.settings_active_measurements_key));

        PreferenceFragment fragment = null;
        switch (activeMeasurementsKey){
            case R.string.settings_speed_test_category_key:
                fragment = new SpeedTestSettingsFragment();
                break;
            case R.string.settings_video_test_category_key:
                fragment = new MediaTestSettingsFragment();
                break;
            case R.string.settings_connectivity_test_category_key:
                fragment = new ConnectivityTestSettingsFragment();
                break;
        }

        // display fragment for settings
        if (fragment != null)
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }

    /**
     * Setup toolbar as an actionbar
     */
    private void setupToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(getString(R.string.view_settings));
    }
}