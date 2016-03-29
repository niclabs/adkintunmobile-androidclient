package cl.niclabs.adkintunmobile.views.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.services.StartUp;
import cl.niclabs.adkintunmobile.views.aboutus.AboutUsActivity;

public class MainActivity extends AppCompatActivity {

    private Context context;

    // Navigation drawer components
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        setupToolBar();
        setupNavigationDrawer();

        // Start System
        StartUp.bootOnSystem(this.context);
    }

    private void setupToolBar() {
        // Setup toolbar as an actionbar
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupNavigationDrawer() {
        // Setup general Layout: actionbar + main view + navigation drawer
        this.mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Setup animation of fade in/out nv_drawer
        this.drawerToggle = setupDrawerToggle();
        this.mDrawer.setDrawerListener(this.drawerToggle);

        // Setup Navigation Drawer
        this.nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, this.mDrawer, this.toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    public void setupDrawerContent(NavigationView nv) {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_notifications_log:
                        break;
                    case R.id.nav_status:
                        item.setChecked(true);
                        setTitle(item.getTitle());
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        updateMainFragment(new Fragment());
                        break;
                    case R.id.nav_connection_type:
                        break;
                    case R.id.nav_signal_type:
                        item.setChecked(true);
                        setTitle(item.getTitle());
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        updateMainFragment(new Fragment());
                        break;
                    case R.id.nav_carrier_ranking:
                        break;
                    case R.id.nav_applications_traffic:
                        break;
                    case R.id.nav_settings:
                        break;
                    case R.id.nav_about_us:
                        Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                        startActivity(intent);
                        break;
                }
                mDrawer.closeDrawers();
                return true;
            }
        });
    }

    /*
     * Navigation Drawer Synchronization methods
     */
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

    /*
     * changeCurrentFragment Methods
     */
    public void updateMainFragment(Fragment newFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_fragment, newFragment);
        fragmentTransaction.commit();
    }
}

