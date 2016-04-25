package cl.niclabs.adkintunmobile.views.applicationstraffic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.ApplicationTraffic;

public class ApplicationsTrafficFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private final String TAG = "AdkM:AppTrafficFragment";

    private String title;
    private Context context;

    private ApplicationsTrafficViewPagerAdapter mViewPagerAdapter;
    private ArrayList<ApplicationsTrafficListElement> wifiTrafficArray, mobileTrafficArray;
    private ApplicationsTrafficListFragment wifiListFragment, mobileListFragment;

    private RelativeLayout loadingPanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        this.title = getActivity().getString(R.string.view_applications_traffic);
        this.context = getActivity();

        this.mViewPagerAdapter = new ApplicationsTrafficViewPagerAdapter(getActivity().getSupportFragmentManager());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(this.title);
        final View view = inflater.inflate(R.layout.fragment_applications_traffic, container, false);
        this.loadingPanel = (RelativeLayout) view.findViewById(R.id.loading_panel);

        // Cargar datos de tráfico de apps de las últimas 24 horas
        (new Thread(){
            @Override
            public void run() {
                Date today = new Date(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                long yesterday = calendar.getTimeInMillis();

                loadAppTrafficEventsData(yesterday);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupViewPager(view);
                        dismissLoadingPane();
                    }
                });

            }
        }).start();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.application_traffic, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    private void setupViewPager(View view){

        this.wifiListFragment = new ApplicationsTrafficListFragment();
        this.wifiListFragment.setTitle(this.context.getString(R.string.view_applications_traffic_wifi));
        this.wifiListFragment.setDataArray(this.wifiTrafficArray);

        this.mobileListFragment = new ApplicationsTrafficListFragment();
        this.mobileListFragment.setTitle(this.context.getString(R.string.view_applications_traffic_mobile));
        this.mobileListFragment.setDataArray(this.mobileTrafficArray);

        this.mViewPagerAdapter.addFragment(this.mobileListFragment);
        this.mViewPagerAdapter.addFragment(this.wifiListFragment);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
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
                "SELECT DISTINCT uid, network_type from APPLICATION_TRAFFIC where timestamp > ?",
                Long.toString(initTime));

        // Recuperar datos de cada aplicación
        while (apps.hasNext()) {
            ApplicationTraffic current = apps.next();
            ApplicationsTrafficListElement currentApp =
                    getAppTxSummary(
                            initTime,
                            current.uid,
                            current.networkType);

            if (current.networkType == ApplicationTraffic.EventType.WIFI.getValue())
                this.wifiTrafficArray.add(currentApp);
            if (current.networkType == ApplicationTraffic.EventType.MOBILE.getValue())
                this.mobileTrafficArray.add(currentApp);

        }

    }

    private ApplicationsTrafficListElement getAppTxSummary(long initTime, int uid, int networkType){
        ApplicationsTrafficListElement mApplicationTrafficListElement =
                new ApplicationsTrafficListElement(this.context, uid);
        Iterator<ApplicationTraffic> iterator =
                ApplicationTraffic.findAsIterator(
                        ApplicationTraffic.class, "timestamp > ? and uid = ? and network_type = ?",
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


    private void enableLoadingPane() {
        loadingPanel.setVisibility(View.VISIBLE);
    }

    private void dismissLoadingPane() {
        Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        loadingPanel.startAnimation(fadeOut);
        loadingPanel.setVisibility(View.GONE);
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
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        final long initTime = c.getTimeInMillis();

        enableLoadingPane();
        (new Thread(){
            @Override
            public void run() {
                loadAppTrafficEventsData(initTime);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trafficListsUpdate();
                        dismissLoadingPane();
                    }
                });
            }
        }).start();


    }
}
