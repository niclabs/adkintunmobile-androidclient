package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.ConnectivityTestPreferenceFragment;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.MediaTestPreferenceFragment;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.SpeedTestPreferenceFragment;

public class ActiveMeasurementsActivity extends AppCompatActivity {

    protected String title;
    protected Context context;
    protected Toolbar toolbar;

    protected ActiveMeasurementsViewPagerAdapter mViewPagerAdapter;
    protected ViewPager mViewPager;

    protected static int currentItem = 0;
    public static boolean running = false;
    public static boolean enabledButtons = true;


    public static int getCurrentItem() {
        return currentItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_measurements);

        setBaseActivityParams();
        setupToolbar();
        setUpViewPager();
        mViewPager.setCurrentItem(currentItem);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mViewPager.setCurrentItem(currentItem);
    }

    public static void setCurrentItem(int i) {
        currentItem = i;
    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        enabledButtons = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        currentItem = 0;
    }

    public static boolean isRunning(){
        return running;
    }

    public static void setEnabledButtons(boolean enabled){
        enabledButtons = enabled;
    }


    public void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    public void setBaseActivityParams(){
        this.title = getString(R.string.view_active_measurements);
        this.context = this;
    }

    private void setUpViewPager() {
        this.mViewPagerAdapter = new ActiveMeasurementsViewPagerAdapter(getSupportFragmentManager());
        SpeedTestPreferenceFragment f1 = new SpeedTestPreferenceFragment();
        MediaTestPreferenceFragment f2 = new MediaTestPreferenceFragment();
        ConnectivityTestPreferenceFragment f3 = new ConnectivityTestPreferenceFragment();

        this.mViewPagerAdapter.addFragment(f1);
        this.mViewPagerAdapter.addFragment(f2);
        this.mViewPagerAdapter.addFragment(f3);
        this.mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        this.mViewPager.setAdapter(this.mViewPagerAdapter);
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                setCurrentItem(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.active_measurements, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        String categoryKey;
        switch (mViewPager.getCurrentItem()){
            case 0:
                categoryKey = getString(R.string.settings_speed_test_category_key);
                break;
            case 1:
                categoryKey = getString(R.string.settings_video_test_category_key);
                break;
            default: //2
                categoryKey = getString(R.string.settings_connectivity_test_category_sites_key);
                break;
        }
        switch (item.getItemId()){
            case R.id.menu_history_btn:
                myIntent = new Intent(this, ActiveMeasurementsHistoryActivity.class);
                myIntent.putExtra(getString(R.string.settings_active_measurements_key), categoryKey);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Fragment getViewPagerItem(int i) {
        return mViewPagerAdapter.getItem(i);
    }
}

