package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;

public class ActiveMeasurementsSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int viewPagerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToolBar();

        String testKey = getIntent().getExtras().getString(getString(R.string.settings_active_measurements_key));

        viewPagerIndex = ActiveMeasurementsActivity.getCurrentItem();

        PreferenceFragmentCompat fragment;
        if (testKey.equals(getString(R.string.settings_speed_test_category_key))){
            fragment = new SpeedTestSettingsFragment();
        }
        else if (testKey.equals(getString(R.string.settings_connectivity_test_category_sites_key))) {
            fragment = new ConnectivityTestSettingsFragment();
        }
        else{
            fragment = new MediaTestSettingsFragment();
        }

        // display fragment for settings
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        ActiveMeasurementsActivity parent = (ActiveMeasurementsActivity) getParent();
        parent.setCurrentItem(viewPagerIndex);
        super.onBackPressed();
    }

    public void makeNoConnectionToast(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.view_active_measurements_error_network_connection),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}