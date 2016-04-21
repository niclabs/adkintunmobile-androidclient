package cl.niclabs.adkintunmobile.views.applicationstraffic;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.ApplicationTraffic;

public class ApplicationsTrafficFragment extends Fragment {

    private final String TAG = "AdkM:AppTrafficFragment";

    private String title;
    private Context context;

    private ApplicationsTrafficViewPagerAdapter mViewPagerAdapter;

    private ArrayList<ApplicationsTrafficListElement> wifiTrafficArray, mobileTrafficArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        this.title = getActivity().getString(R.string.view_applications_traffic);
        this.context = getActivity();

        this.wifiTrafficArray = new ArrayList<ApplicationsTrafficListElement>();
        this.mobileTrafficArray = new ArrayList<ApplicationsTrafficListElement>();

        this.mViewPagerAdapter = new ApplicationsTrafficViewPagerAdapter(getActivity().getSupportFragmentManager());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(this.title);
        final View view = inflater.inflate(R.layout.fragment_applications_traffic, container, false);

        // Cargar datos de tráfico de apps de las últimas 24 horas
        (new Thread(){
            @Override
            public void run() {

                Date today = new Date(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                long yesterday = calendar.getTimeInMillis();

                loadAppTrafficEvents(yesterday);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupViewPager(view);
                        dismissLoadingPane(view);
                    }
                });

            }
        }).start();

        return view;
    }

    private void setupViewPager(View view){
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        ApplicationsTrafficListFragment wifiList = new ApplicationsTrafficListFragment();
        wifiList.setTitle(this.context.getString(R.string.view_applications_traffic_wifi));
        wifiList.setDataArray(this.wifiTrafficArray);

        ApplicationsTrafficListFragment mobileList = new ApplicationsTrafficListFragment();
        mobileList.setTitle(this.context.getString(R.string.view_applications_traffic_mobile));
        mobileList.setDataArray(this.mobileTrafficArray);

        this.mViewPagerAdapter.addFragment(mobileList);
        this.mViewPagerAdapter.addFragment(wifiList);

        mViewPager.setAdapter(this.mViewPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void loadAppTrafficEvents(long initTime) {

        // Obtener aplicaciones con actividad desde initTime
        Iterator<ApplicationTraffic> apps = ApplicationTraffic.findWithQueryAsIterator(
                ApplicationTraffic.class,
                "SELECT DISTINCT uid, network_type from APPLICATION_TX where timestamp > ?",
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

    private void dismissLoadingPane(View view) {
        Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        RelativeLayout loadingPane = (RelativeLayout) view.findViewById(R.id.loading_panel);
        loadingPane.startAnimation(fadeOut);
        view.findViewById(R.id.loading_panel).setVisibility(View.GONE);
    }

}
