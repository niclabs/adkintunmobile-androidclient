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

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NewsNotification;
import cl.niclabs.adkintunmobile.services.SetupSystem;
import cl.niclabs.adkintunmobile.utils.display.NotificationManager;
import cl.niclabs.adkintunmobile.views.aboutus.AboutUsActivity;
import cl.niclabs.adkintunmobile.views.activeconnections.ActiveConnectionsActivity;
import cl.niclabs.adkintunmobile.views.applicationstraffic.ApplicationsTrafficActivity;
import cl.niclabs.adkintunmobile.views.connectiontype.connectionmode.ConnectionModeActivity;
import cl.niclabs.adkintunmobile.views.connectiontype.networktype.NetworkTypeActivity;
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
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        this.context = this;

        // Start System
        SetupSystem.startUpSystem(this.context);

        // SetupToolBar();
        setupNavigationDrawer();

        // Initial Fragment: DashboardFragment
        updateMainFragment(new DashboardFragment());

        // Show tutorial
        showTutorial();
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
                        openNotificationView(null);
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
                        openApplicationTrafficView(null);
                        break;
                    case R.id.nav_active_connections:
                        openActiveConnectionsView(null);
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

    // TODO: Habilitar método cuando estén operativas las notificaciones
    public void openNotificationView(View view){
        //Intent myIntent = new Intent(getApplicationContext(), NotificationLogActivity.class);
        //startActivity(myIntent);
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

    public void openActiveConnectionsView(View view) {
        Intent myIntent = new Intent(getApplicationContext(), ActiveConnectionsActivity.class);
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

    public void notif(View view){
        NewsNotification notification = new NewsNotification(NewsNotification.INFO, "Probandolos", "Hasta el fin del fin");
        notification.save();

        NewsNotification n = NewsNotification.findFirst(
                NewsNotification.class,
                "timestamp = ?",
                NewsNotification.mostRecentlyTimestamp()+"");
        NotificationManager.showNotification(this, n.title, n.content);
    }

    private int helpCounter;
    private ShowcaseView showcaseView;

    private void showTutorial() {
        helpCounter = 0;
        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setContentTitle(getString(R.string.view_dashboard_tutorial_1_title))
                .setContentText(getString(R.string.view_dashboard_tutorial_1_body))
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(40)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (helpCounter) {
                            case 0:
                                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.shimmer_view_container)), true);
                                showcaseView.setContentTitle(getString(R.string.view_dashboard_tutorial_2_title));
                                showcaseView.setContentText(getString(R.string.view_dashboard_tutorial_2_body));
                                break;

                            case 1:
                                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.tv_antenna)), true);
                                showcaseView.setContentTitle(getString(R.string.view_dashboard_tutorial_3_title));
                                showcaseView.setContentText(getString(R.string.view_dashboard_tutorial_3_body));
                                break;

                            case 2:
                                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.tv_sim)), true);
                                showcaseView.setContentTitle(getString(R.string.view_dashboard_tutorial_4_title));
                                showcaseView.setContentText(getString(R.string.view_dashboard_tutorial_4_body));
                                break;

                            case 3:
                                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.tv_signal)), true);
                                showcaseView.setContentTitle(getString(R.string.view_dashboard_tutorial_5_title));
                                showcaseView.setContentText(getString(R.string.view_dashboard_tutorial_5_body));
                                break;

                            case 4:
                                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.tv_internet)), true);
                                showcaseView.setContentTitle(getString(R.string.view_dashboard_tutorial_6_title));
                                showcaseView.setContentText(getString(R.string.view_dashboard_tutorial_6_body));
                                break;

                            case 5:
                                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.card_mobile_consumption)), true);
                                showcaseView.setContentTitle(getString(R.string.view_dashboard_tutorial_7_title));
                                showcaseView.setContentText(getString(R.string.view_dashboard_tutorial_7_body));
                                break;

                            case 6:
                                mDrawer.openDrawer(GravityCompat.START);
                                showcaseView.setShowcase(new ViewTarget(mDrawer.getRootView()), true);
                                showcaseView.setContentTitle(getString(R.string.view_dashboard_tutorial_8_title));
                                showcaseView.setContentText(getString(R.string.view_dashboard_tutorial_8_body));
                                showcaseView.setButtonText(getString(R.string.tutorial_close));
                                break;

                            case 7:
                                mDrawer.closeDrawers();
                                showcaseView.setShowcase(Target.NONE, true);
                                showcaseView.setContentTitle(getString(R.string.view_dashboard_tutorial_9_title));
                                showcaseView.setContentText(getString(R.string.view_dashboard_tutorial_9_body));
                                showcaseView.setButtonText(getString(R.string.tutorial_close));
                                break;

                            case 8:
                                showcaseView.hide();
                                break;
                        }
                        helpCounter++;
                    }
                })
                .withNewStyleShowcase()
                .build();
        showcaseView.setButtonText(getString(R.string.tutorial_next));
    }
}

