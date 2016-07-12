package cl.niclabs.adkintunmobile.views.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.PointTarget;
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
import cl.niclabs.adkintunmobile.views.status.DayOfRechargeDialog;
import cl.niclabs.adkintunmobile.views.status.StatusActivity;
import cl.niclabs.adkintunmobile.views.status.DataQuotaDialog;

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
        final String[] tutorialTitle = getResources().getStringArray(R.array.tutorial_dashboard_title);
        final String[] tutorialBody = getResources().getStringArray(R.array.tutorial_dashboard_body);

        Display display = getWindowManager().getDefaultDisplay();

        Point initialTarget = new Point();

        display.getSize(initialTarget);
        initialTarget.y = (int) getResources().getDimension(R.dimen.dashboard_first_target_y);
        initialTarget.x /= 2;

        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(new PointTarget(initialTarget))
                .setContentTitle(tutorialTitle[helpCounter])
                .setContentText(tutorialBody[helpCounter])
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(37)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helpCounter++;
                        Target mTarget = Target.NONE;

                        switch (helpCounter) {
                            case 1:
                                mTarget = new ViewTarget(findViewById(R.id.shimmer_view_container));
                                break;

                            case 2:
                                mTarget = new ViewTarget(findViewById(R.id.tv_antenna));
                                break;

                            case 3:
                                mTarget = new ViewTarget(findViewById(R.id.tv_sim));
                                break;

                            case 4:
                                mTarget = new ViewTarget(findViewById(R.id.tv_signal));
                                break;

                            case 5:
                                mTarget = new ViewTarget(findViewById(R.id.tv_internet));
                                break;

                            case 6:
                                mTarget = new ViewTarget(findViewById(R.id.card_mobile_consumption));
                                break;

                            case 7:
                                mDrawer.openDrawer(GravityCompat.START);
                                Display display = getWindowManager().getDefaultDisplay();
                                Point pointTarget = new Point();

                                display.getSize(pointTarget);
                                pointTarget.y = (int) getResources().getDimension(R.dimen.extended_toolbar_dashboard_height);
                                pointTarget.x = 0;
                                mTarget = new PointTarget(pointTarget);
                                break;

                            case 8:
                                mDrawer.closeDrawers();
                                mTarget = new ViewTarget(findViewById(R.id.iv_collapsable_toolbar_app_icon));
                                showcaseView.setButtonText(getString(R.string.tutorial_close));
                                break;

                            default:
                                showcaseView.hide();
                                final FragmentManager fm = getSupportFragmentManager();
                                DataQuotaDialog.showDialogPreference(fm, new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        DayOfRechargeDialog.showDialogPreference(fm, null);
                                    }
                                });
                                return;
                        }
                        showcaseView.setContentTitle(tutorialTitle[helpCounter]);
                        showcaseView.setContentText(tutorialBody[helpCounter]);
                        showcaseView.setShowcase(mTarget, true);
                    }
                })
                .withNewStyleShowcase()
                .build();
        showcaseView.setButtonText(getString(R.string.tutorial_next));
    }
}

