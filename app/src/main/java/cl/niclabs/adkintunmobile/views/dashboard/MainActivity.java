package cl.niclabs.adkintunmobile.views.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.services.SetupSystem;
import cl.niclabs.adkintunmobile.views.aboutus.AboutUsActivity;
import cl.niclabs.adkintunmobile.views.applicationstraffic.ApplicationsTrafficActivity;
import cl.niclabs.adkintunmobile.views.connectiontype.connectionmode.ConnectionModeActivity;
import cl.niclabs.adkintunmobile.views.connectiontype.connectionmode.ConnectionTypeFragment;
import cl.niclabs.adkintunmobile.views.connectiontype.networktype.NetworkTypeActivity;
import cl.niclabs.adkintunmobile.views.connectiontype.networktype.NetworkTypeFragment;
import cl.niclabs.adkintunmobile.views.notificationlog.NotificationLogFragment;
import cl.niclabs.adkintunmobile.views.rankings.RankingFragment;
import cl.niclabs.adkintunmobile.views.settings.SettingsActivity;
import cl.niclabs.adkintunmobile.views.status.StatusActivity;

public class MainActivity extends AppCompatActivity {

    private Context context;

    // Navigation drawer components
    private DrawerLayout mDrawer;
    //private Toolbar toolbar;
    private NavigationView nvDrawer;
    //private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        // Start System
        SetupSystem.startUpSystem(this.context);

        //setupToolBar();
        setupNavigationDrawer();

        // Initial Fragment: DashboardFragment
        updateMainFragment(new DashboardFragment());
    }

    /*
    private void setupToolBar() {
        // Setup toolbar as an actionbar
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    */

    private void setupNavigationDrawer() {
        // Setup general Layout: actionbar + main view + navigation drawer
        this.mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Setup animation of fade in/out nv_drawer
        //this.drawerToggle = setupDrawerToggle();
        //this.mDrawer.setDrawerListener(this.drawerToggle);

        // Setup Navigation Drawer
        this.nvDrawer = (NavigationView) findViewById(R.id.navigationView);
        setupDrawerContent(nvDrawer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, this.mDrawer, this.toolbar, R.string.drawer_open,  R.string.drawer_close);
    }
    */

    public void setupDrawerContent(NavigationView nv) {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                Intent myIntent;
                Fragment myFragment;
                switch (item.getItemId()) {
                    case R.id.nav_dashboard:
                        updateMainFragment(new DashboardFragment());
                        break;
                    case R.id.nav_notifications_log:
                        updateMainFragment(new NotificationLogFragment());
                        break;
                    case R.id.nav_status:
                        openStatusView(null);
                        break;
                    case R.id.nav_connection_mode:
                        openConnectionModeView(null);
                        break;
                    case R.id.nav_network_type:
                        openNetworkTypeView(null);
                        break;
                    case R.id.nav_carrier_ranking:
                        updateMainFragment(new RankingFragment());
                        break;
                    case R.id.nav_applications_traffic:
                        //updateMainFragment(new ApplicationsTrafficFragment());
                        openApplicationTrafficView(null);
                        break;
                    case R.id.nav_settings:
                        myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(myIntent);
                        break;
                    case R.id.nav_about_us:
                        myIntent = new Intent(getApplicationContext(), AboutUsActivity.class);
                        startActivity(myIntent);
                        break;
                }
                mDrawer.closeDrawers();
                return true;
            }
        });
    }

    public void openStatusView(View view){
        Intent myIntent = new Intent(getApplicationContext(), StatusActivity.class);
        startActivity(myIntent);
    }

    public void openApplicationTrafficView(View view){
        Intent myIntent = new Intent(getApplicationContext(), ApplicationsTrafficActivity.class);
        startActivity(myIntent);
    }

    public void openConnectionModeView(View view){
        Intent myIntent = new Intent(getApplicationContext(), ConnectionModeActivity.class);
        startActivity(myIntent);
    }

    public void openNetworkTypeView(View view){
        Intent myIntent = new Intent(getApplicationContext(), NetworkTypeActivity.class);
        startActivity(myIntent);
    }
    /*
     * Navigation Drawer Synchronization methods
     */
    /*
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }
    */

    /*
     * changeCurrentFragment Methods
     */
    public void updateMainFragment(Fragment newFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit);

        fragmentTransaction.replace(R.id.main_content, newFragment);
        fragmentTransaction.commit();
    }

}

