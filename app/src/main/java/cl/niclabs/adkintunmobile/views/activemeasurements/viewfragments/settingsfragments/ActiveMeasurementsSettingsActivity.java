package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

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

        Bundle bundle = getIntent().getExtras();
        int activeMeasurementsKey = bundle.getInt(getString(R.string.settings_active_measurements_key));

        PreferenceFragment fragment = null;
        switch (ActiveMeasurementsActivity.getCurrentItem()){
            case 0:
                fragment = new SpeedTestSettingsFragment();
                break;
            case 1:
                fragment = new MediaTestSettingsFragment();
                break;
            case 2:
                fragment = new ConnectivityTestSettingsFragment();
                break;
        }

        viewPagerIndex = ActiveMeasurementsActivity.getCurrentItem();

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

}