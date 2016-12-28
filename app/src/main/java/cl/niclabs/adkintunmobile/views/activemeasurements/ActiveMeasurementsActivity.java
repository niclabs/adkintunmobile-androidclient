package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.HttpURLConnection;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.connectivitytest.GetRecommendedSitesDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.ActiveServersTask;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.CheckServerTask;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.ConnectivityTestFragment;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.MediaTestFragment;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.SpeedTestFragment;
import cl.niclabs.adkintunmobile.views.activemeasurements.viewfragments.settingsfragments.ActiveMeasurementsSettingsActivity;

public class ActiveMeasurementsActivity extends AppCompatActivity {

    protected String title;
    protected Context context;
    protected Toolbar toolbar;

    protected ActiveMeasurementsViewPagerAdapter mViewPagerAdapter;
    protected ViewPager mViewPager;

    protected static int currentItem = 0;
    static boolean running = false;

    public static int getCurrentItem() {
        return currentItem;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setContentView(R.layout.activity_active_measurements);

        setBaseActivityParams();
        setupToolbar();
        setUpViewPager();
        mViewPager.setCurrentItem(currentItem);
    }

    public static void setCurrentItem(int i) {
        currentItem = i;
    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        currentItem = 0;
    }

    public void makeNoConnectionToast(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, getString(R.string.view_active_measurements_error_network_connection),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isRunning(){
        return running;
    }

    public void onSpeedTestClick(View view){
        checkServer();
    }

    public void onConnectivityTestClick(View view){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int sitesCount = sharedPreferences.getInt(getString(R.string.settings_connectivity_sites_count_key), 0);
        if (sitesCount > 0) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("webPagesTestDialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            ConnectivityTestDialog newFragment = new ConnectivityTestDialog();
            newFragment.show(ft, "webPagesTestDialog");
        }
        else{
            FragmentManager fm = getSupportFragmentManager();
            GetRecommendedSitesDialog dialog = new GetRecommendedSitesDialog();
            dialog.show(fm, null);
        }
    }
    public void onVideoTestClick(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("videoTestDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        MediaTestDialog newFragment = new MediaTestDialog();
        newFragment.show(ft, "videoTestDialog");
    }

    private void checkServer() {
        CheckServerTask checkServerTask = new CheckServerTask(this) {
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
                FragmentManager fm = getSupportFragmentManager();
                ActiveServersDialog dialog = new ActiveServersDialog();
                dialog.setArguments(bundle);
                dialog.show(fm, null);
            }
        };
        activeServersTask.execute();
    }

    public void startSpeedTest() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("speedTestDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        SpeedTestDialog newFragment = new SpeedTestDialog();
        newFragment.show(ft, "speedTestDialog");
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
        SpeedTestFragment f1 = new SpeedTestFragment();
        MediaTestFragment f2 = new MediaTestFragment();
        ConnectivityTestFragment f3 = new ConnectivityTestFragment();

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

        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_speedometer_white);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_ondemand_video_white);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_link_white);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.active_tests, menu);
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
                categoryKey = getString(R.string.settings_connectivity_test_category_key);
                break;
        }
        switch (item.getItemId()){
            case R.id.menu_history_btn:
                myIntent = new Intent(this, ActiveMeasurementsHistoryActivity.class);
                myIntent.putExtra(getString(R.string.settings_active_measurements_key), categoryKey);
                startActivity(myIntent);
                return true;
            case R.id.menu_settings_btn:
                myIntent = new Intent(this, ActiveMeasurementsSettingsActivity.class);
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

