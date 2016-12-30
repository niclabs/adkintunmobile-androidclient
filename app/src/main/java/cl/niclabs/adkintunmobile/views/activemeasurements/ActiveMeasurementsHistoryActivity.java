package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;

public class ActiveMeasurementsHistoryActivity extends AppCompatActivity {

    private final String TAG = "AdkM:AppHystoryAM";

    private String title, subtitle;
    private String testKey;
    private Context context;
    private Toolbar toolbar;
    private RelativeLayout loadingPanel;
    private int viewPagerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_measurements_history);

        setBaseActivityParams();
        setupToolbar();

        setupRefresh();
    }

    private void setupRefresh() {
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.colorPrimary,
                R.color.colorAccent,
                R.color.textColorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "Reportes Actualizados", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setSubtitle(this.subtitle);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setBaseActivityParams(){
        this.title = getString(R.string.view_active_measurements_history_title);

        viewPagerIndex = ActiveMeasurementsActivity.getCurrentItem();

        this.testKey = getIntent().getExtras().getString(getString(R.string.settings_active_measurements_key));
        if (this.testKey.equals(getString(R.string.settings_speed_test_category_key)))
            this.subtitle = getString(R.string.view_active_measurements_speedtest_title);
        if (this.testKey.equals(getString(R.string.settings_video_test_category_key)))
            this.subtitle = getString(R.string.view_active_measurements_mediatest_title);
        if (this.testKey.equals(getString(R.string.settings_connectivity_test_category_key)))
            this.subtitle = getString(R.string.view_active_measurements_connectivitytest_title);

        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);

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
