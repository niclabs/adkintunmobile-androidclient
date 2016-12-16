package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import java.net.HttpURLConnection;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersTask;
import cl.niclabs.adkintunmobile.utils.activemeasurements.CheckServerTask;
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
    protected void onStop() {
        super.onStop();
        currentItem = 0;
    }


        // TODO: Borrar y subir minsdk
    private void emulateClick(final WebView webview) {
        long delta = 100;
        long downTime = SystemClock.uptimeMillis();
        float x = webview.getLeft() + webview.getWidth()/2; //in the middle of the webview
        float y = webview.getTop() + webview.getHeight()/2;

        final MotionEvent motionEvent = MotionEvent.obtain( downTime, downTime + delta, MotionEvent.ACTION_DOWN, x, y, 0 );
        final MotionEvent motionEvent2 = MotionEvent.obtain( downTime + delta + 1, downTime + delta * 2, MotionEvent.ACTION_UP, x, y, 0 );
        Runnable tapdown = new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(motionEvent);
                }
            }
        };

        Runnable tapup = new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(motionEvent2);
                }
            }
        };

        int toWait = 0;
        int delay = 100;
        webview.postDelayed(tapdown, delay);
        delay += 100;
        webview.postDelayed(tapup, delay);

    }

    // TODO: Mover a fragment
    public void onSpeedTestClick(View view){
        checkServer();
    }

    // TODO: Mover a fragment
    public void onWebPagesTestClick(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("webPagesTestDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        ConnectivityTestDialog newFragment = new ConnectivityTestDialog();
        newFragment.show(ft, "webPagesTestDialog");
    }

    // TODO: Mover a fragment
    public void onVideoTestClick(View view){
        startMediaTest();
        //checkMaxQuality();
    }

    public void startMediaTest() {
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
}

