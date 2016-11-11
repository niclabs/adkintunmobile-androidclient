package cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cl.niclabs.adkintunmobile.R;

public class ActiveMeasurementsSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.context = this;

        setupToolBar();

        Bundle bundle = getIntent().getExtras();

        ActiveMeasurementsSettingsFragment fragment = new ActiveMeasurementsSettingsFragment();
        fragment.setArguments(bundle);

        // display fragment for settings
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
