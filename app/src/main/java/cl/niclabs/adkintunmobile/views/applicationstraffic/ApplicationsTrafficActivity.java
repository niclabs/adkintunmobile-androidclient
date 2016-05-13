package cl.niclabs.adkintunmobile.views.applicationstraffic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.utils.display.DisplayManager;

public class ApplicationsTrafficActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final String TAG = "AdkM:AppTrafficActivity";

    private String title;
    private Context context;
    private Toolbar toolbar;
    private RelativeLayout loadingPanel;

    private ApplicationsTrafficViewPagerAdapter mViewPagerAdapter;
    private ArrayList<ApplicationsTrafficListElement> wifiTrafficArray, mobileTrafficArray;
    private ApplicationsTrafficListFragment wifiListFragment, mobileListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications_traffic);

        setBaseActivityParams();
        setupToolbar();

        // Cargar datos de tráfico de apps del día actual
        (new Thread(){
            @Override
            public void run() {
                Date today = new Date(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long yesterday = calendar.getTimeInMillis();

                loadAppTrafficEventsData(yesterday);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupViewPager();
                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                    }
                });

            }
        }).start();
    }

    public void setBaseActivityParams(){
        this.title = getString(R.string.view_applications_traffic);
        this.context = this;
        this.loadingPanel = (RelativeLayout) findViewById(R.id.loading_panel);

        this.mViewPagerAdapter = new ApplicationsTrafficViewPagerAdapter(getSupportFragmentManager());
    }

    public void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.application_traffic, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_date_picker_btn:
                makeDateDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // Métodos de funcionamiento
    private void setupViewPager(){

        this.wifiListFragment = new ApplicationsTrafficListFragment();
        this.wifiListFragment.setTitle(getString(R.string.view_applications_traffic_wifi));
        this.wifiListFragment.setDataArray(this.wifiTrafficArray);

        this.mobileListFragment = new ApplicationsTrafficListFragment();
        this.mobileListFragment.setTitle(getString(R.string.view_applications_traffic_mobile));
        this.mobileListFragment.setDataArray(this.mobileTrafficArray);

        this.mViewPagerAdapter.addFragment(this.mobileListFragment);
        this.mViewPagerAdapter.addFragment(this.wifiListFragment);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.setAdapter(this.mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void trafficListsUpdate(){
        this.mobileListFragment.updateData(this.mobileTrafficArray);
        this.wifiListFragment.updateData(this.wifiTrafficArray);
    }

    private void loadAppTrafficEventsData(long initTime) {
        this.wifiTrafficArray = new ArrayList<ApplicationsTrafficListElement>();
        this.mobileTrafficArray = new ArrayList<ApplicationsTrafficListElement>();

        // Obtener aplicaciones con actividad desde initTime
        Iterator<ApplicationTraffic> apps = ApplicationTraffic.findWithQueryAsIterator(
                ApplicationTraffic.class,
                "SELECT DISTINCT uid, network_type from APPLICATION_TRAFFIC where timestamp >= ?",
                Long.toString(initTime));

        // Recuperar datos de cada aplicación
        while (apps.hasNext()) {
            ApplicationTraffic current = apps.next();
            ApplicationsTrafficListElement currentApp =
                    getAppTxSummary(
                            initTime,
                            current.uid,
                            current.networkType);

            if (current.networkType == ApplicationTraffic.WIFI)
                this.wifiTrafficArray.add(currentApp);
            if (current.networkType == ApplicationTraffic.MOBILE)
                this.mobileTrafficArray.add(currentApp);

        }

    }

    private ApplicationsTrafficListElement getAppTxSummary(long initTime, int uid, int networkType){
        ApplicationsTrafficListElement mApplicationTrafficListElement =
                new ApplicationsTrafficListElement(this.context, uid);
        Iterator<ApplicationTraffic> iterator =
                ApplicationTraffic.findAsIterator(
                        ApplicationTraffic.class, "timestamp >= ? and uid = ? and network_type = ?",
                        Long.toString(initTime),
                        Integer.toString(uid),
                        Integer.toString(networkType));

        while (iterator.hasNext()){
            ApplicationTraffic current = iterator.next();
            mApplicationTrafficListElement.updateRxBytes(current.rxBytes);
            mApplicationTrafficListElement.updateTxBytes(current.txBytes);
        }

        return mApplicationTrafficListElement;
    }

    private void makeDateDialog(){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.context,
                this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        final long initTime = calendar.getTimeInMillis();

        DisplayManager.enableLoadingPanel(this.loadingPanel);

        (new Thread(){
            @Override
            public void run() {
                loadAppTrafficEventsData(initTime);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trafficListsUpdate();
                        DisplayDateManager d = new DisplayDateManager(context);

                        DisplayManager.dismissLoadingPanel(loadingPanel, context);
                    }
                });
            }
        }).start();


    }
}
